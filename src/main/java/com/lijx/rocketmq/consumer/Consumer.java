package com.lijx.rocketmq.consumer;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: lijx
 * @Description:消费者
 * @Date: 2022/11/2
 */

@Component
public class Consumer implements CommandLineRunner
{

    /**
     * z0PMGBK
     * 消费者
     */
    @Value("${rocketmq.consumer.pushConsumer}")
    private String pushConsumer;

    /**
     * NameServer 地址
     */
    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.producer.topic}")
    private String msgTopic;

    @Value("${rocketmq.producer.tag}")
    private String msgTag;

    /**
     * 初始化RocketMq的监听信息，渠道信息
     */
    public void messageListener()
    {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SpringBootRocketMqGroup");
        consumer.setNamesrvAddr(namesrvAddr);
        try
        {

            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumer.subscribe(msgTopic, msgTag);

            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费一条
            consumer.setConsumeMessageBatchMaxSize(1);

            //在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) ->
            {
                // 会把不同的消息分别放置到不同的队列中
                for (Message msg : msgs)
                {
                    System.out.println("接收到了消息：" + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            consumer.start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception
    {
        this.messageListener();
    }
}
