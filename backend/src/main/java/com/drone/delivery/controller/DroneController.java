package com.drone.delivery.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drone.delivery.dto.DroneStatusDTO;
import com.drone.delivery.model.Drone;
import com.drone.delivery.model.enums.StatusDrone;
import com.drone.delivery.model.enums.StatusPedido;
import com.drone.delivery.model.enums.StatusVoo;
import com.drone.delivery.service.DroneService;
import com.drone.delivery.service.VooService;
import com.drone.delivery.service.PedidoService;

@RestController
@RequestMapping("/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;
    
    @Autowired
    private VooService vooService;
    
    @Autowired
    private PedidoService pedidoService;

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

    @PutMapping("/{droneId}/simular-entrega")
    public ResponseEntity<String> simularEntrega(@PathVariable Long droneId) {
        try {
            // Buscar voos ativos do drone
            var voosAtivos = vooService.buscarVoosPorDrone(droneId).stream()
                    .filter(voo -> voo.getStatus() != StatusVoo.CONCLUIDO)
                    .toList();

            if (voosAtivos.isEmpty()) {
                return ResponseEntity.badRequest().body("Drone não possui voos ativos");
            }

            var voo = voosAtivos.get(0);

            // Simular sequência: PLANEJADO → EM_ANDAMENTO → CONCLUIDO
            if (voo.getStatus() == StatusVoo.PLANEJADO) {
                vooService.iniciarVoo(voo.getId());
                droneService.atualizarStatus(droneId, StatusDrone.EM_VOO);
                return ResponseEntity.ok("Voo iniciado! Drone em voo.");
            } else {
                vooService.finalizarVoo(voo.getId());
                voo.getPedidos()
                        .forEach(pedido -> pedidoService.atualizarStatus(pedido.getId(), StatusPedido.ENTREGUE));
                droneService.retornarParaBase(droneId);
                return ResponseEntity.ok("Entrega finalizada! Drone retornado à base.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}