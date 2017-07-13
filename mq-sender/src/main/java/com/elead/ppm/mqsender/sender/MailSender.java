package com.elead.ppm.mqsender.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.elead.platform.common.api.message.SimpleTextMailDto;

@Component
public class MailSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);
	private static final String MAIL_ROUTE_KEY = "mail.route.key";
	private static final String MAIL_QUEUE = "mailQueue";
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Bean
	public Queue mailQueue() {
		return new Queue(MAIL_QUEUE, true);
	}

	@Bean
	public Binding myExchangeBinding(@Qualifier("directExchange") DirectExchange directExchange,
			@Qualifier(MAIL_QUEUE) Queue queue) {
		return BindingBuilder.bind(queue).to(directExchange).with(MAIL_ROUTE_KEY);
	}
	
    public void send(SimpleTextMailDto mail) {
        LOGGER.info("---------Mail Sender:-----------"+mail);
        this.rabbitTemplate.convertAndSend("directExchange",MAIL_ROUTE_KEY,mail);
    }
}
