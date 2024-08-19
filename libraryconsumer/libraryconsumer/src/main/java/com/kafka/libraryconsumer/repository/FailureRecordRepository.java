package com.kafka.libraryconsumer.repository;


import com.kafka.libraryconsumer.models.FailureRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailureRecordRepository extends CrudRepository<FailureRecord,Integer> {

    List<FailureRecord> findAllByStatus(String status);
}
