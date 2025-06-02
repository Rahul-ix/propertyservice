package com.propertyservice.repository;

import com.propertyservice.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {
}