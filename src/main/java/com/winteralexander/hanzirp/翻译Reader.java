package com.winteralexander.hanzirp;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.source.tree.Tree;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

	public SortedMap<String, String> 读翻译(File file) throws IOException {
		JsonReader reader = gson.newJsonReader(new FileReader(file));
		SortedMap<String, String> hashMap = new TreeMap<>();
		reader.beginObject();
		while(reader.hasNext()) {
			String key = reader.nextName();
			String value = reader.nextString();
			hashMap.put(key, value);
		}
		reader.close();

		return hashMap;
	}

	public void 写翻译(File file, SortedMap<String, String> map) throws IOException {
		JsonWriter writer = gson.newJsonWriter(new FileWriter(file));
		writer.setFormattingStyle(FormattingStyle.PRETTY);
		writer.beginObject();
		for(Map.Entry<String, String> entry : map.entrySet()) {
			writer.name(entry.getKey());
			writer.value(entry.getValue());
		}

		writer.endObject();
		writer.close();
	}
}
