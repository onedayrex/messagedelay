package com.git.onedayrex.config;

import com.git.onedayrex.mqmessage.ProcessMessageListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by onedayrex
 * 2018/2/4
 **/
@Configuration
public class DelayMqConfig {

    /**
     * 延迟消息实现思路：
     * 1、创建延迟队列，所有延迟消息发送到此队列上，设置过期时间ttl
     * 2、创建消息处理队列、延迟队列中过期的消息通过dlx路由发送到消息处理队列中消费
     */

    /**
     * 延迟队列名
     */
    public static final String DELAY_QUEUE_NAME = "delay_queue_name";

    public static final String DEAD_LETTER_EXCHANGE_NAME = "dead_letter_exchange_name";

    public static final String DEAD_LETTER_ROUTING_KEY = "dead_letter_routing_key";

    public static final String DLX_EXCHANGE_NAME = "dlx_exchange_name";

    public static final String PROCESS_QUEUE_NAME = "process_queue_name";

    /**
     * 创建延迟队列
     * 消息失效后通过DEADLETTEREXCHANGE发送给
     * 绑定的消息处理队列
     * @return
     */
    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                //可设置整体队列延迟，如果消息也有单独给延迟，取最小的
//                .withArgument("x-message-ttl",3000)
                .build();
    }

    /**
     * 创建DLX路由
     * 延迟队列失效后通过DLX发送到
     * 消息处理队列
     * @return
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME);
    }

    /**
     * 创建消息处理队列
     * @return
     */
    @Bean
    public Queue processQueue() {
        return QueueBuilder.durable(PROCESS_QUEUE_NAME)
                .build();
    }

    /**
     * 消息队列绑定到DLX路由上
     * 接收延迟队列失效的消息
     * @return
     */
    @Bean
    public Binding bindingDlxExchange() {
        return BindingBuilder.bind(processQueue())
                .to(dlxExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory, ProcessMessageListener processMessageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PROCESS_QUEUE_NAME);
        container.setMessageListener(new MessageListenerAdapter(processMessageListener));
        return container;
    }


}
