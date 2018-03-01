package com.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaCoustomerMain {
	public static void main(String[] args) throws InterruptedException {

		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.0.251:19093,192.168.0.250:19093,192.168.0.253:19093");
		props.put("group.id", "test6");
		props.put("enable.auto.commit", "true");
		// props.put("auto.offset.reset", "earliest");
		props.put("auto.offset.reset", "latest");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.RangeAssignor");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

		new Thread(new KafkaConsumerRunner(consumer)).start();
	}

}
