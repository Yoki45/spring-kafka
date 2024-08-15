package com.spring.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.kafka.dto.LibraryEvent;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface ILibraryEventProducerService {

    CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException;

    void handleFailure(Integer key, String value, Throwable ex);

    void handleSuccess(Integer key, String value, SendResult<Integer, String> result);
}
