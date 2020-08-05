package com.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.dao.CouponDao;
import com.demo.entity.Coupon;
import com.demo.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService{
	
	@Autowired
	private CouponDao couponDao;
	
	
	@Override
	public boolean AddACoupon(Coupon coupon) {
		couponDao.insertACoupon(coupon);
		return true;
	}

}
