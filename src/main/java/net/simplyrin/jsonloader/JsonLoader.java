package net.simplyrin.jsonloader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by SimplyRin on 2018/05/29.
 *
 *  Copyright 2018 SimplyRin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class JsonLoader {

	private JsonObject jsonObject;

	public JsonLoader() {
		this.jsonObject = new JsonObject();
	}

	public JsonLoader(String jsonText) {
		this.jsonObject = new JsonParser().parse(jsonText).getAsJsonObject();
	}

	public JsonLoader(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public JsonLoader getJsonObject(String key) {
		return new JsonLoader(this.jsonObject.get(key).getAsJsonObject());
	}

	public void put(String property, String text) {
		this.jsonObject.addProperty(property, text);
	}

	@Deprecated
	public void put(String property, List<String> list) {
		this.jsonObject.addProperty(property, list.toString());
	}

	public void put(String property, boolean _boolean) {
		this.jsonObject.addProperty(property, _boolean);
	}

	public void put(String property, int _int) {
		this.jsonObject.addProperty(property, _int);
	}

	public void put(String property, long _long) {
		this.jsonObject.addProperty(property, _long);
	}

	public void put(String property, float _float) {
		this.jsonObject.addProperty(property, _float);
	}

	public void put(String property, double _double) {
		this.jsonObject.addProperty(property, _double);
	}

	public boolean has(String property) {
		return this.jsonObject.has(property);
	}

	public String getString(String property) {
		return this.jsonObject.get(property).getAsString();
	}

	@Deprecated
	public List<String> getStringList(String property) {
		List<String> list = new ArrayList<>();
		for(String value : this.getString(property).replace("[", "").replace("]", "").split(", ")) {
			list.add(value);
		}
		return list;
	}

	public boolean getBoolean(String property) {
		return this.jsonObject.get(property).getAsBoolean();
	}

	public int getInt(String property) {
		return this.jsonObject.get(property).getAsInt();
	}

	public float getFloat(String property) {
		return this.jsonObject.get(property).getAsFloat();
	}

	public long getLong(String property) {
		return this.jsonObject.get(property).getAsLong();
	}

	public double getDouble(String property) {
		return this.jsonObject.get(property).getAsDouble();
	}

	public JsonObject getJsonObject() {
		return this.jsonObject;
	}

	@Override
	public String toString() {
		return this.jsonObject.toString();
	}

	public static void saveJson(JsonLoader json, String path) {
		saveJson(json, new File(path));
	}

	public static void saveJson(JsonLoader json, File file) {
		try {
			FileWriter filewriter = new FileWriter(file);
			filewriter.write(json.toString());
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JsonLoader getJson(String path) {
		return getJson(new File(path));
	}

	public static JsonLoader getJson(File file) {
		try {
			return new JsonLoader(Files.lines(Paths.get(file.getPath()), Charset.defaultCharset()).collect(Collectors.joining(System.getProperty("line.separator"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonLoader loadJson(String path) {
		return getJson(new File(path));
	}

	public static JsonLoader loadJson(File file) {
		return getJson(file);
	}

}
