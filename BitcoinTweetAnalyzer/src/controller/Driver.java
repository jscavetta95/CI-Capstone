package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONException;

import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.responses.Sentiment;

import coindesk.CoinDeskAPI;
import controller.TextAnalyzer.SentimentMode;

public class Driver
{
	public static void main(String[] args) throws TextAPIException, JSONException, IOException
	{
		// Testing text analyzer.
		testTextAnalysis();
		
		// Testing BPI retriever
		double bpi = CoinDeskAPI.getBPIForYesterday();
		System.out.println("BPI for yesterday: " + bpi);
		System.out.println();

		ArrayList<Double> bpiList = CoinDeskAPI.getBPIFromDateRange(LocalDate.of(2017, 12, 15), LocalDate.of(2017, 12, 20));
		
		System.out.println("BPI from 12/15/17 to 12/20/17");
		int day = 15;
		for(double bpiValue : bpiList) 
		{
			System.out.println(day + "th: " + bpiValue);
			day++;
		}
	}
	
	private static void testTextAnalysis() throws TextAPIException
	{
		Sentiment sentimentResult;

		String sampleTweet = "The last week has shown beyond all doubt that "
				+ "Bitcoin is anything but a \"safe asset\". It's a highly risky speculative investment. "
				+ "Can we stop the \"digital gold\" nonsense now, please?";
		sentimentResult = TextAnalyzer.analyzeSentiment(sampleTweet, SentimentMode.TWEET);
		System.out.println(sampleTweet + "\n\t " + sentimentResult.getPolarity() + ", "
				+ sentimentResult.getPolarityConfidence() + "\n");

		sampleTweet = "Bitcoin mining is potentially even more wasteful of energy than gold mining";
		sentimentResult = TextAnalyzer.analyzeSentiment(sampleTweet, SentimentMode.TWEET);
		System.out.println(sampleTweet + "\n\t " + sentimentResult.getPolarity() + ", "
				+ sentimentResult.getPolarityConfidence() + "\n");

		sampleTweet = "Winklevii: Bitcoin could be worth 40 times what it is right now";
		sentimentResult = TextAnalyzer.analyzeSentiment(sampleTweet, SentimentMode.TWEET);
		System.out.println(sampleTweet + "\n\t " + sentimentResult.getPolarity() + ", "
				+ sentimentResult.getPolarityConfidence() + "\n");

		sampleTweet = "Arizona may allow people to pay their taxes with bitcoin http://nyp.st/2E9TvDM  via @nypost";
		sentimentResult = TextAnalyzer.analyzeSentiment(sampleTweet, SentimentMode.TWEET);
		System.out.println(sampleTweet + "\n\t " + sentimentResult.getPolarity() + ", "
				+ sentimentResult.getPolarityConfidence() + "\n");

		sampleTweet = "Cryptocurrency bull run imminent with bitcoin to hit $50,000 in 2018";
		sentimentResult = TextAnalyzer.analyzeSentiment(sampleTweet, SentimentMode.TWEET);
		System.out.println(sampleTweet + "\n\t " + sentimentResult.getPolarity() + ", "
				+ sentimentResult.getPolarityConfidence() + "\n");
	}
}
