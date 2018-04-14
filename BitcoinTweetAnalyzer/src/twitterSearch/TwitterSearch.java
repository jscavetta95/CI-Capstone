package twitterSearch;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearch {

	public static List<Status> searchTweetsFromDate(String searchTerm, GregorianCalendar date, int numberOfTweets) {

		// Configure API connection.
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("1qvJzIC4ZDTemWvDkQ77w7ZwX")
				.setOAuthConsumerSecret("2qYFoW68Okn6tA2VBJrkd0ksaBPOuy17TG1xkZj06D7cADgRud")
				.setOAuthAccessToken("793788156621492224-sA1OiYOwx02EEdrE0MzGwTAf5OKxtLZ")
				.setOAuthAccessTokenSecret("BZ3p5VpW8Q3I8KQZLG1xLjZMaFacFSCXX24HyuaIvZ5Dw");
		Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

		// Set query.
		String queryText = String.format("%s since:%s-%s-%s", searchTerm, date.get(GregorianCalendar.YEAR),
				date.get(GregorianCalendar.MONTH), date.get(GregorianCalendar.DAY_OF_MONTH));
		Query query = new Query(queryText);

		// Get tweets.
		long lastID = Long.MAX_VALUE;
		List<Status> tweets = new ArrayList<Status>();

		boolean endOfTweets = false;

		while (tweets.size() < numberOfTweets || endOfTweets) {
			if (numberOfTweets - tweets.size() > 100) {
				query.setCount(100);
			} else {
				query.setCount(numberOfTweets - tweets.size());
			}

			try {
				QueryResult result = twitter.search(query);
				if (result.getTweets().size() == 0) {
					endOfTweets = true;
				} else {
					tweets.addAll(result.getTweets());
					for (Status t : tweets) {
						if (t.getId() < lastID) {
							lastID = t.getId();
						}
					}
					query.setMaxId(lastID - 1);
				}
			} catch (TwitterException te) {
				System.out.println("Couldn't connect: Waiting 15 minutes for rate-limit to reset.");
				try {
					Thread.sleep(901000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return tweets;
	}
}