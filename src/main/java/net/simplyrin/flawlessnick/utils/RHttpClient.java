package net.simplyrin.flawlessnick.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SimplyRin on 2018/05/26.
 *
 * Version: 1.2-SNAPSHOT
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
public class RHttpClient {

	private static boolean showLog = true;
	private static boolean showTime = true;
	private static boolean printErrorLog = false;

	private static int connectTimeout = 10000;
	private static int readTimeout = 10000;

	private static String userAgent = "Mozilla/5.0";
	private static String charset = "UTF-8";

	@Deprecated
	public static String raw(String link) {
		return connect(link);
	}

	public static String connect(String link) {
		return connect(link, null);
	}

	public static String connect(String link, String postText) {
		if(showLog) {
			System.out.println((showTime ? getTimePrefix() + " " : "") + "Connecting to " + link + ".");
		}
		try {
			URL url = new URL(link);
			HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
			if(postText != null) {
				connection.setRequestMethod("POST");
			} else {
				connection.setRequestMethod("GET");
			}
			connection.addRequestProperty("User-Agent", userAgent);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			if(postText != null) {
				connection.setDoOutput(true);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
				outputStreamWriter.write(postText);
				outputStreamWriter.close();
			}
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(charset)));
			String line, output = "";
			while((line = bufferedReader.readLine()) != null) {
				output += line + "\n";
			}
			return output;
		} catch (Exception e) {
			if(printErrorLog) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void setShowLog(boolean showLog) {
		RHttpClient.showLog = showLog;
	}

	public static void setShowTime(boolean showTime) {
		RHttpClient.showTime = showTime;
	}

	public static void setPrintErrorLog(boolean printErrorLog) {
		RHttpClient.printErrorLog = printErrorLog;
	}

	public static void setConnectTimeout(int connectTimeout) {
		RHttpClient.connectTimeout = connectTimeout;
	}

	public static void setReadTimeout(int readTimeout) {
		RHttpClient.readTimeout = readTimeout;
	}

	public static void setUserAgent(String userAgent) {
		RHttpClient.userAgent = userAgent;
	}

	public static void setCharset(String charset) {
		RHttpClient.charset = charset;
	}

	private static String getTimePrefix() {
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
		return sdf.format(new Date());
	}

}
