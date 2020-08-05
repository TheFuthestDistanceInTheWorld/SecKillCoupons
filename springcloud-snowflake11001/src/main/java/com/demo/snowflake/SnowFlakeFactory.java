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
	
	//����Դ���   5λ
	private final long datacenterId;
	//�������  5λ
	private final long machineId;
	//���к�  12λ
	private long sequence = 0L;
	//�ϴ�ʱ���  
	private long lastStmp = -1L;
	
	
	public SnowFlakeFactory(long datacenterId, long machineId){
		
		if(datacenterId > Max_Datacenter_Num || datacenterId < 0){
			throw new IllegalArgumentException(" datacenterId �������[0,31] ");
		}
		
		if(machineId > Max_Machine_Num || machineId < 0){
			throw new IllegalArgumentException(" machineId �������[0,31] ");
		}
		
		this.datacenterId = datacenterId;
		this.machineId = machineId;
		
	}
	
	
	
	public synchronized Long nextId(){
		
		long currStmp = getNewstmp();
		
		
		/*
		 * 
		 * ��ǰʱ��С�����һ�η���ʱ��  ˵��������ʱ�ӻز�
		 * ���϶ദ����Դ˵Ĵ�����   ���еȴ��ȴ�һ��ʱ����׳��쳣
		 * ������Ϊ  �׳��쳣�ǲ������   ��Ϊ�׳��쳣ͨ����ζ�������õ�ʧ��
		 * ���һ����Ϊ�������ʧ�ܶ��������������ع������ǱȽ����ġ�
		 * ������õĽ��������  ѭ���ȴ�   ֱ��ʱ�ӵ����ϴη���ʱ���Ϊֹ
		 * ��Ȼż������������½�  �������ǻ�����׳��쳣  ���Ƿ�����ڷ�æ  
		 * 
		 * 
		 * */
		
		while(currStmp < lastStmp){
			
			long offset = lastStmp - currStmp;
			//˯(lastTimestamp - currentTimestamp) ma����׷��
			
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(offset));
			//���»�ȡʱ��
			currStmp = getNewstmp();
					
			if(currStmp >= lastStmp){
			break;
			}
			
		}

			
		//�˴��������к�λ��
		if(currStmp == lastStmp){
			//��ͬ�����ڣ����к�����
			sequence = (sequence + 1) & Max_Sequence;
			//ͬһ������������Ѿ��ﵽ���
			if(sequence == 0L){
				currStmp  = getNextMill();
			}
		}else{
			//��ͬ�����ڣ����к���Ϊ0
			sequence = 0L;
		}
		
		lastStmp = currStmp;
		
		//����������������һ��    ���extension��ʲô��˼����
		long id = (currStmp - Start_Stmp) << Timestmp_Left //ʱ�������
				| datacenterId << Datacenter_Left
				| machineId << Machine_Left
				| sequence;
		
		return id;
	}
	
	/*
	 * ��������ȡ��ǰʱ���
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
