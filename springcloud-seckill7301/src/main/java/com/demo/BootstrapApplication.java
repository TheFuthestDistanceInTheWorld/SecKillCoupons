package com.demo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
@EnableDiscoveryClient
@EnableAsync
@MapperScan("com.demo.dao")
public class BootstrapApplication {
	
	public static void main(String[] args){
		SpringApplication.run(BootstrapApplication.class, args);
	}
	
}

