package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;

import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.responses.Sentiment;

import aylienTextAnalyzer.TextAnalyzer;
import aylienTextAnalyzer.TextAnalyzer.SentimentMode;

import coindesk.CoinDeskAPI;

import twitter4j.Status;

import twitterSearch.TwitterSearch;

public class Driver {

	public static void main(String[] args) {
		
		GregorianCalendar date = new GregorianCalendar(2018, 3, 13);
		
		// Connect to the database
		Connection database = null;
		try {
			database = DriverManager.getConnection("jdbc:mysql://localhost:3306/bitcoin_price_sentiment", 
										           "root", "root");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Insert BPI from selected date into database.
		double bpi = 0;
		try {
			bpi = CoinDeskAPI.getBPIFromDateRange(LocalDate.ofYearDay(date.get(GregorianCalendar.YEAR), 
																	  date.get(GregorianCalendar.DAY_OF_YEAR)),
												  LocalDate.ofYearDay(date.get(GregorianCalendar.YEAR), 
																	  date.get(GregorianCalendar.DAY_OF_YEAR))).get(0);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		
	    Date sqlDate = new java.sql.Date(date.getTimeInMillis());
		String sql = String.format("INSERT INTO bitcoin_price_index (price_index, date) VALUES (%s, '%s')", bpi, sqlDate);
		
		try {
			database.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Get tweets from that date.
		List<Status> tweets = TwitterSearch.searchTweetsFromDate("bitcoin", date, 1000);
		
		// Analyze tweets.
		Sentiment sentimentResult = null;
		for(Status tweet : tweets) 
		{
			try {
				sentimentResult = TextAnalyzer.analyzeSentiment(tweet.getText(), SentimentMode.TWEET);
			} catch (TextAPIException e) {
				try {
					e.printStackTrace();
					Thread.sleep(61000);
					sentimentResult = TextAnalyzer.analyzeSentiment(tweet.getText(), SentimentMode.TWEET);
				} catch (InterruptedException | TextAPIException e1) {
					e1.printStackTrace();
				}
			}
						
			sql = String.format("INSERT INTO sentiment (polarity, polarity_confidence, date) VALUES ('%s', %s, '%s')",
								sentimentResult.getPolarity(), sentimentResult.getPolarityConfidence(), sqlDate);
			
			try {
				database.createStatement().executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
