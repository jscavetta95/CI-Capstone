import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Driver {

	private static final String conTok = "s8G1kpAiweJY7l1nQDjrGsIZl";
	private static final String conTokSec = "WMeYU9HbjmI8tBXreCcDc9NsdCTaGz9NbRmYWZrvtYEcdWwdji";
	private static final String accTok = "793788156621492224-sA1OiYOwx02EEdrE0MzGwTAf5OKxtLZ";
	private static final String accTokSec = "BZ3p5VpW8Q3I8KQZLG1xLjZMaFacFSCXX24HyuaIvZ5Dw";
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		

	    AccessToken accessToken = new AccessToken(accTok, accTokSec);
	    Twitter twitter = new TwitterFactory().getInstance(accessToken);
	    twitter.setOAuthConsumer(conTok, conTokSec);
	    //twitter.setOAuthAccessToken(accessToken);

	    try {
	        Query query = new Query("#IPL");
	        QueryResult result;
	        result = twitter.search(query);
	        List<Status> tweets = result.getTweets();
	        for (Status tweet : tweets) {
	            System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	        }
	    }
	    catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("Failed to search tweets: " + te.getMessage());
	        System.exit(-1);
	    }
	}

}
