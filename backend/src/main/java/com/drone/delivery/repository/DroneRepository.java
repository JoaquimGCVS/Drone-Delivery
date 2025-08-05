package com.drone.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drone.delivery.model.Drone;
import com.drone.delivery.model.enums.StatusDrone;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    
    List<Drone> findByStatus(StatusDrone status);
}