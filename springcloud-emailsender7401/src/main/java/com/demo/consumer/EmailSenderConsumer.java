package com.demo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.util.SendEmail;
import com.demo.vo.SuccessfulPerson;

import com.demo.conf.RabbitMQConfig;
@Component
@RabbitListener(queues = RabbitMQConfig.Email_Task_Queue)
public class EmailSenderConsumer {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SendEmail sendEmail;
	
	@RabbitHandler
	public void sendEmail(SuccessfulPerson successfulPerson){
		sendEmail.sendEmail(successfulPerson);
	}
	
}
