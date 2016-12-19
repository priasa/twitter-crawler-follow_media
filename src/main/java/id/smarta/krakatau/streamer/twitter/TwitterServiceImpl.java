package id.smarta.krakatau.streamer.twitter;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * @author ardi priasa
 *
 */
public class TwitterServiceImpl implements TwitterService {

	TwitterReader twitterReader; 
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Override
	public void doStream() {
		taskExecutor.execute(new Runnable() {
			public void run() {
				twitterReader.readTwitterFeed();
			}
		});
	}

	public TwitterReader getTwitterReader() {
		return twitterReader;
	}

	public void setTwitterReader(TwitterReader twitterReader) {
		this.twitterReader = twitterReader;
	}

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
