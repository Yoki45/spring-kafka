package com.kafka.libraryconsumer.repository;

import com.kafka.libraryconsumer.models.LibraryEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventRepository extends CrudRepository<LibraryEvent, Long> {
}
