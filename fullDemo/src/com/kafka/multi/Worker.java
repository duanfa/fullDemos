package com.kafka.multi;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class Worker implements Runnable {

	private ConsumerRecord<String, String> record;

	public Worker(ConsumerRecord record) {
		this.record = record;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread() + " partition:" + record.partition() + " offset:" + record.offset() + " key:" + record.key() + " value:" + record.value());

	}
}