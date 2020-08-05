package com.demo.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.snowflake.SnowFlakeFactory;

@Configuration
public class SnowFlakeFactoryConfiguration {
	
	
	
	@Value("${snowflake.datacenterId}")
	private Long datacenterId;
	
	@Value("${snowflake.machineId}")
	private Long machineId;
	
	@Bean
	public SnowFlakeFactory initSnowFlakeFactory(){
		
		return new SnowFlakeFactory(this.datacenterId,this.machineId);
	}
	
	
	
}
