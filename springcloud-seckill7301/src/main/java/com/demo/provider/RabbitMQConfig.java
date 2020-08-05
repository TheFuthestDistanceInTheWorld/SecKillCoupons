package com.demo.provider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	//交换机名称
	public static final String Task_Exchange = "TaskExchange";
	public static final String Email_Dead_Task_Exchange = "EmailDeadTaskExchange";
	public static final String Add_Coupon_Dead_Task_Exchange = "AddCouponDeadTaskExchange";
	
	//队列名称
	public static final String Email_Task_Queue = "EmailTaskQueue";
	public static final String Add_Coupon_Task_Queue = "AddCouponTaskQueue";
	public static final String Email_Dead_Task_Queue = "EmailDeadTaskQueue";
	public static final String Add_Coupon_Dead_Task_Queue = "AddCouponDeadTaskQueue";
	
	
	//路由名称
	public static final String Routing_Key = ".RoutingKey";
	public static final String Email_Task_Queue_Routing_Key = Email_Task_Queue + Routing_Key;
	public static final String Add_Coupon_Task_Queue_Routing_Key = Add_Coupon_Task_Queue + Routing_Key;
	public static final String Email_Dead_Task_Queue_Routing_Key = Email_Dead_Task_Queue+ Routing_Key;
	public static final String Add_Coupon_Dead_Task_Queue_Routing_Key = Add_Coupon_Dead_Task_Queue+ Routing_Key;
	
	
	@Bean("taskExchange")
	public Exchange taskExchange(){
		return ExchangeBuilder.topicExchange(Task_Exchange).build();
	}
	
	@Bean("emailDeadTaskExchange")
	public Exchange emailDeadTaskExchange(){
		return ExchangeBuilder.topicExchange(Email_Dead_Task_Exchange).build();
	}
	
	@Bean("addCouponDeadTaskExchange")
	public Exchange addCouponDeadTaskExchange(){
		return ExchangeBuilder.topicExchange(Add_Coupon_Dead_Task_Exchange).build();
	}
	
	@Bean("emailTaskQueue")
	public Queue emailTaskQueue(){
		Map<String,Object> args = new HashMap<String,Object>(2);
		//重试失败后将消息路由至对应的交换机  且用对应的路由key进行路由
		args.put("x-dead-letter-exchange", Email_Dead_Task_Exchange);
		args.put("x-dead-letter-routing-key", Email_Dead_Task_Queue_Routing_Key);
		return QueueBuilder.durable(Email_Task_Queue).withArguments(args).build();
	}
	
	@Bean("addCouponTaskQueue")
	public Queue addCouponTaskQueue(){
		Map<String,Object> args = new HashMap<String,Object>(2);
		args.put("x-dead-letter-exchange",Add_Coupon_Dead_Task_Exchange);
		args.put("x-dead-letter-routing-key", Add_Coupon_Dead_Task_Queue_Routing_Key);
		return QueueBuilder.durable(Add_Coupon_Task_Queue).withArguments(args).build();
	}
	@Bean("emailDeadTaskQueue")
	public Queue emailDeadTaskQueue(){
		return QueueBuilder.durable(Email_Dead_Task_Queue).build();
	}
	@Bean("addCouponDeadTaskQueue")
	public Queue addCouponDeadTaskQueue(){
		return QueueBuilder.durable(Add_Coupon_Dead_Task_Queue).build();
	}
	
	@Bean("emailTaskQueueBinding")
	public Binding emailTaskQueueBinding(){
		return new Binding(Email_Task_Queue,Binding.DestinationType.QUEUE,Task_Exchange,"*"+Routing_Key,null);
	}
	
	@Bean("addCouponQueueBinding")
	public Binding addCouponQueueBinding(){
		return new Binding(Add_Coupon_Task_Queue,Binding.DestinationType.QUEUE,Task_Exchange,"*"+Routing_Key,null);
	}
	
	@Bean("emailDeadTaskQueueBinding")
	public Binding emailDeadTaskQueueBinding(){
		return new Binding(Email_Dead_Task_Queue,Binding.DestinationType.QUEUE,Email_Dead_Task_Exchange,Email_Dead_Task_Queue_Routing_Key,null);
	}
	
	@Bean("addCouponDeadQueueBinding")
	public Binding addCouponDeadQueueBinding(){
		return new Binding(Add_Coupon_Dead_Task_Queue,Binding.DestinationType.QUEUE,Add_Coupon_Dead_Task_Exchange,Add_Coupon_Dead_Task_Queue_Routing_Key,null);
	}
}
