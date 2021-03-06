package com.eai.FileTransfer.configuration;

import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import ch.qos.logback.classic.Logger;

/**********************************
 * 
 * @author iwj
 * AsyncService 동작 오류가 발생했을 때의 Exception Handler 처리 
 *
 ***********************************/
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler{

	private static Logger log = (Logger) LoggerFactory.getLogger(AsyncExceptionHandler.class);
	
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		log.info("Thread Error Exception");
		log.info("exception Message :: " + throwable.getMessage());
		log.info("method name :: " + method.getName());
			for(Object param : obj) {
				System.out.println("param Val ::: " + param);
			}
	}
}