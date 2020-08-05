package com.demo.jwt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtUtil {
	
	public static final String KEY = "022bdc63c3c5a45879ee6581508b9d03adfec4a4658c0ab3d722e50c91a351c42c231cf43bb8f86998202bd301ec52239a74fc0c9a9aeccce604743367c9646b";
	
	
	/*
	 * 有字符串生成加密key
	 * 生成私匙
	 * */
	public static SecretKey generalKey(){
		byte[] encodedKey = Base64.decodeBase64(KEY);
		SecretKeySpec key = new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
		return key;
	}
	
	/*
	 * 创建jwt
	 * id  编号
	 * issuer 发行人
	 * subject  核心内容   此处传入的是jwtModel转化成json  它似乎只存储权限和编号   并不存储其他信息
	 * ttlMillis  到期时间
	 * 
	 * */
	
	public static String createJWT(String id,String issuer,String subject,long ttlMillis) throws Exception{
		// 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        System.out.println("subject:"+subject);
		//生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("uid","123456");
        claims.put("user_name","admin");
        claims.put("nick_name", "X-rapido");
        
        //获取生成的私匙
        SecretKey key = generalKey();
        
        JwtBuilder builder = Jwts.builder()
        		.setClaims(claims)
        		.setId(id)
        		.setIssuedAt(now)
        		.setIssuer(issuer)
        		.setSubject(subject)
        		.signWith(signatureAlgorithm, key);
        
        //设置过期时间
        if(ttlMillis >=0 ){
        	long expMillis = nowMillis + ttlMillis;
        	Date exp = new Date(expMillis);
        	builder.setExpiration(exp);
        }
        
        return builder.compact();
	}
	
	public static Claims parseJWT(String jwt) throws Exception{
		
		SecretKey key = generalKey();
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		
		return claims;
	}
	
	public static boolean checkToken(String jwtToken,ObjectMapper objectMapper) throws Exception{
		//根据自己的业务修改
		Claims claims = JwtUtil.parseJWT(jwtToken);
		String subject = claims.getSubject();
		
		//此处拿到token中存储的对象信息  可以进行一些验证操作   权限鉴定
		JwtModel jwtModel = objectMapper.readValue(subject, JwtModel.class);
		
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date expiration = claims.getExpiration();
		//此处应该验证过期时间
		
		return true;
		
	}
	
	
	
}
