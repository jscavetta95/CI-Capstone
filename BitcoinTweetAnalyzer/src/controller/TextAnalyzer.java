package controller;

import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.parameters.SentimentParams;
import com.aylien.textapi.responses.Sentiment;

public class TextAnalyzer 
{
	private final static String APP_ID = "c5557425";
	private final static String KEY = "eca34b10deef5fdcc8374ed7b82900ad";
	
	public static enum SentimentMode { TWEET, DOCUMENT }
	
	public static Sentiment analyzeSentiment(String textToAnalyze, SentimentMode mode) throws TextAPIException 
	{
	    TextAPIClient client = new TextAPIClient(APP_ID, KEY);
	    SentimentParams.Builder builder = SentimentParams.newBuilder();
	    builder.setText(textToAnalyze);
	    builder.setMode(mode.toString().toLowerCase());
	    return client.sentiment(builder.build());
	}
}