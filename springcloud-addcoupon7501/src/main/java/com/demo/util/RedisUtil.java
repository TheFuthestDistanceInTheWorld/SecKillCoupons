package com.demo.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import io.netty.buffer.PooledByteBufAllocator;


@Component
public class RedisUtil {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public Boolean contains(final String key){
		Object obj = null;
		try{
			obj = redisTemplate.execute(new RedisCallback<Object>(){
				
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					StringRedisSerializer serializer = new StringRedisSerializer();
					Boolean result = null;
					result = connection.exists(serializer.serialize(key));
					return result;
				}
				
			});
		}catch(Exception e){
			log.error(e.getMessage());
		}
		
		return obj != null ? (boolean)obj : null;
	}
	
	public Boolean addElementIfNotExist(final String key){
		Object obj = null;
		
		try{
			obj = redisTemplate.execute(new RedisCallback<Object>(){
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					
					StringRedisSerializer serializer = new StringRedisSerializer();
					Object result = null;
					result = connection.setNX(serializer.serialize(key), serializer.serialize(key));
					return result;
				}
			});
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return obj != null ? (boolean)obj : null;
	}
	
	public Long delete(final String key){
		
		Object obj = null;
		try{
			obj = redisTemplate.execute(new RedisCallback<Object>(){
				
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					StringRedisSerializer serializer = new StringRedisSerializer();
					Long result = null;
					result = connection.del(serializer.serialize(key));
					return result;
				}
				
			});
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return obj != null ? (long)obj : null;
		
	}
	
	
}
