package it.discovery.order.controller;

import it.discovery.order.model.Order;
import it.discovery.order.repository.OrderRepository;

public class OrderController {
	
	private OrderRepository orderRepository;
	
	public void makeOrder(int bookId) {
		Order order = new Order();
		//TODO implement
		orderRepository.save(order);
	}

}
