package com.propertyservice.service;

import com.propertyservice.constants.AppConstants;
import com.propertyservice.dto.EmailRequest;
import com.propertyservice.dto.PropertyDto;
import com.propertyservice.entity.Area;
import com.propertyservice.entity.City;
import com.propertyservice.entity.Property;
import com.propertyservice.entity.State;
import com.propertyservice.repository.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PropertyService {

    private static final Log log = LogFactory.getLog(PropertyService.class);
    //    private final PropertyController propertyController;
    @Autowired
    private KafkaTemplate<String, EmailRequest> kafkaTemplate;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private RoomsRepository roomRepository;

    @Autowired
    private RoomAvailabilityRepository availabilityRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SmsService smsService;


    public Property addProperty(PropertyDto dto, MultipartFile[] files) {
        String  cityName = dto.getCity();
        City city = cityRepository.findByName(cityName);

        String area = dto.getArea();
        Area areaName = areaRepository.findByName(area);

        String state = dto.getState();
        State stateName = stateRepository.findByName(state);

        Property property = new Property();
        property.setName(dto.getName());
        property.setNumberOfBathrooms(dto.getNumberOfBathrooms());
        property.setNumberOfBeds(dto.getNumberOfBeds());
        property.setNumberOfRooms(dto.getNumberOfRooms());
        property.setNumberOfGuestAllowed(dto.getNumberOfGuestAllowed());
        property.setArea(areaName);
        property.setCity(city);
        property.setState(stateName);

        EmailRequest emailRequest = new EmailRequest();
        SimpleMailMessage message = new SimpleMailMessage();

        emailRequest.setTo("rahulkalal676@gmail.com");
        emailRequest.setSubject("Verifying property");
        emailRequest.setBody(
                "Dear User,\n\n" +
                        "The property \"" + property.getName() + "\" has been successfully added to our listings.\n" +
                        "Location: " + cityName + ", " + state + "\n\n" +
                        "Thank you for using our platform.\n\n" +
                        "Best regards,\n" +
                        "Property Management Team"
        );

        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());

        javaMailSender.send(message);
        SmsService smsService1 = new SmsService();
        String to = "+917349082644";
        String smsMessage = "Dear User, your property \"" + property.getName() + "\" has been successfully listed in " + cityName + ", " + state + ". Thank you for choosing us.";
        smsService.sendSms(to, smsMessage);
        kafkaTemplate.send(AppConstants.TOPIC, emailRequest.getTo(), emailRequest);

        log.info("Msg Published To Kafka Topic");
        Property save = propertyRepository.save(property);


        return save;
    }
}


