package com.kafka.libraryconsumer.config;

import com.kafka.libraryconsumer.service.library.FailureRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;



@Configuration
@Slf4j
//@EnableKafka
public class LibraryEventConsumerConfig {

    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    FailureRecordService failureRecordService;

    public static final String RETRY = "RETRY";
    public static final String SUCCESS = "SUCCESS";



    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory
                .getIfAvailable(() -> new DefaultKafkaConsumerFactory<>(this.kafkaProperties.buildConsumerProperties())));
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }


    public DefaultErrorHandler errorHandler() {

        var exceptiopnToIgnorelist = List.of(
                IllegalArgumentException.class
        );


        var fixedBackOff = new FixedBackOff(1000L, 2L);

        /**
         * Error Handler with the BackOff, Exceptions to Ignore, RetryListener
         */

        var defaultErrorHandler = new DefaultErrorHandler(
                consumerRecordRecoverer,
                fixedBackOff
        );

        exceptiopnToIgnorelist.forEach(defaultErrorHandler::addNotRetryableExceptions);



        defaultErrorHandler.setRetryListeners(
                (record, ex, deliveryAttempt) ->
                        log.info("Failed Record in Retry Listener  exception : {} , deliveryAttempt : {} ", ex.getMessage(), deliveryAttempt)
        );



        return defaultErrorHandler;
    }





    ConsumerRecordRecoverer consumerRecordRecoverer = (record, exception) -> {
        log.error("Exception is : {} Failed Record : {} ", exception, record);
        if (exception.getCause() instanceof RecoverableDataAccessException) {
            log.info("Inside the recoverable logic");

            failureRecordService.saveFailedRecord((ConsumerRecord<Integer, String>) record, exception, RETRY);

        } else {

            log.info("Inside the non recoverable logic and skipping the record : {}", record);

        }
    };


}
