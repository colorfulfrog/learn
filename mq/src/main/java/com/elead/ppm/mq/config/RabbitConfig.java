package com.elead.ppm.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix="spring.rabbitmq")
@Configuration
public class RabbitConfig {
	private String host;

	private Integer port;

	private String virtualHost;

	private String username;

	private String password;
	
	@Bean
	public CachingConnectionFactory myConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setVirtualHost(virtualHost);
		
		connectionFactory.setChannelCacheSize(10);
		connectionFactory.setRequestedHeartBeat(10);

		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirms(true);
		return connectionFactory;
	}

	@Bean
	public DirectExchange myExchange() {
		return new DirectExchange("myExchangeDemo", true, false);
	}

	@Bean
	public Queue myQueue() {
		return new Queue("myQueueDemo", true);
	}

	@Bean
	public Binding myExchangeBinding(@Qualifier("myExchange") DirectExchange directExchange,
			@Qualifier("myQueue") Queue queue) {
		return BindingBuilder.bind(queue).to(directExchange).with("routeDemo");
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate r = new RabbitTemplate(myConnectionFactory());
		r.setExchange("myExchangeDemo");
		r.setRoutingKey("routeDemo");
		r.setMessageConverter(jsonMessageConverter());
		return r;
	}
	
	/**
     * 接受消息的监听，这个监听会接受消息队列1的消息
     * 针对消费者配置  
     * @return
     */
    /*@Bean  
    public SimpleMessageListenerContainer messageContainer() {  
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(myConnectionFactory());  
        container.setQueues(myQueue());  
        container.setExposeListenerChannel(true);  
        container.setMaxConcurrentConsumers(10);  
        container.setConcurrentConsumers(5);  
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认  
        container.setMessageListener(new ChannelAwareMessageListener() {

            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                byte[] body = message.getBody();  
                System.out.println("收到消息 : " + new String(body));  
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费  
            }  

        });  
        return container;  
    }*/ 
    
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(@Qualifier("myConnectionFactory")ConnectionFactory connectionFactory,@Qualifier("jsonMessageConverter")MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(messageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
