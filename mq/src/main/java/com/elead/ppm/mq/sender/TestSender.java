package com.elead.ppm.mq.sender;

import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestSender {
	@Autowired
    private RabbitTemplate rabbitTemplate;
    public void send() {
        String context = "你说啥呢？"+ new Date();
        System.out.println("Sender : " + context);
        //this.rabbitTemplate.convertAndSend("hello", context);
        this.rabbitTemplate.convertAndSend("myExchangeDemo","routeDemo",context);
    }
}
