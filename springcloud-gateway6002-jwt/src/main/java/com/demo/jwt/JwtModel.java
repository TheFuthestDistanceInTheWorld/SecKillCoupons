package com.demo.jwt;

import java.util.List;

public class JwtModel {
	
	private String userName;
	//可以理解为角色的权限  例如  user  admin  vipuser
	private List<String> roleIdList;
	
	public JwtModel(){}
	
	public JwtModel(String userName, List<String> roleIdList) {
		super();
		this.userName = userName;
		this.roleIdList = roleIdList;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	@Override
	public String toString() {
		return "JwtModel [userName=" + userName + ", roleIdList=" + roleIdList + "]";
	}
	
}
