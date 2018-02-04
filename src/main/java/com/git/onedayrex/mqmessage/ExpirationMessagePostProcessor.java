package com.git.onedayrex.mqmessage;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * created by onedayrex
 * 2018/2/4
 * 设置发送消息给队列时的消息头
 * 用于设置过期时间
 **/
public class ExpirationMessagePostProcessor implements MessagePostProcessor{

    private final Long ttl;

    public ExpirationMessagePostProcessor(Long ttl) {
        this.ttl = ttl;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties()
                .setExpiration(ttl.toString());
        return message;
    }
}
