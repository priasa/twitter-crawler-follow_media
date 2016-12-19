package id.smarta.krakatau.streamer.twitter;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import id.smarta.krakatau.streamer.util.TwitterStreamBuilderUtil;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

/**
 * 
 * @author ardi priasa
 *
 */
public class TwitterReader {

	static final Logger LOGGER = LoggerFactory.getLogger(TwitterReader.class);

	private JmsTemplate twitterJmsTemplate;
	private TwitterStream stream;
	private ThreadPoolTaskExecutor taskExecutor;

	public String readTwitterFeed() {
		StatusListener listener = new StatusListener() {
			public void onException(Exception e) {
				LOGGER.error("Exception occured:" + e.getMessage());
				e.printStackTrace();
			}

			public void onTrackLimitationNotice(int n) {
				LOGGER.warn("Track limitation notice for " + n);
			}

			public void onStatus(Status status) {
				final String tweetId = ""+status.getId();
				final String tweetStatus = generateTweetStatus(status, false);	
				LOGGER.info(tweetStatus);
				
				//send status to queue
				taskExecutor.execute(new Runnable() {
					public void run() {
						twitterJmsTemplate.convertAndSend("twitterFollowMedia1", tweetStatus, new MessagePostProcessor() {
							public Message postProcessMessage(Message message) throws JMSException {
								message.setJMSCorrelationID(tweetId);
								message.setJMSTimestamp(new Date().getTime());
								return message;
							}
						});
					}
				});
				
				if (status.getRetweetedStatus() != null) {
					final String retweetStatus = generateTweetStatus(status.getRetweetedStatus(), true);
					
					//send retweetstatus to queue
					taskExecutor.execute(new Runnable() {
						public void run() {
							twitterJmsTemplate.convertAndSend("twitterFollowMedia2", retweetStatus, new MessagePostProcessor() {
								public Message postProcessMessage(Message message) throws JMSException {
									message.setJMSCorrelationID(tweetId);
									message.setJMSTimestamp(new Date().getTime());
									return message;
								}
							});
						}
					});
				}
			}

			public void onStallWarning(StallWarning arg0) {
				LOGGER.warn("Stall warning");
			}

			public void onScrubGeo(long arg0, long arg1) {
				LOGGER.info("Scrub geo with:" + arg0 + ":" + arg1);
			}

			public void onDeletionNotice(StatusDeletionNotice arg0) {
				LOGGER.info("Status deletion notice");
			}
		};
		
		if (stream != null) {
			LOGGER.info("###STREAM NOT NULL");
			stream.shutdown();
		} 
		
		stream = TwitterStreamBuilderUtil.getStream();
		stream.addListener(listener);
		LOGGER.info("###STREAM AVAILABLE");
				
		int i = 0;
		StringBuilder builder = new StringBuilder();
		long[] follows = new long[TwitterUserID.values().length];
		for (TwitterUserID twitterUserID: TwitterUserID.values()) {
			long userId = twitterUserID.getUserId().longValue();
			builder.append(userId).append(TwitterStreamBuilderUtil.SPACE);
			follows[i] = userId;
			i++;
		}
		LOGGER.info("Follows[" + builder.toString() + "]");
		
		FilterQuery qry = new FilterQuery();
		qry.follow(follows);
		stream.filter(qry);
		
		return "READ_TWITTER_FEED";
	}

	private String generateTweetStatus(Status status, boolean isRetweet) {
		StringBuilder tweetStatus = new StringBuilder();
		tweetStatus.append(status.getId()).append(",");
		
		String text = status.getText();
		text = text.replaceAll("(\\r|\\n|\\r\\n)+", " ");
		text = text.replace(",", " ");				
		tweetStatus.append(text).append(",");
		
		tweetStatus.append(status.getSource()).append(",");
		tweetStatus.append(status.isFavorited()).append(",");
		tweetStatus.append(status.getFavoriteCount()).append(",");
		tweetStatus.append(status.isRetweet()).append(",");
		tweetStatus.append(status.getRetweetCount()).append(",");
		tweetStatus.append(status.isRetweeted()).append(",");
		tweetStatus.append(status.getUser().getId()).append(",");
		tweetStatus.append(status.getUser().getScreenName());
		
		if (!isRetweet) {
			String originalId = "null";
			if (status.getRetweetedStatus() != null) {
				Status retweetStatus = status.getRetweetedStatus();
				originalId = ""+retweetStatus.getId();
			}
			tweetStatus.append(",").append(originalId);
		}
		
		return tweetStatus.toString();	
	}
	
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public JmsTemplate getTwitterJmsTemplate() {
		return twitterJmsTemplate;
	}

	public void setTwitterJmsTemplate(JmsTemplate twitterJmsTemplate) {
		this.twitterJmsTemplate = twitterJmsTemplate;
	}

	public TwitterStream getStream() {
		return stream;
	}

	public void setStream(TwitterStream stream) {
		this.stream = stream;
	}

	
}
