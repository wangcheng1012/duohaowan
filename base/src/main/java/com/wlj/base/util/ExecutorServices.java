package com.wlj.base.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * @created 2012-3-21
 */
public class ExecutorServices {

	private static ExecutorService pool;

	public static ExecutorService getExecutorService() {

		if (pool == null) {

			pool = Executors.newFixedThreadPool(10);
		}
		return pool;
	}
}
