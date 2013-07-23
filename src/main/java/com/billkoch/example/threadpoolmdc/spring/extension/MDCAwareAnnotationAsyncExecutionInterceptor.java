package com.billkoch.example.threadpoolmdc.spring.extension;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class MDCAwareAnnotationAsyncExecutionInterceptor extends AnnotationAsyncExecutionInterceptor {

	public MDCAwareAnnotationAsyncExecutionInterceptor(Executor defaultExecutor) {
		super(defaultExecutor);
	}

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

		Future<?> result = determineAsyncExecutor(specificMethod).submit(buildCallable(invocation));

		if (Future.class.isAssignableFrom(invocation.getMethod().getReturnType())) {
			return result;
		}
		else {
			return null;
		}
	}
	
	protected Callable<?> buildCallable(final MethodInvocation invocation) {
		return new MDCAwareCallable(invocation, MDC.getCopyOfContextMap());
	}
	
	private class MDCAwareCallable implements Callable<Object> {

		private MethodInvocation invocation;

		private Map mdcContextMap;

		public MDCAwareCallable(final MethodInvocation invocation, Map mdcContextMap) {
			this.invocation = invocation;
			this.mdcContextMap = mdcContextMap;
			
		}
		
		@Override
		public Object call() throws Exception {
			try {
				MDC.setContextMap(mdcContextMap);
				Object result = invocation.proceed();
				if (result instanceof Future) {
					return ((Future<?>) result).get();
				}
			}
			catch (Throwable ex) {
				ReflectionUtils.rethrowException(ex);
			} finally {
				MDC.clear();
			}
			return null;
		}
	}
}
