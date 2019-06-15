package it.discovery.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.discovery.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
}
