package com.winteralexander.hanzirp;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Undocumented :(
 * <p>
 * Created on 2024-11-30.
 *
 * @author Alexander Winter
 */
public class 翻译Reader {
	private final Gson gson;

	public 翻译Reader(Gson gson) {
		this.gson = gson;
	}

	public Map<String, String> 读翻译(File file) throws IOException {
		JsonReader reader = gson.newJsonReader(new FileReader(file));
		Map<String, String> hashMap = new HashMap<>();
		reader.beginObject();
		while(reader.hasNext()) {
			String key = reader.nextName();
			String value = reader.nextString();
			hashMap.put(key, value);
		}

		return hashMap;
	}
}
