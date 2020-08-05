package com.demo.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.util.ObjectAndByte;

@Configuration
public class RabbitTemplateConfiguration {
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Autowired
	private ObjectAndByte objectAndByte;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){
		//发送到了队列但因为某些原因导致队列没有确认   
		public void confirm(CorrelationData correlationData, boolean ack, String cause){
			
            if(ack){
            	log.info("ack: "+ ack + "  correlationData: " + correlationData +"  cause: "+ cause);
            }else{
            	log.warn("ack: "+ ack + "  correlationData: " + correlationData +"  cause: "+ cause);
            }
            
		}
	};
	
	final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback(){
		//根本找不到对应的队列
		@Override
		public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
				String routingKey) {
			
			log.warn("returnCallback: exchange: " + exchange + "  routingKey: "+ routingKey + "  replyCode: " + replyCode + "  replyText: " + replyText + "  message: " + objectAndByte.toObject(message.getBody()) + "  messageHeader:" + message.getMessageProperties());
			
		}
	};
	
	@Bean
	public RabbitTemplate  configRabbitTemplate(){
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMandatory(true);
		template.setConfirmCallback(confirmCallback);
		template.setReturnCallback(returnCallback);
		return template;
	}
	
}
