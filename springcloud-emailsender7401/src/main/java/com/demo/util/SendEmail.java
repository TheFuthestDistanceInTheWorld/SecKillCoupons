package com.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.demo.vo.SuccessfulPerson;

import io.netty.buffer.PooledByteBufAllocator;

@Component
public class SendEmail {
	
	@Autowired
	private JavaMailSender javaMailSender;
	PooledByteBufAllocator pbba = new PooledByteBufAllocator();
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	public boolean sendEmail(SuccessfulPerson successfulPerson) {
		
		SimpleMailMessage simpleMailMessage = null;
		try{
			simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(successfulPerson.getEmail());
			simpleMailMessage.setFrom(fromEmail);
			
			simpleMailMessage.setSubject("来自得物的邮件");
			simpleMailMessage.setText(String.format("您好，%s:\n  您在最近的一次抢购物券活动中成功抢购到了%.2f元购物券，购物券的可用日期为%s至%s。\n  请注意购物券的到期时间，即时使用！！！", successfulPerson.getUsername(),successfulPerson.getDiscount(),successfulPerson.getBegin_date(),successfulPerson.getEnd_date()));
			
			javaMailSender.send(simpleMailMessage);
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
}
