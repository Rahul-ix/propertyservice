package com.propertyservice.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.trial-number}")
    private String fromNumber;

    public void sendSms(String to, String message) {
        Message.creator(
                new PhoneNumber(to), // To number
                new PhoneNumber(fromNumber), // From Twilio number
                message
        ).create();
    }
}
