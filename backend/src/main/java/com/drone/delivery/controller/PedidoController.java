package com.drone.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drone.delivery.dto.PedidoRequestDTO;
import com.drone.delivery.dto.PedidoResponseDTO;
import com.drone.delivery.model.Pedido;
import com.drone.delivery.service.OptimizacaoService;
import com.drone.delivery.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private OptimizacaoService optimizacaoService;
    
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO request) {
        try {
            // Criar pedido
            Pedido pedido = pedidoService.criarPedido(
                request.getClienteX(),
                request.getClienteY(), 
                request.getPeso(),
                request.getPrioridade()
            );
            
            // Executar otimização automática após criar pedido
            optimizacaoService.alocarPedidosOtimizado();
            
            // Preparar resposta
            PedidoResponseDTO response = new PedidoResponseDTO();
            response.setId(pedido.getId());
            response.setClienteX(pedido.getLocalizacaoCliente().getX());
            response.setClienteY(pedido.getLocalizacaoCliente().getY());
            response.setPeso(pedido.getPeso());
            response.setPrioridade(pedido.getPrioridade());
            response.setStatus(pedido.getStatus());
            response.setPontuacaoPrioridade(pedido.getPontuacaoPrioridade());
            response.setDataCriacao(pedido.getDataCriacao());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}