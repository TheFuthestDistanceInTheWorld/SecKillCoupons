package com.demo.consumer;

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
	
	
	
}
