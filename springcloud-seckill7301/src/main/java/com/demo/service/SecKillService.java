package com.demo.service;

import com.demo.Enum.GrabCouponState;
import com.demo.vo.SuccessfulPerson;

public interface SecKillService {
	
	public Long seckill(Long userid,Long seckillid);
	
	public GrabCouponState validateKey(Long key);
	
	public SuccessfulPerson findGrabCouponMessage(Long grabcouponid,Long userid);
	
	public Boolean addACoupon(SuccessfulPerson person);
}
