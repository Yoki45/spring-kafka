package com.kafka.libraryconsumer.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "failure_record")
public class FailureRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer bookId;
    private String topic;
    private Integer key_value;
    private String errorRecord;
    @Column(name = "partitions")
    private Integer partition;
    private Long offset_value;
    private String exception;
    private String status;

}
