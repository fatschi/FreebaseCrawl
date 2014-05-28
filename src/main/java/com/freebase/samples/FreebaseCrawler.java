package com.freebase.samples;

import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cedarsoftware.util.io.JsonObject;

public abstract class FreebaseCrawler {
	protected static Object cleanObject(Object result) {
		if (result instanceof JSONObject) {
			JsonObject<String, Object> copy = new JsonObject<String, Object>();
			copy.putAll((JSONObject) result);
			JsonObject<String, Object> cleaned = new JsonObject<String, Object>();
			for (Entry<String, Object> pair : copy.entrySet()) {
				String newKey = pair.getKey().split("/")[pair.getKey().split(
						"/").length - 1].replace("/", "");
				cleaned.put(newKey, cleanObject(pair.getValue()));
			}
			return new JSONObject(cleaned);
		} else if (result instanceof JSONArray) {
			JSONArray copy = new JSONArray();
			for (Object value : ((JSONArray) result)) {
				copy.add(cleanObject(value));
			}
			return copy;
		} else {
			return result;
		}
	}
}
