package com.winteralexander.hanzirp;

import com.google.gson.Gson;

import java.io.File;
import java.nio.file.Files;
import java.util.SortedMap;

/**
 * Program to convert minecraft chinese translation (json) to pinyin for usage by the main program
 * <p>
 * Created on 2024-11-30.
 *
 * @author Alexander Winter
 */
public class Pinyinify {
	public static void main(String[] args) throws Throwable {
		Gson gson = new Gson();

		翻译Reader reader = new 翻译Reader(gson);

		SortedMap<String, String> map = reader.读翻译(new File("in/translation/zh_cn.json"));

		StringBuilder sb = new StringBuilder();

		for(SortedMap.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			if(!key.startsWith("block.minecraft"))
				continue;

			String value = entry.getValue();
			sb.append(value).append("|");
		}
		sb.deleteCharAt(sb.length() - 1);
		String input = sb.toString();
		String output = pinyinify(input);

		String[] res = output.split("\\|");
		int i = 0;
		for(String key : map.keySet()) {
			map.put(key, res[i].trim().replaceAll("(\\s|​)+", " "));
			i++;
		}

		reader.写翻译(new File("out/pinyin.json"), map);
	}

	public static String pinyinify(String hanzi) throws Throwable {
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("bash", "-c",
				"echo \"h=require('hanzi-tools');console.log(h.pinyinify('" + hanzi + "'));" +
						"\" > out/hanzi.js; node out/hanzi.js");
		File file = new File("out/hanzi.txt");
		pb.redirectOutput(file);
		Process p = pb.start();
		p.waitFor();
		return Files.readString(file.toPath()).trim();
	}
}
