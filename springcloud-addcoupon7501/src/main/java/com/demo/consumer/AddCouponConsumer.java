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
	
	//�˴�ȷ�������ݵ���
	@RabbitHandler
	public void addCoupon(@Payload SuccessfulPerson successfulPerson,Channel channel, @Headers Map<String,Object> headers){
		
		String key = "addCoupon-"+successfulPerson.getGrabcouponid() + "-" +successfulPerson.getUserid();
		
		try{
			//���redis���Ƿ���ڸò����ı�־λ  δ��ɵĻ�����Ӹò����ı�־
			if(!redisUtil.addElementIfNotExist(key))
			{
				channel.basicAck((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false);
				return;
			}
			//ע��  �˴�ѡ�����ֶ��ύ    ��Ϊ�����ѡ���ֶ��ύ  �޷�ȷ���������ݵ���  ɾ����־λ�Ĳ����޷�����
			Coupon coupon = new Coupon();
			coupon.setId(snowFlake.getId());
			coupon.setUser_id(successfulPerson.getUserid());
			coupon.setMin(successfulPerson.getMin());
			coupon.setDiscount(successfulPerson.getDiscount());
			coupon.setBegin_date(successfulPerson.getBegin_date());
			coupon.setEnd_date(successfulPerson.getEnd_date());
			couponService.AddACoupon(coupon);
			
			//�ֶ�ȷ��
			channel.basicAck((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false);
			
		}
		catch(Exception e){
			
			//��redis�е�����ɾ��
			redisUtil.delete(key);
			log.error(e.getMessage());
			//�����쳣    ����  ��ӡ��־   ����nack
			try {
				channel.basicNack((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false, false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		
	}
	
}
