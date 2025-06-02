package com.propertyservice.repository;

import com.propertyservice.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Area findByName(String area);
}