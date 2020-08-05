package com.demo.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configurable
public class RedisBloomFilter {

	@Autowired
	private StringRedisTemplate redisTemplate;
	
	
	private final String BITMAP_KEY;
	
	@Autowired
	private BloomFilterHelper<CharSequence> bloomFilterHelper;
	
	
	public RedisBloomFilter(String bitmap_key){
		this.BITMAP_KEY = bitmap_key;
	}
	
	
	public <T> void initBloomFilter(List<T> valueList){
		
		redisTemplate.executePipelined(new RedisCallback<Long>(){
			
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				try{
					connection.openPipeline();
					for(T value:valueList){
						int[] offset  =bloomFilterHelper.murmurHashOffset(value.toString());
						StringRedisSerializer serializer = new StringRedisSerializer();
						for(int i:offset){
							connection.setBit(serializer.serialize(BITMAP_KEY), i, true);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					connection.closePipeline();
				}
				return null;
			}
		});
	}
	
	/*
	 * 传入任意类型，但是传入的类型会被toString()
	 * 
	 * */
	public <T> void addElement(T value){
		
		int[] offset = bloomFilterHelper.murmurHashOffset(value.toString());
		for(int i:offset){
			redisTemplate.opsForValue().setBit(BITMAP_KEY, i, true);
		}
		
	}
	
	/*
	 * 传入任意类型，但是传入的类型会被toString()  也就是说toString之后的值才是被记录的值
	 * 
	 * */
	public <T> boolean contains(T value){
		
		int[] offset = bloomFilterHelper.murmurHashOffset(value.toString());
		for(int i:offset){
			if(!redisTemplate.opsForValue().getBit(BITMAP_KEY, (long)i)){
				return false;
			}
		}
		return true;
		
	}
	
	

}
