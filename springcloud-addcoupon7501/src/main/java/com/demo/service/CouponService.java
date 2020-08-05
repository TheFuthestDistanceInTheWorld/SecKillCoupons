package com.demo.service;

import org.springframework.stereotype.Service;

import com.demo.entity.Coupon;

@Service
public interface CouponService {
	
	public boolean AddACoupon(Coupon coupon);
	
}
