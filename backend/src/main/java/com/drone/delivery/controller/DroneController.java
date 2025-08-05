package com.drone.delivery.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drone.delivery.dto.DroneStatusDTO;
import com.drone.delivery.model.Drone;
import com.drone.delivery.service.DroneService;

@RestController
@RequestMapping("/drones")
public class DroneController {
    
    @Autowired
    private DroneService droneService;
    
    @GetMapping("/status")
    public ResponseEntity<List<DroneStatusDTO>> consultarStatusDrones() {
        try {
            List<Drone> drones = droneService.buscarTodosDrones();
            
            List<DroneStatusDTO> status = drones.stream()
                .map(this::converterParaStatusDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(status);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    private DroneStatusDTO converterParaStatusDTO(Drone drone) {
        DroneStatusDTO status = new DroneStatusDTO();
        status.setId(drone.getId());
        status.setNome(drone.getNome());
        status.setStatus(drone.getStatus());
        status.setCapacidadeMaxima(drone.getCapacidadeMaxima());
        status.setAlcanceMaximo(drone.getAlcanceMaximo());
        
        if (drone.getPosicaoAtual() != null) {
            status.setPosicaoX(drone.getPosicaoAtual().getX());
            status.setPosicaoY(drone.getPosicaoAtual().getY());
        }
        
        return status;
    }
}