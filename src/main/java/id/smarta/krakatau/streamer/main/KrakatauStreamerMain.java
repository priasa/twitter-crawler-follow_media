package id.smarta.krakatau.streamer.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import id.smarta.krakatau.streamer.twitter.TwitterService;

/**
 * 
 * @author ardi priasa
 *
 */
public class KrakatauStreamerMain {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"context/krakatau-streamer.xml");

		TwitterService service = (TwitterService) appContext.getBean("twitterService");
		service.doStream();
	
	}

}
