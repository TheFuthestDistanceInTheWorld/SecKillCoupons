package com.demo.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GrabCoupon {
	
	
	private Long id ;
	
	private Long count;
	private Double min;
	private Double discount;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date begin_date;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date end_date;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date begin_time;
	
	public GrabCoupon(){}
	
	public GrabCoupon(Long id, Long count, Double min, Double discount, Date begin_date, Date end_date,
			Date begin_time) {
		super();
		this.id = id;
		this.count = count;
		this.min = min;
		this.discount = discount;
		this.begin_date = begin_date;
		this.end_date = end_date;
		this.begin_time = begin_time;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
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

	public Date getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	@Override
	public String toString() {
		return "GrabCoupon [id=" + id + ", count=" + count + ", min=" + min + ", discount=" + discount + ", begin_date="
				+ begin_date + ", end_date=" + end_date + ", begin_time=" + begin_time + "]";
	}
	
}
