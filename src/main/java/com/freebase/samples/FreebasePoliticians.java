package com.freebase.samples;

import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cedarsoftware.util.io.JsonWriter;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class FreebasePoliticians extends FreebaseCrawler{
	public static void main(String[] args) {
		try {
			HttpTransport httpTransport = new NetHttpTransport();
			HttpRequestFactory requestFactory = httpTransport
					.createRequestFactory();
			JSONParser parser = new JSONParser();
			// MQL query
			/*
			 * [{ "id": null, "name": null, "type": "/government/politician",
			 * "/people/person/date_of_birth": null,
			 * "/people/deceased_person/date_of_death": null,
			 * "government_positions_held": [{
			 * "/government/government_position_held/from": null,
			 * "/government/government_position_held/to": null,
			 * "/government/government_position_held/office_position_or_title":
			 * [{ "name|=": [ "United States Representative",
			 * "United States Senator" ], "name": null }],
			 * "/government/government_position_held/legislative_sessions": [{}]
			 * }] }]
			 */
			String queryTemplate = "[{\n  \"id\": null,\n  \"name\": null,\n  \"type\": \"/government/politician\",\n  \"/people/person/date_of_birth\": null,\n  \"/people/deceased_person/date_of_death\": null,\n  \"government_positions_held\": [{\n    \"/government/government_position_held/from\": null,\n    \"/government/government_position_held/to\": null,\n    \"/government/government_position_held/office_position_or_title\": [{\n \"name|=\": [\"United States Representative\", \"United States Senator\"], \"name\": null\n    }],\n    \"/government/government_position_held/legislative_sessions\": [{}]\n  }], \"limit\":100\n}]";
			String currentCursor = "";
			GenericUrl url = new GenericUrl(
					"https://www.googleapis.com/freebase/v1/mqlread");
			JSONArray finalResult = new JSONArray();
			while (true) {
				url.put("query", queryTemplate);
				url.put("cursor", currentCursor);
				url.put("key", "AIzaSyB2rARUGp8xWNQ5p_U4Yh1Xxs_e-dR9Cws");
				System.out.println(url.toString());
				HttpRequest request = requestFactory.buildGetRequest(url);
				HttpResponse httpResponse = request.execute();
				JSONObject response = (JSONObject) parser.parse(httpResponse
						.parseAsString());

				JSONArray results = (JSONArray) response.get("result");
				System.out.println(currentCursor);
				for (Object result : results) {
					finalResult.add(cleanObject(result));
					System.out.println(cleanObject(result));
				}

				if (response.get("cursor").equals(Boolean.FALSE)) {
					break;
				} else {
					currentCursor = "=" + (String) response.get("cursor");
				}
			}
			PrintWriter out = new PrintWriter("freebasePoliticians.json");
			out.print(JsonWriter.formatJson(finalResult.toJSONString()));
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}