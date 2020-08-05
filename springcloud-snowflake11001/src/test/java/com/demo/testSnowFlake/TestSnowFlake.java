package com.demo.testSnowFlake;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.snowflake.SnowFlakeFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSnowFlake {
	
	@Autowired
	private SnowFlakeFactory snowFlakeFactory;
	
	@Test
	public void test100000(){
		
		ExecutorService es = Executors.newFixedThreadPool(10);
		List<Future<Long>> l = new LinkedList<Future<Long>>();
		long begin  = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			l.add(es.submit(new Callable<Long>(){

				public Long call() throws Exception {
					
					return snowFlakeFactory.nextId();
				}
				
			})
			);
		}
		for(int i=0;i<100000;i++){
			
			try {
				l.get(i).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(System.currentTimeMillis() - begin);
		
	}
	
	@Test
	public void testSerializable100000(){
		long begin  = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			
			snowFlakeFactory.nextId();
		}
		
		System.out.println(System.currentTimeMillis() - begin);
	}
	
	
}
