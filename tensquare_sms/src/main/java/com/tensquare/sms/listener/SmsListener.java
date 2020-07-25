package com.tensquare.sms.listener;

import com.tensquare.sms.util.SendUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SendUtil sendUtil;

    @RabbitHandler
    public void executeSms(Map<String,String> map){
        System.out.println("手机号::"+map.get("mobile"));
        System.out.println("验证码::"+map.get("checkcode"));
        sendUtil.SendUtil(map.get("mobile"),map.get("checkcode"));
    }
}
