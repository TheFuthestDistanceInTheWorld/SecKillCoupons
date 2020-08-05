package com.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.demo.entity.GrabCoupon;
import com.demo.vo.SuccessfulPerson;

public interface GrabCouponDao {
	
	public List<Long> selectAllGrabCouponDao();
	
	public GrabCoupon selectGrabCouponById(@Param("grabcouponid") Long grabcouponid);
	
	
}
