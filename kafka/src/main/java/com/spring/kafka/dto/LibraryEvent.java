package com.spring.kafka.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent(Integer libraryEventId,
                           LiblaryEventType libraryEventType,
                           @NotNull
                           @Valid
                           Book book) {
}
