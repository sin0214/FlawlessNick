package club.sk1er.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Created by mitchellkatz on 9/28/17. Designed for production use on Sk1er.club
 */
public class JsonHolder {

	/*
	 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
	 *
	 *     This program is free software: you can redistribute it and/or modify
	 *     it under the terms of the GNU Lesser General Public License as published
	 *     by the Free Software Foundation, either version 3 of the License, or
	 *     (at your option) any later version.
	 *
	 *     This program is distributed in the hope that it will be useful,
	 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
	 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 *     GNU Lesser General Public License for more details.
	 *
	 *     You should have received a copy of the GNU Lesser General Public License
	 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
	 */
	private JsonObject object;

	public JsonHolder(JsonObject object) {
		this.object = object;
	}

	public JsonHolder(String raw) {
		this(new JsonParser().parse(raw).getAsJsonObject());
	}

	public JsonHolder() {
		this(new JsonObject());
	}

	public static String[] getNames(JsonHolder statsObject) {
		List<String> keys = statsObject.getKeys();
		String[] keyArray = new String[keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			keyArray[i] = keys.get(i);
		}
		return keyArray;
	}

	@Override
	public String toString() {
		return object.toString();
	}

	public JsonHolder put(String key, boolean value) {
		object.addProperty(key, value);
		return this;
	}

	public void mergeNotOverride(JsonHolder merge) {
		merge(merge, false);
	}

	public void mergeOverride(JsonHolder merge) {
		merge(merge, true);
	}

	public void merge(JsonHolder merge, boolean override) {
		JsonObject object = merge.getObject();
		for (String s : merge.getKeys()) {
			if (override || !this.has(s))
				put(s, object.get(s));
		}
	}

	public JsonHolder put(String key, String value) {
		object.addProperty(key, value);
		return this;
	}

	public JsonHolder put(String key, int value) {
		object.addProperty(key, value);
		return this;
	}

	public JsonHolder put(String key, double value) {
		object.addProperty(key, value);
		return this;
	}

	public JsonHolder put(String key, long value) {
		object.addProperty(key, value);
		return this;
	}

	public JsonHolder optJsonObject(String key, JsonObject fallBack) {
		try {
			return new JsonHolder(object.get(key).getAsJsonObject());
		} catch (Exception e) {
			return new JsonHolder(fallBack);
		}
	}

	public JsonHolder optJsonObject(String key, JsonHolder fallBack) {
		try {
			return new JsonHolder(object.get(key).getAsJsonObject());
		} catch (Exception e) {
			return fallBack;
		}
	}

	public JsonArray optJSONArray(String key, JsonArray fallback) {
		try {
			return object.get(key).getAsJsonArray();
		} catch (Exception e) {
			return fallback;
		}
	}

	public JsonArray optJSONArray(String key) {
		return optJSONArray(key, new JsonArray());
	}

	public boolean has(String key) {
		return object.has(key);
	}

	public long optLong(String key, long fallback) {
		try {
			return object.get(key).getAsLong();
		} catch (Exception e) {
			return fallback;
		}
	}

	public long optLong(String key) {
		return optLong(key, 0);
	}

	public boolean optBoolean(String key, boolean fallback) {
		try {
			return object.get(key).getAsBoolean();
		} catch (Exception e) {
			return fallback;
		}
	}

	public boolean optBoolean(String key) {
		return optBoolean(key, false);
	}

	public JsonObject optActualJsonObject(String key) {
		try {
			return object.get(key).getAsJsonObject();
		} catch (Exception e) {
			return new JsonObject();
		}
	}

	public JsonHolder optJsonObject(String key) {
		return optJsonObject(key, new JsonObject());
	}

	public int optInt(String key, int fallBack) {
		try {
			return object.get(key).getAsInt();
		} catch (Exception e) {
			return fallBack;
		}
	}

	public int optInt(String key) {
		return optInt(key, 0);
	}

	public String optString(String key, String fallBack) {
		try {
			return object.get(key).getAsString();
		} catch (Exception e) {
			return fallBack;
		}
	}

	public String optString(String key) {
		return optString(key, "");
	}

	public double optDouble(String key, double fallBack) {
		try {
			return object.get(key).getAsDouble();
		} catch (Exception e) {
			return fallBack;
		}
	}

	public List<String> getKeys() {
		List<String> tmp = new ArrayList<>();
//		tmp.addAll(object.keySet());
		for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
			tmp.add(stringJsonElementEntry.getKey());
		}
		return tmp;

	}

	public double optDouble(String key) {
		return optDouble(key, 0.0);
	}

	public JsonObject getObject() {
		return object;
	}

	public boolean isNull(String key) {
		return object.has(key) && object.get(key).isJsonNull();
	}

	public JsonHolder put(String values, JsonHolder values1) {
		put(values, values1.getObject());
		return this;
	}

	public void put(String values, JsonObject object) {
		this.object.add(values, object);
	}


	public void remove(String header) {
		object.remove(header);
	}

	public String getString(String seed) {
		return optString(seed);
	}

	public JsonArray optJSONArrayNonNull(String matches) {
		return optJSONArray(matches);
	}

	public int getInt(String type) {
		return optInt(type);
	}

	public UUID getUUID(String offender) {
		return optUUID(offender);
	}

	private UUID optUUID(String offender) {
		return UUID.fromString(optString(offender));
	}

	public String getOptString(String removedBy) {
		return optString(removedBy, null);
	}

	public long getLong(String dateIssued) {
		return optLong(dateIssued);
	}

	public long getOptLong(String removeDate, long l) {
		return optLong(removeDate, l);
	}

	public Object get(String field) {
		return object.get(field);
	}

	public void put(String key, JsonArray jsonArrayHolder) {
		object.add(key, jsonArrayHolder);
	}

	public JsonHolder put(String key, JsonElement element) {
		this.object.add(key, element);
		return this;
	}

	public JsonHolder put(String key, Object value) {
		return put(key, value.toString());
	}

	public JsonHolder put(String key, Number value) {
		object.add(key, new JsonPrimitive(value));
		return this;
	}

	public JsonHolder getJsonHolder(String entry) {
		return optJsonObject(entry);
	}

	public void putNull(String key) {
		this.object.add(key, JsonNull.INSTANCE);
	}

	public JsonElement getElement(String field) {
		return object.get(field);
	}
}