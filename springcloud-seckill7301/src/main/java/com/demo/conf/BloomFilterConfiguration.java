package com.demo.conf;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.util.BloomFilterHelper;
import com.demo.util.RedisBloomFilter;
import com.google.common.hash.Funnels;

@Configuration
public class BloomFilterConfiguration {
	
	// 期望插入的数据个数
	@Value("${spring.redis.bloomfilter.expectedInsertions}")
	private Integer expectedInsertions;
	
	// 出现错误的概率
	@Value("${spring.redis.bloomfilter.fpp}")
	private Double fpp;
	
	//指定bloom过滤器的名称
	@Value("${spring.redis.bloomfilter.name}")
	private String name;
	
	@Bean
	public BloomFilterHelper<CharSequence>  initBloomFilterHelper(){
		return new BloomFilterHelper<CharSequence>(Funnels.stringFunnel(Charset.defaultCharset()),expectedInsertions,fpp);
	}
	
	@Bean
	public RedisBloomFilter initRedisBloomFilter(){
		return new RedisBloomFilter(name);
	}
	
}
