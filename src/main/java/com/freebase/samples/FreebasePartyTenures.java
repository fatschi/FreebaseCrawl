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

public class FreebasePartyTenures extends FreebaseCrawler{
  public static void main(String[] args) {    
    try {
        HttpTransport httpTransport = new NetHttpTransport();
        HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
        JSONParser parser = new JSONParser();
        //MQL query
        /*
        [{
			  "id": null,
			  "type": "/government/political_party_tenure",
			  "politician": {
			  },
			  "party": {
			  },
			  "from": null,
			  "to": null
		}]
         */
        String queryTemplate = "[{  \"id\": null,  \"type\": \"/government/political_party_tenure\",  \"politician\": {  },  \"party\": {  },  \"from\": null,  \"to\": null, \"limit\":100\n}]";
        String currentCursor = "";
        GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/mqlread");
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