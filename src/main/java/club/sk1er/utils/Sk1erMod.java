package club.sk1er.utils;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class Sk1erMod {

	public static String rawWithAgent(String url) {
		return rawWithAgent(url, null);
	}

	public static String rawWithAgent(String url, String args) {
		return rawWithAgent(url, args, "Mozilla/4.77");
	}

	public static String rawWithAgent(String url, String args, String userAgent) {
		System.out.println("Fetching: " + url);

		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			if(args != null) {
				connection.setRequestMethod("POST");
			} else {
				connection.setRequestMethod("GET");
			}
			connection.setUseCaches(true);
			connection.addRequestProperty("User-Agent", userAgent);
			connection.setReadTimeout(15000);
			connection.setConnectTimeout(15000);
			connection.setDoOutput(true);

			if(args != null) {
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(args);
				out.close();
				connection.connect();
			}

			InputStream is = connection.getInputStream();
			Charset encoding = Charset.defaultCharset();
			String s = IOUtils.toString(is, encoding);
			if (s != null) {
				return s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}