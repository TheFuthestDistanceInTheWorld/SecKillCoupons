package com.demo.consumer;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.demo.entity.Coupon;
import com.demo.feginservice.SnowFlake;
import com.demo.service.CouponService;
import com.demo.util.RedisUtil;
import com.demo.vo.SuccessfulPerson;
import com.rabbitmq.client.Channel;

@RabbitListener(queues = RabbitMQConfig.Add_Coupon_Task_Queue)
@Component
public class AddCouponConsumer {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private SnowFlake snowFlake;
	
	@Autowired
	private RedisUtil redisUtil;
	
	//此处确保操作幂等性
	@RabbitHandler
	public void addCoupon(@Payload SuccessfulPerson successfulPerson,Channel channel, @Headers Map<String,Object> headers){
		
		String key = "addCoupon-"+successfulPerson.getGrabcouponid() + "-" +successfulPerson.getUserid();
		
		try{
			//检查redis中是否存在该操作的标志位  未完成的话就添加该操作的标志
			if(!redisUtil.addElementIfNotExist(key))
			{
				channel.basicAck((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false);
				return;
			}
			//注意  此处选择了手动提交    因为如果不选择手动提交  无法确保操作的幂等性  删除标志位的操作无法进行
			Coupon coupon = new Coupon();
			coupon.setId(snowFlake.getId());
			coupon.setUser_id(successfulPerson.getUserid());
			coupon.setMin(successfulPerson.getMin());
			coupon.setDiscount(successfulPerson.getDiscount());
			coupon.setBegin_date(successfulPerson.getBegin_date());
			coupon.setEnd_date(successfulPerson.getEnd_date());
			couponService.AddACoupon(coupon);
			
			//手动确认
			channel.basicAck((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false);
			
		}
		catch(Exception e){
			
			//将redis中的数据删除
			redisUtil.delete(key);
			log.error(e.getMessage());
			//出现异常    捕获  打印日志   并且nack
			try {
				channel.basicNack((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false, false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		
	}
	
}
