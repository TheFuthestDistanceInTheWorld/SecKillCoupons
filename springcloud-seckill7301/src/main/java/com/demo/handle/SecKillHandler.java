package com.demo.handle;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class SecKillHandler {
	
	public static ResponseEntity<String> tooMuchRequest(BlockException e){
		return new ResponseEntity<String>("当前服务不可用，请重试",HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	public static ResponseEntity<String> threwException(BlockException e){
		return new ResponseEntity<String>("服务器错误，可尝试再次请求",HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
