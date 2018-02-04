package com.git.onedayrex.mqmessage;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * created by onedayrex
 * 2018/2/4
 **/
@Component
public class ProcessMessageListener implements ChannelAwareMessageListener{

    private static final Logger loggerr = LoggerFactory.getLogger(ProcessMessageListener.class);

    public static CountDownLatch latch;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String result = new String(message.getBody());
        loggerr.info("receive mq message==>{}", result);
    }
}
