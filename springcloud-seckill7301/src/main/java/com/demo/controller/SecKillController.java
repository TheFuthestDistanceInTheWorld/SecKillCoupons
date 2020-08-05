package com.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.demo.handle.SecKillHandler;
import com.demo.provider.TaskProvider;
import com.demo.service.SecKillService;
import com.demo.util.RedisBloomFilter;
import com.demo.util.RedisUtil;

@RestController
public class SecKillController {
	
	@Autowired
	private SecKillService secKillService;
	
	@Autowired
	private RedisBloomFilter redisBloomFilter;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private TaskProvider taskProvider;
	
	
	//private RateLimiter rateLimiter = RateLimiter.create(100);
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@PostMapping("/seckill")
	@SentinelResource(value="seckill",blockHandlerClass = SecKillHandler.class,blockHandler="tooMuchRequest",
			fallbackClass=SecKillHandler.class ,defaultFallback = "threwException"
			)
	public ResponseEntity<String> secKill(@RequestParam("key") Long key,@RequestParam("userid") Long userid){
		/*
		 * 使用令牌桶算法进行限流   此处取消限流  因为限流的任务交由 sentinal去管理
		 * 
		 * if(!rateLimiter.tryAcquire(2,TimeUnit.SECONDS)){
			return "请求超时，可再次尝试";
			}
		 * 
		 * */
		
		
		//System.out.println("==========="+key);
		/* 
		 * 判断抢购任务是否存在(缓存穿透处理方案)：
		 * 1.先查询redis  看看是否存在该抢购任务 (如果返回-1说明秒杀已经结束)
		 * 2.布隆过滤器判断一个抢购任务一定不存在  此时就可以返回   请求了不存在的秒杀任务(防止缓存穿透)
		 * 		布隆过滤器应该有完善的初始化过程   并且设置操作应该放在数据库插入数据的部分
		 * 
		 * 3.再查数据库  确定是否存在  如果不存在返回   请求了不存在的秒杀任务  如果存在，就返回 秒杀已经结束  或者秒杀还未开始
		 * 
		 * 缓存击穿处理方案:
		 * 1.热点数据不失效(这里觉得单纯使库存不失效不是一个好的举措，还是应该设置失效时间并且将处于准备失效状态的库存置为-1)
		 * 2.秒杀结束value置为-1(使用  getandset 获取订单余量的同时更改当前抢购的状态为已结束)  这一部分应该交给admin去处理
		 * 
		 * 缓存雪崩处理方案:
		 * 1.使用redis集群  保证缓存高可用
		 * 2.使用hystrix 保证服务器的高可用
		 * 3.在给秒杀结束的库存设置一个失效时间，失效时间应该具有随机性   包括用于存放用户抢购信息的list  也应该有一个失效时间
		 * 
		 * 库存是热点数据   但仍然应该设置失效时间 否则大量的不失效数据存在  会对性能造成影响
		 * 
		 * */
		 
		//查redis
		String num = redisUtil.get("grabcoupon-"+key);
		//System.out.println("==========="+num);
		if(num == null){
			
			//bloomfilter进行过滤
			if(!redisBloomFilter.contains(key)){
				return new ResponseEntity<String>("请勿请求不存在的秒杀",HttpStatus.OK) ;
			}
			
			//查询数据库  通过查数据库决定秒杀是已经结束   还是秒杀还未开始  并返回开始时间
			return secKillService.validateKey(key).getResponseEntity();
		}
		
		//如果不为null且值为-1   可直接返回结果   如果为null  则继续执行
		
		if(Integer.valueOf(num) == -1){
			return new ResponseEntity<String>("你手慢了,秒杀已经结束",HttpStatus.OK);
		}
		
		//开始抢购   (int) (Math.random()*(10000-1)+1)
		Long result = null;
		result = secKillService.seckill(userid, key);
		
		if(result == 0){
			return new ResponseEntity<String>("你手慢了,秒杀已经结束",HttpStatus.OK);
		}
		else if(result == 1){
			//此处使用消息队列     使用direct方式配置
			taskProvider.sendSuccessfulPerson(secKillService.findGrabCouponMessage(key, userid));
			return new ResponseEntity<String>("抢购成功:",HttpStatus.OK);
		}
		else if(result == 2){
			return new ResponseEntity<String>("您已经抢购过了",HttpStatus.OK);
		}
		else{
			return new ResponseEntity<String>("服务器错误，可尝试再次请求",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
