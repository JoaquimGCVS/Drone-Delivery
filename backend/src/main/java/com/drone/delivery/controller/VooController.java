package com.drone.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drone.delivery.service.DroneService;
import com.drone.delivery.service.VooService;
import com.drone.delivery.service.PedidoService;
import com.drone.delivery.model.Voo;
import com.drone.delivery.model.enums.StatusPedido;

@RestController
@RequestMapping("/voos")
public class VooController {
    @Autowired
    private com.drone.delivery.service.OptimizacaoService optimizacaoService;
    
    @Autowired
    private VooService vooService;
    
    @Autowired
    private DroneService droneService;
    
    @Autowired
    private PedidoService pedidoService;


    // Criar voo a partir de um drone livre e pedidos pendentes
    @PostMapping("/criar")
    public ResponseEntity<?> criarVoo(@RequestBody com.drone.delivery.dto.CriarVooPorDroneRequestDTO request) {
        try {
            // Buscar drone


            var drone = droneService.buscarPorId(request.getDroneId());
            if (drone == null) {
                return ResponseEntity.badRequest().body("Drone não encontrado");
            }

            // Verificar se drone está livre (status IDLE)
            if (drone.getStatus() != com.drone.delivery.model.enums.StatusDrone.IDLE) {
                return ResponseEntity.badRequest().body("Drone não está livre para criar voo");
            }

            // Buscar pedidos pendentes ordenados
            var pedidosPendentes = pedidoService.buscarPedidosPendentesOrdenados();
            if (pedidosPendentes.isEmpty()) {
                return ResponseEntity.badRequest().body("Não há pedidos pendentes para alocar");
            }

            // Alocar pedidos de forma otimizada para este drone
            var grupoPedidos = optimizacaoService.alocarPedidosParaDrone(drone, pedidosPendentes);
            if (grupoPedidos.isEmpty()) {
                return ResponseEntity.badRequest().body("Nenhum pedido pode ser alocado para este drone");
            }

            // Criar voo
            Voo voo = vooService.criarVoo(drone, grupoPedidos);
            return ResponseEntity.ok(voo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // Iniciar voo
    @PutMapping("/{vooId}/iniciar")
    public ResponseEntity<String> iniciarVoo(@PathVariable Long vooId) {
        try {
            vooService.iniciarVoo(vooId);
            return ResponseEntity.ok("Voo iniciado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
    
    // Finalizar voo
    @PutMapping("/{vooId}/finalizar")
    public ResponseEntity<String> finalizarVoo(@PathVariable Long vooId) {
        try {
            // Buscar voo primeiro
            var voos = vooService.buscarTodosVoos();
            var voo = voos.stream()
                .filter(v -> v.getId().equals(vooId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Voo não encontrado"));
            
            // Finalizar voo
            vooService.finalizarVoo(vooId);
            
            // Marcar pedidos como entregues
            voo.getPedidos().forEach(pedido -> 
                pedidoService.atualizarStatus(pedido.getId(), StatusPedido.ENTREGUE)
            );
            
            // Retornar drone para base
            droneService.retornarParaBase(voo.getDrone().getId());

            // Alocar pedidos automaticamente após finalizar voo
            optimizacaoService.alocarPedidosOtimizado();
            
            return ResponseEntity.ok("Voo finalizado! Pedidos entregues e drone retornado à base.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}