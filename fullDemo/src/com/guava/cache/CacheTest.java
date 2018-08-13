package com.guava.cache;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheTest {
	public static void main(String[] args) throws ExecutionException, InterruptedException {

		final Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(10, TimeUnit.SECONDS).build();

		while (true) {
			String resultVal = cache.get("jerry", new Callable<String>() {
				public String call() {
					String strProValue = "hello " + "jerry" + "!" + new Date();
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(15000);
								cache.put("jerry", "bbb");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
					return "";
				}
			});
			if(StringUtils.isNotBlank(resultVal)) {
				System.out.println("jerry value : " + resultVal);
			}else {
				
			}
			Thread.sleep(2000);

		}

	}
}
