package com.propertyservice.controller;

import java.time.LocalDate;
import java.util.List;

import com.propertyservice.entity.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.PropertyDto;
import com.propertyservice.entity.RoomAvailability;
import com.propertyservice.entity.Rooms;
import com.propertyservice.repository.RoomAvailabilityRepository;
import com.propertyservice.service.PropertyService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;


    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    @PostMapping(
            value = "/add-property",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  // Ensures the endpoint accepts multipart/form-data
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<APIResponse> addProperty(
            @RequestParam("property") String propertyJson,  // Use RequestParam to get the property as a raw JSON string
            @RequestParam("files") MultipartFile[] files) {  // Use RequestParam to handle files
        logger.info("propertyJson: {}", propertyJson);
        logger.info("files count: {}", files != null ? files.length : 0);

        if (propertyJson == null || propertyJson.trim().isEmpty()) {
            logger.error("Property JSON is missing or empty");
            return ResponseEntity.badRequest().body(new APIResponse<>("Property JSON is missing or empty", 400, null));
        }

        // Parse the property JSON into PropertyDto
        ObjectMapper objectMapper = new ObjectMapper();
        PropertyDto dto = null;
        try {
            dto = objectMapper.readValue(propertyJson, PropertyDto.class);  // Convert JSON string to PropertyDto
        } catch (JsonProcessingException e) {
            logger.error("Error parsing property JSON", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Handle bad JSON
        }

        // Process the property and files
        Property property = propertyService.addProperty(dto, files);

        // Create response object
        APIResponse<Property> response = new APIResponse<>();
        response.setMessage("Property added");
        response.setStatus(201);
        response.setData(property);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}