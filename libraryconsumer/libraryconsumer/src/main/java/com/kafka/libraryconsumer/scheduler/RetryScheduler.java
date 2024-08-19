package com.kafka.libraryconsumer.scheduler;

import com.kafka.libraryconsumer.config.LibraryEventConsumerConfig;
import com.kafka.libraryconsumer.models.FailureRecord;
import com.kafka.libraryconsumer.repository.FailureRecordRepository;
import com.kafka.libraryconsumer.service.library.ILibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryScheduler {


    @Autowired
    ILibraryEventService libraryEventsService;

    @Autowired
    FailureRecordRepository failureRecordRepository;


    @Scheduled(fixedRate = 10000 )
    public void retryFailedRecords(){

        log.info("Retrying Failed Records Started!");
        var status = LibraryEventConsumerConfig.RETRY;
        failureRecordRepository.findAllByStatus(status)
                .forEach(failureRecord -> {
                    try {
                        var consumerRecord = buildConsumerRecord(failureRecord);
                        libraryEventsService.processLibraryEvent(consumerRecord);
                        failureRecord.setStatus(LibraryEventConsumerConfig.SUCCESS);
                    } catch (Exception e){
                        log.error("Exception in retryFailedRecords : ", e);
                    }

                });

    }

    private ConsumerRecord<Integer, String> buildConsumerRecord(FailureRecord failureRecord) {

        return new ConsumerRecord<>(failureRecord.getTopic(),
                failureRecord.getPartition(), failureRecord.getOffset_value(), failureRecord.getKey_value(),
                failureRecord.getErrorRecord());

    }
}
