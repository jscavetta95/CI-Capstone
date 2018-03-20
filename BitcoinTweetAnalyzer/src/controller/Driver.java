package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONException;

import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.responses.Sentiment;

import coindesk.CoinDeskAPI;
import controller.TextAnalyzer.SentimentMode;

public class Driver
{
	public static void main(String[] args) throws TextAPIException, JSONException, IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{		
		Connection database = 
				DriverManager.getConnection("jdbc:mysql://localhost:3306/bitcoin_price_sentiment", 
											"root", "root");
		
		// Insert BPI from yesterday into database.
		double bpi = CoinDeskAPI.getBPIForYesterday();
		String sql = String.format("INSERT INTO bitcoin_price_index (price_index, date) VALUES (%s, '%s')", bpi, LocalDate.now());
		
		database.createStatement().executeUpdate(sql);
		
		// Insert sentiments from sample tweets into the database.
		ArrayList<String> tweetSamples = loadSamples();
		
		Sentiment sentimentResult;
		
		for(String sample : tweetSamples) 
		{
			sentimentResult = TextAnalyzer.analyzeSentiment(sample, SentimentMode.TWEET);
			
			sql = String.format("INSERT INTO sentiment (polarity, polarity_confidence, date) VALUES ('%s', %s, '%s')",
								sentimentResult.getPolarity(), sentimentResult.getPolarityConfidence(), LocalDate.now());
			
			database.createStatement().executeUpdate(sql);
		}
	}
	
	private static ArrayList<String> loadSamples() throws TextAPIException
	{
		ArrayList<String> samples = new ArrayList<String>();
		
		samples.add("The last week has shown beyond all doubt that "
				  + "Bitcoin is anything but a \"safe asset\". It's a highly risky speculative investment. "
				  + "Can we stop the \"digital gold\" nonsense now, please?");
		
		samples.add("Bitcoin mining is potentially even more wasteful of energy than gold mining");
		
		samples.add("Winklevii: Bitcoin could be worth 40 times what it is right now");
		
		samples.add("Cryptocurrency bull run imminent with bitcoin to hit $50,000 in 2018");
		
		samples.add("Testing");
		
		return samples;
	}
}
