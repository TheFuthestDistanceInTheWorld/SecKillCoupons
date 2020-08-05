package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.snowflake.SnowFlakeFactory;

@RestController
public class SnowFlakeController {
	
	@Autowired
	private SnowFlakeFactory snowFlakeFactory;
	
	@GetMapping("/getId")
	public Long getId(){
		return snowFlakeFactory.nextId();
	}
	
}
