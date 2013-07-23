package com.billkoch.example.threadpoolmdc.spring.extension;

import java.lang.reflect.Field;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;

public class MDCAwareAsyncAnnotationBeanPostProcessor extends AsyncAnnotationBeanPostProcessor {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		try {
			super.setBeanFactory(beanFactory);
			Field executorField = this.getClass().getSuperclass().getDeclaredField("executor");
			executorField.setAccessible(true);
			this.advisor = new MDCAwareAsyncAnnotationAdvisor((Executor) executorField.get(this));
			((MDCAwareAsyncAnnotationAdvisor) this.advisor).setBeanFactory(beanFactory);
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to get declared field 'executor'", e);
		}
	}
}
