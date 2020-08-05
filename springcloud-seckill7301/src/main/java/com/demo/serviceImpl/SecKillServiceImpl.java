package com.demo.serviceImpl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import com.demo.Enum.GrabCouponState;
import com.demo.dao.CouponDao;
import com.demo.dao.GrabCouponDao;
import com.demo.dao.UserDao;
import com.demo.entity.Coupon;
import com.demo.entity.GrabCoupon;
import com.demo.entity.User;
import com.demo.service.SecKillService;
import com.demo.util.DateOperator;
import com.demo.vo.SuccessfulPerson;

@Component
public class SecKillServiceImpl implements SecKillService{
	
	private static Logger logger = LoggerFactory.getLogger(SecKillServiceImpl.class);
	
	@Autowired
	private DefaultRedisScript<Long> defaultRedisScript;
	
	@Autowired
	private StringRedisTemplate redistemplate;
	
	@Autowired
	private GrabCouponDao grabCouponDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CouponDao couponDao;
	
	/*
	 * 
	 * userid:用户编号
	 * seckillid:秒杀编号
	 * 
	 * @Return:  一个状态值      1 代表成功    2 代表失败   0代表货物抢完了   -1代表抢购的过程中抛出了异常
	 * 
	 * */
	@Override
	public Long seckill(Long userid, Long seckillid) {
		List<String> keys = null;
		Long result = null;
		
		try{
			keys = new LinkedList<String>();
			keys.add(userid.toString());
			keys.add(seckillid.toString());
			result = redistemplate.execute(defaultRedisScript, keys);
			
		}catch(Exception e){
			logger.error("error",e);
			result = new Long(-1);
			return result;
		}
		
		return result;
	}

	@Override
	public GrabCouponState validateKey(Long key) {
		GrabCoupon grabcoupon = grabCouponDao.selectGrabCouponById(key);
		if(grabcoupon==null){
			return GrabCouponState.NOTEXIST;
		}
		
		Long begintime = grabcoupon.getBegin_time().getTime();
		Long currenttime = System.currentTimeMillis();
		if(currenttime > begintime){
			return GrabCouponState.FINISHED;
		}
		
		return GrabCouponState.NOTSTARTED;
	}

	@Override
	public SuccessfulPerson findGrabCouponMessage(Long grabcouponid, Long userid) {
		SuccessfulPerson successfulperson = new SuccessfulPerson();
		User user = userDao.selectUserById(userid);
		GrabCoupon grabcoupon = grabCouponDao.selectGrabCouponById(grabcouponid);
		
		successfulperson.setUserid(userid);
		successfulperson.setGrabcouponid(grabcouponid);
		successfulperson.setUsername(user.getUsername());
		successfulperson.setEmail(user.getEmail());
		
		successfulperson.setMin(grabcoupon.getMin());
		successfulperson.setDiscount(grabcoupon.getDiscount());
		successfulperson.setBegin_date(DateOperator.convertDateToString(grabcoupon.getBegin_date()));
		successfulperson.setEnd_date(DateOperator.convertDateToString(grabcoupon.getEnd_date()));
		
		return successfulperson;
	}
	
	public Boolean addACoupon(SuccessfulPerson person){
		Coupon coupon = new Coupon();
		coupon.setUser_id(person.getUserid());
		coupon.setMin(person.getMin());
		coupon.setDiscount(person.getDiscount());
		coupon.setBegin_date(person.getBegin_date());
		coupon.setEnd_date(person.getEnd_date());
		
		couponDao.insertCoupon(coupon);
		
		return true;
	}

}
