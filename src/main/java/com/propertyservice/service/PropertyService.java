package com.propertyservice.service;

import com.propertyservice.dto.PropertyDto;
import com.propertyservice.entity.Area;
import com.propertyservice.entity.City;
import com.propertyservice.entity.Property;
import com.propertyservice.entity.State;
import com.propertyservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PropertyService {

//    private final PropertyController propertyController;

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
        Property save = propertyRepository.save(property);


        return save;
    }
}


