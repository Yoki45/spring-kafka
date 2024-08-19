package com.kafka.libraryconsumer.service.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ILibraryEventService {

    void processLibraryEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException;
}
