package com.demo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.vo.SuccessfulPerson;

@Component
public class TaskProvider {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void sendSuccessfulPerson(SuccessfulPerson successfulPerson){
		//将消息发送至队列     使用fanout广播模式
		
		/*
		 * 此处使用${userid}-${key}的形式进行 幂等性的消除
		 * 此处不采用随机的key  因为那会使向消息队列中添加该信息的操作可能出现两次添加的情况
		 * */
		CorrelationData correlationData = new CorrelationData(successfulPerson.getUserid()+"-"+successfulPerson.getGrabcouponid());
		
		rabbitTemplate.convertAndSend(RabbitMQConfig.Task_Exchange, RabbitMQConfig.Email_Task_Queue_Routing_Key, successfulPerson,correlationData);
	
	}
	
}
