package com.demo.auth;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Arrays;
import org.jboss.logging.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.demo.dto.ReturnData;
import com.demo.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ConfigurationProperties("org.my.jwt")
public class JwtTokenFilter implements GlobalFilter,Ordered{
	Logger log = Logger.getLogger(this.getClass());
	
	private String[] skipAuthUrls;
	//支持对象类型转换为  json格式
	private ObjectMapper objectMapper;
	
	public JwtTokenFilter(ObjectMapper objectMapper){
		this.objectMapper = objectMapper;
	}

	@Override
	public int getOrder() {
		
		return -100;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String url = exchange.getRequest().getURI().getPath();
		
		//判断请求的url是否在无需检查的url列表中
		if(null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)){
			return chain.filter(exchange);
		}
		
		//从请求头中获取token
		String token = exchange.getRequest().getHeaders().getFirst("Authorization");
		
		ServerHttpResponse resp = exchange.getResponse();
		
		//token是null或者全是空格
		if(StringUtils.isBlank(token)){
			return authErro(resp,"请登录");
		}else{
			try{
				
				//此处是对Token的验证   此处就需要查找数据库  对应数据库信息(但也可以不查数据库，只要不过期就好了)
				JwtUtil.checkToken(token, objectMapper);
				return chain.filter(exchange);
				
			}catch(ExpiredJwtException e){
				log.error(e.getMessage(),e);
                if(e.getMessage().contains("Allowed clock skew")){
                    return authErro(resp,"认证过期");
                }else{
                    return authErro(resp,"认证失败");
                }
			}catch (Exception e) {
                log.error(e.getMessage(),e);
                return authErro(resp,"认证失败");
            }
			
		}
		
	}
	
	private Mono<Void> authErro(ServerHttpResponse resp,String message){
		resp.setStatusCode(HttpStatus.UNAUTHORIZED);
		resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
		
		//初始化结果对象
		ReturnData<String> returnData = new ReturnData<String>(org.apache.http.HttpStatus.SC_UNAUTHORIZED, message, message);
		
		
		String returnStr = "";
		try{
			
			returnStr = objectMapper.writeValueAsString(returnData);
			
		}catch(JsonProcessingException e){
			
			log.error(e.getMessage(),e);
		}
		
		DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));

	}
	
}
