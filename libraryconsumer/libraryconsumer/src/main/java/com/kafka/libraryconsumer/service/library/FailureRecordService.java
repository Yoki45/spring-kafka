package com.kafka.libraryconsumer.service.library;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface FailureRecordService  {

    void saveFailedRecord(ConsumerRecord<Integer, String> record, Exception exception, String recordStatus);
}
