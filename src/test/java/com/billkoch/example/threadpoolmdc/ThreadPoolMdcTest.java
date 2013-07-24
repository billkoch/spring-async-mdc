package com.billkoch.example.threadpoolmdc;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/threadpool-context.xml")
public class ThreadPoolMdcTest {

	@Autowired
	private AsyncService uut;
	
	@Test
	public void shouldMaintainMDCContextInPooledThreads() throws InterruptedException, ExecutionException {
		Collection<FutureAndExpectedTrackingId> futuresAndExpectedTrackingIds = new ArrayList<FutureAndExpectedTrackingId>();
		
		for(Integer i=1; i <= 100; i++ ) {
			String expectedTrackingId = i.toString();
			MDC.put("trackingId", expectedTrackingId);

			futuresAndExpectedTrackingIds.add(new FutureAndExpectedTrackingId(uut.doWork(), expectedTrackingId));
		}
		
		for(FutureAndExpectedTrackingId futureAndExpectedTrackingId : futuresAndExpectedTrackingIds) {
			assertThat(futureAndExpectedTrackingId.getFuture().get(), is(futureAndExpectedTrackingId.getExpectedTrackingId()));
		}
	}
	
	private class FutureAndExpectedTrackingId {
		
		private final Future<String> future;

		private final String expectedTrackingId;

		public FutureAndExpectedTrackingId(Future<String> future, String expectedTrackingId) {
			this.future = future;
			this.expectedTrackingId = expectedTrackingId;
		}

		public Future<String> getFuture() {
			return future;
		}

		public String getExpectedTrackingId() {
			return expectedTrackingId;
		}
	}
}
