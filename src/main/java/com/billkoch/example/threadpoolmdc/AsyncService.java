package com.billkoch.example.threadpoolmdc;

import java.util.concurrent.Future;

public interface AsyncService {

	Future<String> doWork();
}
