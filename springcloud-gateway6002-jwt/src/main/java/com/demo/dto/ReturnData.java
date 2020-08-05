package com.demo.dto;

public class ReturnData<T> {
	private int code;
	private String mass;
	private T data;
	
	public ReturnData(){}
	
	public ReturnData(int code, String mass, T data) {
		super();
		this.code = code;
		this.mass = mass;
		this.data = data;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMass() {
		return mass;
	}
	public void setMass(String mass) {
		this.mass = mass;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ReturnData [code=" + code + ", mass=" + mass + ", data=" + data + "]";
	}
}
