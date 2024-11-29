package com.winteralexander.hanzirp;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO Undocumented :(
 * <p>
 * Created on 2024-11-28.
 *
 * @author Alexander Winter
 */
public class Main {
	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		汉字ResourcePackerConfig config = gson.fromJson(new String(Files.readAllBytes(Path.of(args[0]))),
				汉字ResourcePackerConfig.class);
		System.out.println(config);
	}
}
