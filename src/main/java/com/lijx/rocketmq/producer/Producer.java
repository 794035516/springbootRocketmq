package com.lijx.rocketmq.producer;


import org.apache.commons.lang3.time.StopWatch;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * @Author: lijx
 * @Description:生产者
 * @Date: 2022/11/21
 */

@Component
public class Producer
{

    /**
     * 生产者的组名
     */
    @Value("${rocketmq.producer.pullProducer}")
    private String producerGroup;

    private DefaultMQProducer producer;
    /**
     * NameServer 地址
     */
    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @PostConstruct
    public void defaultMQProducer()
    {

        //生产者的组名
        producer = new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        producer.setVipChannelEnabled(false);
        try
        {
            producer.start();
            System.out.println("-------->:producer启动了");
        } catch (MQClientException e)
        {
            e.printStackTrace();
        }
    }

    //同步发送方式
    public String syncSend(String topic, String tags, String body)
            throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException
    {
        Message message = new Message(topic, tags, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
        StopWatch stop = new StopWatch();
        stop.start();
        //同步方式
        SendResult result = producer.send(message);
        System.out.println("同步发送响应: MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
        stop.stop();
        return "{\"同步MsgId\":\"" + result.getMsgId() + "\"}";
    }

    //异步发送方式
    public String asyncSend(String topic, String tags, String body)
            throws InterruptedException, RemotingException, MQClientException, MQBrokerException, UnsupportedEncodingException
    {
        Message message = new Message(topic, tags, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
        //异步方式
        System.out.println("异步发送一次消息-----");
        producer.send(message, new SendCallback(){
            public void onSuccess(SendResult sendResult){
                System.out.println("异步发送结果: " + sendResult + ", msgid: " + sendResult.getMsgId() +", status: " + sendResult.getSendStatus() );
            }

            public void onException(Throwable e){
                System.out.println("异步发送异常: ");
                e.printStackTrace();
            }
        });

        return "异步返回";
    }


}

