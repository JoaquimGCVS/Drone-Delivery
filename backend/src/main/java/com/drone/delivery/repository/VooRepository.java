package com.drone.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drone.delivery.model.Voo;
import com.drone.delivery.model.enums.StatusVoo;

@Repository
public interface VooRepository extends JpaRepository<Voo, Long> {
    
    List<Voo> findByStatus(StatusVoo status);
    
    List<Voo> findByStatusIn(List<StatusVoo> status);
    
    List<Voo> findByDroneId(Long droneId);
}