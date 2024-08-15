package com.spring.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.kafka.dto.LibraryEvent;
import com.spring.kafka.dto.LibraryEventType;
import com.spring.kafka.service.ILibraryEventProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LibraryEventController {

    private final ILibraryEventProducerService iLibraryEventProducerService;


    @PostMapping("v1/event")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@Valid @RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        log.info("Posting library event: {}", libraryEvent);
        iLibraryEventProducerService.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }


    @PutMapping("/v1/event")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {


        ResponseEntity<String> BAD_REQUEST = validateLibraryEvent(libraryEvent);
        if (BAD_REQUEST != null) return BAD_REQUEST;

        iLibraryEventProducerService.sendLibraryEvent(libraryEvent);
        log.info("after produce call");
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

    private static ResponseEntity<String> validateLibraryEvent(LibraryEvent libraryEvent) {
        if (libraryEvent.libraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }

        if (!LibraryEventType.UPDATE.equals(libraryEvent.libraryEventType()))  {
            log.info("Inside the if block");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE event type is supported");
        }
        return null;
    }


}
