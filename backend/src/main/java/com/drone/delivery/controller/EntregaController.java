package com.drone.delivery.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drone.delivery.dto.PedidoResumoDTO;
import com.drone.delivery.dto.RotaResponseDTO;
import com.drone.delivery.model.Voo;
import com.drone.delivery.service.VooService;

@RestController
@RequestMapping("/entregas")
public class EntregaController {
    
    @Autowired
    private VooService vooService;
    
    @GetMapping("/rota")
    public ResponseEntity<List<RotaResponseDTO>> buscarRotasPlanejadas() {
        try {
            List<Voo> voosAtivos = vooService.buscarRotasAtivas();
            
            List<RotaResponseDTO> rotas = voosAtivos.stream()
                .map(this::converterParaRotaDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(rotas);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    private RotaResponseDTO converterParaRotaDTO(Voo voo) {
        RotaResponseDTO rota = new RotaResponseDTO();
        rota.setVooId(voo.getId());
        rota.setDroneId(voo.getDrone().getId());
        rota.setDroneNome(voo.getDrone().getNome());
        rota.setStatusVoo(voo.getStatus());
        rota.setDistanciaTotal(voo.getDistanciaTotal());
        rota.setPesoTotal(voo.getPesoTotal());
        rota.setInicioVoo(voo.getInicioVoo());
        
        // Converter pedidos para DTOs
        List<PedidoResumoDTO> pedidos = voo.getPedidos().stream()
            .map(pedido -> {
                PedidoResumoDTO dto = new PedidoResumoDTO();
                dto.setId(pedido.getId());
                dto.setClienteX(pedido.getLocalizacaoCliente().getX());
                dto.setClienteY(pedido.getLocalizacaoCliente().getY());
                dto.setPeso(pedido.getPeso());
                dto.setPrioridade(pedido.getPrioridade());
                dto.setStatus(pedido.getStatus());
                return dto;
            })
            .collect(Collectors.toList());
        
        rota.setPedidos(pedidos);
        
        return rota;
    }
}