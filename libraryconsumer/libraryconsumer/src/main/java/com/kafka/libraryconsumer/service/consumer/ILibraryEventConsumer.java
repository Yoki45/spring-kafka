package com.kafka.libraryconsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;


public interface ILibraryEventConsumer {

    @KafkaListener(
            topics = {"library-events"}
            , autoStartup = "${libraryListener.startup:true}"
            , groupId = "library-events-listener-group")
    void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException;

}
