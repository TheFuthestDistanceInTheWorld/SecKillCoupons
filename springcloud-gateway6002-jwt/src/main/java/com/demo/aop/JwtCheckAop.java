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
	
	//�Ա���ע�����εķ���������ǿ
	@Pointcut("@annotation(com.demo.annotation.JwtCheck)")
	private void apiAop(){
	}
	
	@Around("apiAop()")
	public Object aroundApi(ProceedingJoinPoint point) throws Throwable{
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		//��ȡ����������ע��   ��ά���飬Ϊʲô�Ƕ�ά������  ��Ϊһ�����������кü���ע��
		Annotation[][] parameterAnnotationArray = method.getParameterAnnotations();
		Object[] args = point.getArgs();
		
		String token = null;
		
		//�ҵ��Ǹ�����ע��RequestHeader��ֵΪAuthorization�Ĳ���    ��ȡ�ĸ�������ֵ��Ϊtoken  ���������token���ú����Ǹ�
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
			return authErro("���¼");
			
		}else{
			
			try{
				//�˴���token����check  �����޷���token�л�ȡ��Ϣ �����ӿ���ʹ��  ���Ҳ�ϲ����  ���Ƿ�����һ��  �����������������Ҫ
				JwtUtil.checkToken(token, objectMapper);
				//��֤��ɺ�ִ�ж�Ӧ����
				Object proceed = point.proceed();
				return proceed;
			}catch(ExpiredJwtException e){
				log.error(e.getMessage(),e);
				if(e.getMessage().contains("Allowed clock skew")){
					return authErro("��֤����");
				}else{
					return authErro("��֤ʧ��");
				}
			}catch(Exception e){
				log.error(e.getMessage(),e);
				return authErro("��֤ʧ��");
			}
			
		}
	}
	
	private Object authErro(String message) {
        ReturnData<String> returnData = new ReturnData<>(org.apache.http.HttpStatus.SC_UNAUTHORIZED, message, message);
        return returnData;
    }
	
}
