package com.demo.Enum;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum GrabCouponState {
	
	NOTEXIST(HttpStatus.OK,"秒杀不存在"),NOTSTARTED(HttpStatus.OK,"秒杀未开始"),FINISHED(HttpStatus.OK,"秒杀已结束");
	
	private ResponseEntity<String> responseEntity;
	
	private GrabCouponState(HttpStatus status,String body){
		this.responseEntity = new ResponseEntity<String>(body,status);
	}

	public ResponseEntity<String> getResponseEntity() {
		return responseEntity;
	}
	
}
