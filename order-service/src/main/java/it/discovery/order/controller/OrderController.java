package it.discovery.order.controller;

import it.discovery.order.client.BookClient;
import it.discovery.order.model.Order;
import it.discovery.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderRepository orderRepository;

	private final BookClient bookClient;

	@PostMapping("{bookId}")
	public Order makeOrder(@PathVariable int bookId) {
		Order order = new Order();
		order.setBookId(bookId);
		order.setPrice(bookClient.findById(bookId).getBody().getPrice());
		return orderRepository.save(order);
	}

	@GetMapping
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

}
