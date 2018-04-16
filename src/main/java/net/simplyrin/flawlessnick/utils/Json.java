package net.simplyrin.flawlessnick.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import club.sk1er.utils.JsonHolder;

/**
 * Created by SimplyRin on 2018/03/01
 *
 * MIT License
 *
 * Copyright (c) 2018 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Json {

	public static void saveJson(JsonHolder json, String path) {
		saveJson(json, new File(path));
	}

	public static void saveJson(JsonHolder json, File file) {
		try {
			FileWriter filewriter = new FileWriter(file);
			filewriter.write(json.toString());
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JsonHolder getJson(String path) {
		return getJson(new File(path));
	}

	public static JsonHolder getJson(File file) {
		try {
			return new JsonHolder(Files.lines(Paths.get(file.getPath()), Charset.defaultCharset()).collect(Collectors.joining(System.getProperty("line.separator"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonHolder loadJson(String path) {
		return getJson(new File(path));
	}

	public static JsonHolder loadJson(File file) {
		return getJson(file);
	}

}
