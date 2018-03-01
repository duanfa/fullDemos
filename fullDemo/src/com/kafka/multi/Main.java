package com.kafka.multi;
public class Main {

    public static void main(String[] args) {
        String brokerList = "192.168.0.251:19093,192.168.0.250:19093,192.168.0.253:19093";
        String groupId = "group2";
        String topic = "PRMessage";
        int workerNum = 5;

        ConsumerHandler consumers = new ConsumerHandler(brokerList, groupId, topic);
        consumers.execute(workerNum);
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException ignored) {}
        consumers.shutdown();
    }
}