package com.tensquare.test;

import com.tensquare.rabbit.RabbitApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class ProductTest {
    //生产者
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //直接模式
    @Test
    public void testSend1(){
        rabbitTemplate.convertAndSend("itcast","直接模式测试");
    }


    //分列模式
    @Test
    public void testSend2(){
        rabbitTemplate.convertAndSend("bw","","分列模式测试");
    }


    //主题模式

    @Test
    public void testSend3(){
        rabbitTemplate.convertAndSend("topic84","good.log","主题模式测试");
    }

}
