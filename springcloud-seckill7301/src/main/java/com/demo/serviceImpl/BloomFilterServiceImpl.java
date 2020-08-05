package com.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.dao.GrabCouponDao;
import com.demo.service.BloomFilterService;

@Service
public class BloomFilterServiceImpl implements BloomFilterService{
	
	
	@Autowired
	private GrabCouponDao grabCouponDao;
	
	@Override
	public List<Long> getAllGrabCouponId() {
		
		return grabCouponDao.selectAllGrabCouponDao();
		
	}

}
