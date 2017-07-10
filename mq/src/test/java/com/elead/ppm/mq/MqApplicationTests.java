package com.elead.ppm.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.elead.ppm.mq.sender.TestSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private TestSender testSender;
	
	@Test
	public void contextLoads() {
		rabbitTemplate.convertAndSend("ss");
	}
	
	@Test
	public void sendTest(){
		testSender.send();
	}
}
