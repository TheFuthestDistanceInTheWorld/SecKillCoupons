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
	 * ���ַ������ɼ���key
	 * ����˽��
	 * */
	public static SecretKey generalKey(){
		byte[] encodedKey = Base64.decodeBase64(KEY);
		SecretKeySpec key = new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
		return key;
	}
	
	/*
	 * ����jwt
	 * id  ���
	 * issuer ������
	 * subject  ��������   �˴��������jwtModelת����json  ���ƺ�ֻ�洢Ȩ�޺ͱ��   �����洢������Ϣ
	 * ttlMillis  ����ʱ��
	 * 
	 * */
	
	public static String createJWT(String id,String issuer,String subject,long ttlMillis) throws Exception{
		// ָ��ǩ����ʱ��ʹ�õ�ǩ���㷨��Ҳ����header�ǲ��֣�jjwt�Ѿ����ⲿ�����ݷ�װ���ˡ�
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        System.out.println("subject:"+subject);
		//����JWT��ʱ��
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        
        //����payload��˽�������������ض���ҵ����Ҫ��ӣ����Ҫ���������֤��һ������Ҫ��jwt�Ľ��շ���ǰ��ͨ����֤��ʽ�ģ�
        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("uid","123456");
        claims.put("user_name","admin");
        claims.put("nick_name", "X-rapido");
        
        //��ȡ���ɵ�˽��
        SecretKey key = generalKey();
        
        JwtBuilder builder = Jwts.builder()
        		.setClaims(claims)
        		.setId(id)
        		.setIssuedAt(now)
        		.setIssuer(issuer)
        		.setSubject(subject)
        		.signWith(signatureAlgorithm, key);
        
        //���ù���ʱ��
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
		//�����Լ���ҵ���޸�
		Claims claims = JwtUtil.parseJWT(jwtToken);
		String subject = claims.getSubject();
		
		//�˴��õ�token�д洢�Ķ�����Ϣ  ���Խ���һЩ��֤����   Ȩ�޼���
		JwtModel jwtModel = objectMapper.readValue(subject, JwtModel.class);
		
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date expiration = claims.getExpiration();
		//�˴�Ӧ����֤����ʱ��
		
		return true;
		
	}
	
	
	
}
