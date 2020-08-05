package com.demo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.annotation.JwtCheck;
import com.demo.dto.ReturnData;
import com.demo.dto.UserDTO;
import com.demo.jwt.JwtModel;
import com.demo.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private ObjectMapper objectMapper;
	
	@Value("${org.my.jwt.effective-time}")
	private String effectiveTime;
	
	public AuthController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
	
	@PostMapping("/login")
	public ReturnData<String> login(@RequestBody UserDTO userdto) throws Exception{
		List<String> roleIdList = new ArrayList<String>();
		roleIdList.add("role_test_1");
		JwtModel jwtModel = new JwtModel("test",roleIdList);
		int effectivTimeInt = Integer.valueOf(effectiveTime.substring(0,effectiveTime.length()-1));
        String effectivTimeUnit = effectiveTime.substring(effectiveTime.length()-1,effectiveTime.length());
        String jwt = null;
        switch (effectivTimeUnit){
        case "s" :{
            //秒
            jwt = JwtUtil.createJWT("test", "test", objectMapper.writeValueAsString(jwtModel), effectivTimeInt * 1000L);
            break;
        }
        case "m" :{
            //分钟
            jwt = JwtUtil.createJWT("test", "test", objectMapper.writeValueAsString(jwtModel), effectivTimeInt * 60L * 1000L);
            break;
        }
        case "h" :{
            //小时
            jwt = JwtUtil.createJWT("test", "test", objectMapper.writeValueAsString(jwtModel), effectivTimeInt * 60L * 60L * 1000L);
            break;
        }
        case "d" :{
            //小时
            jwt = JwtUtil.createJWT("test", "test", objectMapper.writeValueAsString(jwtModel), effectivTimeInt * 24L * 60L * 60L * 1000L);
            break;
        }
    }
		return new ReturnData<String>(HttpStatus.SC_OK,"认证成功",jwt);
	}
	
	@GetMapping("/unauthorized")
	public ReturnData<String> unauthorized(){
		return new ReturnData<String>(HttpStatus.SC_UNAUTHORIZED,"未认证,请重新登陆",null);
	}
	
	@GetMapping("/testJwtCheck")
	@JwtCheck
	public ReturnData<String> testJwtCheck(@RequestParam("name")@Valid String name,@RequestHeader("Authorization")String token){
		
		return new ReturnData<String>(HttpStatus.SC_OK,"请求成功咯","请求成功咯"+name);
		
	}
	
	
}
