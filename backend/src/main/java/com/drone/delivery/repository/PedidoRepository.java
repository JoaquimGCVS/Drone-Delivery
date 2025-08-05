package com.drone.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drone.delivery.model.Pedido;
import com.drone.delivery.model.enums.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByStatus(StatusPedido status);
    
    List<Pedido> findByStatusOrderByPontuacaoPrioridadeAsc(StatusPedido status);
}