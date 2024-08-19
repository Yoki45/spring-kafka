package com.kafka.libraryconsumer.service.library;

import com.kafka.libraryconsumer.models.FailureRecord;
import com.kafka.libraryconsumer.repository.FailureRecordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FailureRecordServiceImpl implements FailureRecordService {

    private final FailureRecordRepository failureRecordRepository;


    @Override
    public void saveFailedRecord(ConsumerRecord<Integer, String> record, Exception exception, String recordStatus) {

        var failureRecord = new FailureRecord(null,record.topic(), record.key().intValue(),  record.value(), record.partition(),record.offset(),
                exception.getCause().getMessage(),
                recordStatus);

        failureRecordRepository.save(failureRecord);
    }
}
