package com.acme.httpcache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HttpCacheAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpCacheAspect.class);
	
	@Pointcut("execution(* io.crnk.core.engine.internal.http.JsonApiRequestProcessor.setResponse(..))")
	public void setResponse() {
	}
	
	@Before(value = "setResponse()")
	public void logSetResponse(JoinPoint joinPoint) throws Throwable {
	    LOGGER.info("logSetResponse - Crnk is done. About to serialize. Links ");

	}

}
