package com.demo.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RedisConfiguration {
	
	@Bean
	public DefaultRedisScript<Long> defaultRedisScript(){
		
		DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<Long>();
		defaultRedisScript.setResultType(Long.class);
		defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("static/lua/SecKill.lua")));
		
		return defaultRedisScript;
	}
	
	
	
}
