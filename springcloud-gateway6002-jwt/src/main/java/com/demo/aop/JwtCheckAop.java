package com.demo.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import com.demo.dto.ReturnData;
import com.demo.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;

@Component
@Aspect
public class JwtCheckAop {
	
	Logger log = Logger.getLogger(this.getClass());
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	//对被该注解修饰的方法进行增强
	@Pointcut("@annotation(com.demo.annotation.JwtCheck)")
	private void apiAop(){
	}
	
	@Around("apiAop()")
	public Object aroundApi(ProceedingJoinPoint point) throws Throwable{
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		//获取参数上所有注解   二维数组，为什么是二维数组呢  因为一个参数可能有好几个注解
		Annotation[][] parameterAnnotationArray = method.getParameterAnnotations();
		Object[] args = point.getArgs();
		
		String token = null;
		
		//找到那个存在注解RequestHeader且值为Authorization的参数    获取哪个参数的值作为token  如果有两个token就拿后面那个
		for(Annotation[] annotations:parameterAnnotationArray){
			for(Annotation a:annotations){
				System.out.println(a);
				if(a instanceof RequestHeader){
					RequestHeader requestHeader = (RequestHeader)a;
					if("Authorization".equals(requestHeader.value())){
						
						token = (String)args[ArrayUtils.indexOf(parameterAnnotationArray,annotations)];

					}
				}
			}
		}
		
		if(StringUtils.isBlank(token)){
			return authErro("请登录");
			
		}else{
			
			try{
				//此处对token进行check  检测后无法从token中获取信息 并被接口所使用  是我不喜欢的  但是反过来一想  本来这个方法并不需要
				JwtUtil.checkToken(token, objectMapper);
				//验证完成后执行对应方法
				Object proceed = point.proceed();
				return proceed;
			}catch(ExpiredJwtException e){
				log.error(e.getMessage(),e);
				if(e.getMessage().contains("Allowed clock skew")){
					return authErro("认证过期");
				}else{
					return authErro("认证失败");
				}
			}catch(Exception e){
				log.error(e.getMessage(),e);
				return authErro("认证失败");
			}
			
		}
	}
	
	private Object authErro(String message) {
        ReturnData<String> returnData = new ReturnData<>(org.apache.http.HttpStatus.SC_UNAUTHORIZED, message, message);
        return returnData;
    }
	
}
