package com.billkoch.example.threadpoolmdc;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class MyAsyncService implements AsyncService {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MyAsyncService.class);
	
	@Override
	@Async("threadPoolExecutor")
	public Future<String> doWork() {
		LOG.debug("Doing work");
		LOG.debug("Finished doing work");
		return new AsyncResult<String>("some result");
	}
}
