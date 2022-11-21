package com.lijx.rocketmq.controller;


import com.lijx.rocketmq.producer.Producer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @Author: lijx
 * @Description: 这里是为了测试
 * @Date: 2022/11/21
 */
@RestController
@RequestMapping("/hello")
public class TestController
{
    @Autowired
    private Producer producer;

    @Value("${rocketmq.producer.topic}")
    private String msgTopic;

    @Value("${rocketmq.producer.tag}")
    private String msgTag;
    @RequestMapping(value = "/syncPush", method = RequestMethod.GET)
    public String syncPushMsg(String msg)
    {
        try
        {
            return producer.syncSend( msgTopic, msgTag, msg);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (RemotingException e)
        {
            e.printStackTrace();
        } catch (MQClientException e)
        {
            e.printStackTrace();
        } catch (MQBrokerException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "SYNC_ERROR";
    }

    @RequestMapping(value = "/asyncPush", method = RequestMethod.GET)
    public String asyncPushMsg(String msg)
    {
        try
        {
            return producer.asyncSend( msgTopic, msgTag, msg);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (RemotingException e)
        {
            e.printStackTrace();
        } catch (MQClientException e)
        {
            e.printStackTrace();
        } catch (MQBrokerException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return "ASYNC_ERROR";
    }
}

