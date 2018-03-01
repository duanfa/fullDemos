package com.kafka;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

public class KafkaConsumerRunner implements Runnable {
	public static Integer lock = 0;
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private final KafkaConsumer consumer;
	long i = 0;

	public KafkaConsumerRunner(KafkaConsumer consumer) {
		this.consumer = consumer;

	}

	public void run() {
		try {
			consumer.subscribe(Arrays.asList("PRMessage"));
			while (!closed.get()) {
				ConsumerRecords<String, String> records;
				synchronized (lock) {
					records = consumer.poll(10000);
				}
				for (ConsumerRecord<String, String> record : records) {
					if(record.value().indexOf("reciver=jcthulhu--")>-1){
						
						System.out.println(Thread.currentThread() + " partition:" + record.partition() + " offset:" + record.offset() + " key:" + record.key() + " value:" + record.value());
					}
					/*while (i++ % 5 == 0) {
						System.out.println("-------------------------------------------");
						try {
							Thread.sleep(3000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}*/
				}
			}
		} catch (WakeupException e) {
			if (!closed.get())
				throw e;
		} finally {
			consumer.close();
		}
	}

	// Shutdown hook which can be called from a separate thread
	public void shutdown() {
		closed.set(true);
		consumer.wakeup();
	}
}