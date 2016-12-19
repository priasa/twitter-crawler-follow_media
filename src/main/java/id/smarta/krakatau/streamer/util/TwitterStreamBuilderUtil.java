package id.smarta.krakatau.streamer.util;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamBuilderUtil {

	public static final String SPACE = " ";

	public static TwitterStream getStream() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("vihdBTAMe49rpK59hYa6UQr0s");
		cb.setOAuthConsumerSecret("RUhv75ENif4eaVCVWNazgu68w4L9J88wG9CJVP0CskiJripwgr");
		cb.setOAuthAccessToken("1228738278-CXoyKwuW6L2Z8GBuZx0jExXw57zvmEVk2vABpGD");
		cb.setOAuthAccessTokenSecret("pCcanCsm8OacaFseqqYo5OpTN4s7U1ZrAfCrXeJdF80Rg");

		return new TwitterStreamFactory(cb.build()).getInstance();
	}
}
