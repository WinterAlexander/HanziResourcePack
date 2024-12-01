package com.winteralexander.hanzirp;

import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
		汉字资源包制作者配置 config = gson.fromJson(new String(Files.readAllBytes(Path.of(args[0]))),
				汉字资源包制作者配置.class);

		Map<String, String> 翻译 = reader.读翻译(new File("in/", config.translationPath));
		Map<String, String> 拼音 = reader.读翻译(new File("in/", config.pinyinPath));

		for(File file : new File(new File("in/", config.basePackPath), "minecraft/textures/block/").listFiles()) {
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

			File out = new File("out/assets/minecraft/textures/block/" + originalName);
			out.mkdirs();

			ImageIO.write(img, "PNG", out);
		}
	}
}
