package com.propertyservice.service;

import com.propertyservice.constants.AppConstants;
import com.propertyservice.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private KafkaTemplate<String, EmailRequest> kafkaTemplate;

    public String addMsg(EmailRequest emailRequest) {

        // sends msg to kafka topic
        kafkaTemplate.send(AppConstants.TOPIC,emailRequest.getTo(), emailRequest);

        return "Msg Published To Kafka Topic";
    }
}
