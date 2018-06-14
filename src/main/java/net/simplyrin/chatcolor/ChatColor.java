package net.simplyrin.chatcolor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by SimplyRin on 2018/06/01.
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
public class ChatColor {

	private static List<String> colorList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c" , "d", "e", "f", "k", "l", "m", "n", "o", "r");

	public static String translate(String character, String message) {
		for(String color : colorList) {
			message = message.replace(character + color, "§" + color);
		}

		return message;
	}

	public static String remove(String message) {
		for(String color : colorList) {
			message = message.replace("§" + color, "");
		}

		return message;
	}

}
