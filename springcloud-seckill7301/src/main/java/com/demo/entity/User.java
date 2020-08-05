package com.demo.entity;

public class User {
	
	private Long id;
	private String username;
	private String sex;
	private String email;
	private String password;
	private String src;
	
	public User(){}
	
	public User(Long id, String username, String sex, String email, String password, String src) {
		super();
		this.id = id;
		this.username = username;
		this.sex = sex;
		this.email = email;
		this.password = password;
		this.src = src;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", sex=" + sex + ", email=" + email + ", password="
				+ password + ", src=" + src + "]";
	}
	
}
