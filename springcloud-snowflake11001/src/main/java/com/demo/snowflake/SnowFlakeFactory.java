package com.demo.snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class SnowFlakeFactory {
	
	private final static long Start_Stmp = 1592230473310L;
										   
	private final static long Sequence_Bit = 12;
	private final static long Machine_Bit = 5;
	private final static long Datacenter_Bit = 5;
	
	private final static long Max_Datacenter_Num = -1L ^ (-1L << Datacenter_Bit);
	private final static long Max_Machine_Num = -1L ^ (-1L << Machine_Bit);
	private final static long Max_Sequence = -1L ^ (-1L << Sequence_Bit);
	
	private final static long Machine_Left = Sequence_Bit;
	private final static long Datacenter_Left = Sequence_Bit + Machine_Bit;
	private final static long Timestmp_Left = Datacenter_Left + Datacenter_Bit;
	
	//数据源编号   5位
	private final long datacenterId;
	//机器编号  5位
	private final long machineId;
	//序列号  12位
	private long sequence = 0L;
	//上次时间戳  
	private long lastStmp = -1L;
	
	
	public SnowFlakeFactory(long datacenterId, long machineId){
		
		if(datacenterId > Max_Datacenter_Num || datacenterId < 0){
			throw new IllegalArgumentException(" datacenterId 必须介于[0,31] ");
		}
		
		if(machineId > Max_Machine_Num || machineId < 0){
			throw new IllegalArgumentException(" machineId 必须介于[0,31] ");
		}
		
		this.datacenterId = datacenterId;
		this.machineId = machineId;
		
	}
	
	
	
	public synchronized Long nextId(){
		
		long currStmp = getNewstmp();
		
		
		/*
		 * 
		 * 当前时间小于最后一次访问时间  说明产生了时钟回拨
		 * 网上多处代码对此的处理是   进行等待等待一定时间后抛出异常
		 * 但我认为  抛出异常是不合理的   因为抛出异常通常意味这服务调用的失败
		 * 如果一旦因为服务调用失败而导致整个操作回滚，这是比较糟糕的。
		 * 这里采用的解决方案是  循环等待   直至时钟到达上次访问时间戳为止
		 * 虽然偶尔会产生性能下降  但不总是会造成抛出异常  除非服务过于繁忙  
		 * 
		 * 
		 * */
		
		while(currStmp < lastStmp){
			
			long offset = lastStmp - currStmp;
			//睡(lastTimestamp - currentTimestamp) ma让其追上
			
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(offset));
			//重新获取时间
			currStmp = getNewstmp();
					
			if(currStmp >= lastStmp){
			break;
			}
			
		}

			
		//此处更新序列号位数
		if(currStmp == lastStmp){
			//相同毫秒内，序列号自增
			sequence = (sequence + 1) & Max_Sequence;
			//同一毫秒的序列数已经达到最大
			if(sequence == 0L){
				currStmp  = getNextMill();
			}
		}else{
			//不同毫秒内，序列号置为0
			sequence = 0L;
		}
		
		lastStmp = currStmp;
		
		//真正生成数字在这一步    这个extension是什么意思啊？
		long id = (currStmp - Start_Stmp) << Timestmp_Left //时间戳部分
				| datacenterId << Datacenter_Left
				| machineId << Machine_Left
				| sequence;
		
		return id;
	}
	
	/*
	 * 自旋锁获取当前时间戳
	 * */
	private long getNextMill(){
		
		long mill = getNewstmp();
		while(mill <= lastStmp){
			mill = getNewstmp();
		}
		return mill;
	}
	private long getNewstmp(){
		return System.currentTimeMillis();
	}
	
}
