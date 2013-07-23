package com.billkoch.example.threadpoolmdc;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/threadpool-context.xml")
public class ThreadPoolMdcTest {

	private static final Logger LOG = LoggerFactory.getLogger(ThreadPoolMdcTest.class);
	
	@Autowired
	private AsyncService uut;
	
	@Test
	public void basic() {
		for(int i=1; i <= 100; i++ ) {
			MDC.put("trackingId", "" + i);
			LOG.trace("About to do work");
			try {
				uut.doWork().get();
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("An error occurred", e);
			}
		}
	}
}
