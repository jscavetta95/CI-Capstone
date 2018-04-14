package twitterSearch;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Driver {

	public static void main(String[] args) throws TwitterException {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true)
							.setOAuthConsumerKey("1qvJzIC4ZDTemWvDkQ77w7ZwX")
							.setOAuthConsumerSecret("2qYFoW68Okn6tA2VBJrkd0ksaBPOuy17TG1xkZj06D7cADgRud")
							.setOAuthAccessToken("793788156621492224-sA1OiYOwx02EEdrE0MzGwTAf5OKxtLZ")
							.setOAuthAccessTokenSecret("BZ3p5VpW8Q3I8KQZLG1xLjZMaFacFSCXX24HyuaIvZ5Dw");
		
		TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
		Twitter twitter = twitterFactory.getInstance();
		
		Query query = new Query("bitcoin");
	    QueryResult result = twitter.search(query);
	    
	    for (Status status : result.getTweets()) {
	        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
	    }
	}
}
