package com.winteralexander.hanzirp;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Map;

/**
 * Generator for the pack
 * <p>
 * Created on 2024-11-28.
 *
 * @author Alexander Winter
 */
public class Main {
	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		翻译Reader reader = new 翻译Reader(gson);
		Path configFile = Path.of(args[0]);

		汉字资源包制作者配置 config = gson.fromJson(new String(Files.readAllBytes(configFile)),
				汉字资源包制作者配置.class);

		File baseDir = configFile.getParent().toFile();

		Map<String, String> 翻译 = reader.读翻译(new File(baseDir, config.translationPath));
		Map<String, String> 拼音 = reader.读翻译(new File(baseDir, config.pinyinPath));

		File outFolder = new File(config.exportFolder);
		outFolder.mkdirs();

		for(File file : new File(new File(baseDir, config.basePackPath), "minecraft/textures/block/").listFiles()) {
			String originalName = file.getName();

			if(!originalName.toLowerCase(Locale.ROOT).endsWith("png"))
				continue;
			String name = originalName;

			name = name.substring(0, name.length() - 4);

			if(!翻译.containsKey("block.minecraft." + name)) {
				if(name.endsWith("_top"))
					name = name.substring(0, name.length() - 4);

				if(!翻译.containsKey("block.minecraft." + name))
					continue;
			}

			BufferedImage back = ImageIO.read(file);

			if(back.getWidth() != back.getHeight())
				continue; // skip non square images

			BufferedImage img = 汉字纹理制作者.制作纹理(翻译.get("block.minecraft." + name),
					config.includePinyin ? 拼音.get("block.minecraft." + name) : null,
					config,
					config.keepTextures ? back : null);

			File out = new File(outFolder, "assets/minecraft/textures/block/" + name + ".png");
			out.mkdirs();

			ImageIO.write(img, "PNG", out);
		}

		Files.writeString(new File(outFolder, "pack.mcmeta").toPath(),
				"""
				{
				  "pack": {
				    "pack_format": %d,
				    "description": "%s"
				  }
				}""".formatted(config.packFormat, config.packName),
				StandardCharsets.UTF_8,
				StandardOpenOption.CREATE_NEW);
	}
}
