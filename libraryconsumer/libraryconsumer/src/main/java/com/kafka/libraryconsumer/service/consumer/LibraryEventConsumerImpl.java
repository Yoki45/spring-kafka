package com.kafka.libraryconsumer.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.libraryconsumer.service.library.ILibraryEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventConsumerImpl implements ILibraryEventConsumer {

    private final ILibraryEventService libraryEventService;


    @Override
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        Integer key = consumerRecord.key();
        String value = consumerRecord.value();

        log.info("Processing record with key: {} and value: {}", key, value);
        log.info("ConsumerRecord : {} ", consumerRecord);
        libraryEventService.processLibraryEvent(consumerRecord);
    }
}
