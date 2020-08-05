package com.demo.vo;

import java.io.Serializable;

public class SuccessfulPerson implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userid;
	private String username;
	private String email;
	
	private Long grabcouponid;//应该给这个参数么？
	
	
	private Double min;
	private Double discount;
	private String begin_date;
	private String end_date;
	
	public SuccessfulPerson(){}
	
	public SuccessfulPerson(Long userid, String username, String email, Long grabcouponid, Double min, Double discount,
			String begin_date, String end_date) {
		super();
		this.userid = userid;
		this.username = username;
		this.email = email;
		this.grabcouponid = grabcouponid;
		this.min = min;
		this.discount = discount;
		this.begin_date = begin_date;
		this.end_date = end_date;
	}
	
	public Long getUserid() {
		return userid;
	}
	
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getGrabcouponid() {
		return grabcouponid;
	}

	public void setGrabcouponid(Long grabcouponid) {
		this.grabcouponid = grabcouponid;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	@Override
	public String toString() {
		return "SuccessfulPerson [userid=" + userid + ", username=" + username + ", email=" + email + ", grabcouponid="
				+ grabcouponid + ", min=" + min + ", discount=" + discount + ", begin_date=" + begin_date
				+ ", end_date=" + end_date + "]";
	}
	
}
