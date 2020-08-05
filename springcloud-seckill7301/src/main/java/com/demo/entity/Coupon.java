package com.demo.entity;

import java.util.Date;

public class Coupon {
	
	private Long id;
	private Long user_id;
	private Double min;
	private Double discount;
	private String begin_date;
	private String end_date;
	
	public Coupon(){}
	
	public Coupon(Long id, Long user_id, Double min, Double discount, String begin_date, String end_date) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.min = min;
		this.discount = discount;
		this.begin_date = begin_date;
		this.end_date = end_date;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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
		return "Coupon [id=" + id + ", user_id=" + user_id + ", min=" + min + ", discount=" + discount + ", begin_date="
				+ begin_date + ", end_date=" + end_date + "]";
	}
	
	
}
