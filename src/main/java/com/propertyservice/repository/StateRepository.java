package com.propertyservice.repository;

import com.propertyservice.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
    State findByName(String state);
}