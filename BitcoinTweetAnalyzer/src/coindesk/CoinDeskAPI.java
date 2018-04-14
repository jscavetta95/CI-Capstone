package coindesk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class CoinDeskAPI 
{
    private static final String BASE_ENDPOINT = "https://api.coindesk.com/v1/bpi/historical/close.json";

    public static double getBPIForYesterday() throws JSONException, IOException
    {
        return getBPI(new URL(String.format("%s?for=yesterday", BASE_ENDPOINT))).get(0);
    }
    
    public static ArrayList<Double> getBPIFromDateRange(LocalDate start, LocalDate end) throws JSONException, IOException
    {
        return getBPI(new URL(String.format("%s?start=%s&end=%s", BASE_ENDPOINT, start, end)));
    }
	
	private static ArrayList<Double> getBPI(URL url) throws JSONException, IOException
	{
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.addRequestProperty("User-Agent", "Mozilla/4.76");
		connection.setRequestMethod("GET");
		connection.setReadTimeout(0);
		
		JSONObject json = new JSONObject(
				new BufferedReader(
						new InputStreamReader(
								connection.getInputStream())).readLine());
		
		return extractBPIFromJSON(json);
	}
	
	private static ArrayList<Double> extractBPIFromJSON(JSONObject json)
	{
		ArrayList<Double> bpiList = new ArrayList<Double>();
		json = json.getJSONObject("bpi");
		JSONArray jsonArray = json.toJSONArray(json.names());
		for(int i = 0; i < jsonArray.length(); i++)
		{
			bpiList.add(jsonArray.getDouble(i));
		}
		return bpiList;
	}
}
