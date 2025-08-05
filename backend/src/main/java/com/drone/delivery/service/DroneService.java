package com.drone.delivery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.delivery.model.Coordenada;
import com.drone.delivery.model.Drone;
import com.drone.delivery.model.enums.StatusDrone;
import com.drone.delivery.repository.DroneRepository;

@Service
public class DroneService {
    
    @Autowired
    private DroneRepository droneRepository;
    
    // Buscar drones disponíveis (status = IDLE)
    public List<Drone> buscarDronesDisponiveis() {
        return droneRepository.findByStatus(StatusDrone.IDLE);
    }
    
    // Atualizar status do drone
    public void atualizarStatus(Long droneId, StatusDrone novoStatus) {
        Drone drone = droneRepository.findById(droneId)
            .orElseThrow(() -> new RuntimeException("Drone não encontrado"));
        drone.setStatus(novoStatus);
        droneRepository.save(drone);
    }
    
    // Buscar todos os drones com seus status
    public List<Drone> buscarTodosDrones() {
        return droneRepository.findAll();
    }
    
    // Inicializar drones na base (se necessário)
    public void inicializarDrones() {
        // Verificar se já existem drones
        long count = droneRepository.count();
        
        if (count == 0) {
            // Criar drones iniciais (exemplo: 5 drones)
            for (int i = 1; i <= 5; i++) {
                Drone drone = new Drone();
                drone.setNome("Drone-" + i);
                drone.setStatus(StatusDrone.IDLE);
                drone.setCapacidadeMaxima(10.0); // kg
                drone.setAlcanceMaximo(20.0); // km
                drone.setPosicaoAtual(Coordenada.base()); // (50, 50)
                
                droneRepository.save(drone);
            }
        }
    }
    
    // Buscar drone por ID
    public Drone buscarPorId(Long droneId) {
        return droneRepository.findById(droneId)
            .orElseThrow(() -> new RuntimeException("Drone não encontrado"));
    }
    
    // Verificar se drone está disponível para nova missão
    public boolean isDroneDisponivel(Long droneId) {
        Drone drone = buscarPorId(droneId);
        return drone.getStatus() == StatusDrone.IDLE;
    }
    
    // Atualizar posição atual do drone
    public void atualizarPosicao(Long droneId, Coordenada novaPosicao) {
        Drone drone = buscarPorId(droneId);
        drone.setPosicaoAtual(novaPosicao);
        droneRepository.save(drone);
    }
    
    // Retornar drone para a base
    public void retornarParaBase(Long droneId) {
        Drone drone = buscarPorId(droneId);
        drone.setPosicaoAtual(Coordenada.base());
        drone.setStatus(StatusDrone.IDLE);
        droneRepository.save(drone);
    }
    
    // Buscar drones por status
    public List<Drone> buscarPorStatus(StatusDrone status) {
        return droneRepository.findByStatus(status);
    }
}