package com.demo.feginservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="SnowFlake",value="SnowFlake")
public interface SnowFlake {
	
	@GetMapping("/getId")
	public Long  getId();
	
}
