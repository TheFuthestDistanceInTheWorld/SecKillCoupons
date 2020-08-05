package com.demo.dao;

import org.apache.ibatis.annotations.Param;

import com.demo.entity.Coupon;

public interface CouponDao {
	
	public void insertACoupon(@Param("coupon") Coupon coupon);
	
	
}
