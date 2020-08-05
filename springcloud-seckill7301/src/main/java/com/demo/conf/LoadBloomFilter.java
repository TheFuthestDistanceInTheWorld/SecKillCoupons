package com.demo.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.demo.service.BloomFilterService;
import com.demo.util.RedisBloomFilter;


/*
 * 初始化对应的布隆过滤器
 * 将数据库中的信息通过管道
 * 刷新到过滤器中
 * */

@Component
@Order(value = 1)
public class LoadBloomFilter implements ApplicationRunner{
	
	@Autowired
	private RedisBloomFilter redisBloomFilter;
	
	@Autowired
	private BloomFilterService bloomFilterService;
	
	 // 细节：  将所有数据库中表grabcoupons中的内容刷新进过滤器中
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		List<Long> grabcouponid = bloomFilterService.getAllGrabCouponId();
		//使用管道的方式  将数据批量的添加至布隆过滤器
		redisBloomFilter.initBloomFilter(grabcouponid);
	}
	
}


