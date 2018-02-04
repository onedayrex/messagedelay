package com.git.onedayrex;

import com.git.onedayrex.config.DelayMqConfig;
import com.git.onedayrex.mqmessage.ExpirationMessagePostProcessor;
import com.git.onedayrex.mqmessage.ProcessMessageListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagedelayApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;


	@Test
	public void contextLoads() throws InterruptedException {
		/**
		 * 线程阻塞
		 */
		ProcessMessageListener.latch = new CountDownLatch(3);
		int i = 1;
		while (i<=3) {
			long expire = i * 1000;
			rabbitTemplate.convertAndSend(DelayMqConfig.DELAY_QUEUE_NAME,
					(Object)("expire message time:" + expire),new ExpirationMessagePostProcessor(expire));
			i++;
		}
		ProcessMessageListener.latch.await();
	}

}
