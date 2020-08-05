package com.demo.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
@Component
public class RedisUtil {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	
	/*
	 * 判断一个key是否存在，如果存在返回true  在管道和事务下使用默认返回null
	 * 
	 * 
	 * */
	public Boolean isExist(final String key){
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
			e.printStackTrace();
		}
		
		return obj != null ? (boolean)obj : null;
	}
	

	public String get(final String key){
		
		Object obj = null;
		try{
			obj = redisTemplate.execute(new RedisCallback<Object>(){

				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					StringRedisSerializer serializer = new StringRedisSerializer();
					byte[] data = connection.get(serializer.serialize(key));
					
					connection.close();
					if(data == null){
						return null;
					}
					return  serializer.deserialize(data);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj != null ? obj.toString() : null;
		
	}
	
}
