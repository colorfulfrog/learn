package com.elead.ppm.mqrecevier.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.elead.platform.common.api.message.SimpleTextMailDto;
import com.elead.ppm.mqrecevier.service.MailService;

@Component
@RabbitListener(queues = "mailQueue",containerFactory="rabbitListenerContainerFactory")
public class MailReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);
	private static final String MAIL_ROUTE_KEY = "mail.route.key";
	private static final String MAIL_QUEUE = "mailQueue";
	
	@Autowired
	private MailService mailService;
	
	@Bean
	public Queue mailQueue() {
		return new Queue(MAIL_QUEUE, true);
	}

	@Bean
	public Binding myExchangeBinding(@Qualifier("directExchange") DirectExchange directExchange,
			@Qualifier(MAIL_QUEUE) Queue queue) {
		return BindingBuilder.bind(queue).to(directExchange).with(MAIL_ROUTE_KEY);
	}
	
	@RabbitHandler
    public void process(SimpleTextMailDto mail) throws Exception {
		LOGGER.info("---------Mail Receiver:-----------"+mail);
		String type = mail.getType();
		if(null != type && "Text".equalsIgnoreCase(type)){
			mailService.sendSimpleTextMail(mail);
		}else if(null != type && "Html".equalsIgnoreCase(type)){
			mailService.sendMimeMail(mail);
		}
    }
}
