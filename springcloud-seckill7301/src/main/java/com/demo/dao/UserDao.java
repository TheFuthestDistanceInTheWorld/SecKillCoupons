package com.demo.dao;

import org.springframework.data.repository.query.Param;

import com.demo.entity.User;

public interface UserDao {
	public User selectUserById(@Param("userid")Long userid);
}
