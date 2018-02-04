package com.git.onedayrex;

import com.git.onedayrex.config.DelayMqConfig;
import com.git.onedayrex.mqmessage.ExpirationMessagePostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by onedayrex
 * 2018/2/4
 *
 **/
@RestController
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage() {
        int i = 3;
        while (i>=1) {
            long expire = i * 1000;
            rabbitTemplate.convertAndSend(DelayMqConfig.DELAY_QUEUE_NAME,
                    (Object)("expire message time:" + expire),new ExpirationMessagePostProcessor(expire));
            i--;
        }
        logger.info("send mq message successful");
    }
}
