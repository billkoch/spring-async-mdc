package com.billkoch.example.threadpoolmdc.spring.extension;

import java.util.concurrent.Executor;

import org.aopalliance.aop.Advice;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;

public class MDCAwareAsyncAnnotationAdvisor extends AsyncAnnotationAdvisor {

	private static final long serialVersionUID = 1L;
	
	public MDCAwareAsyncAnnotationAdvisor() {
		super();
	}
	
	public MDCAwareAsyncAnnotationAdvisor(Executor executor) {
		super(executor);
	}
	
	@Override
	protected Advice buildAdvice(Executor executor) {
		return new MDCAwareAnnotationAsyncExecutionInterceptor(executor);
	}
}
