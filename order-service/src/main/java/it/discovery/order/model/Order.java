package it.discovery.order.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "ORDERS")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int bookId;
	
	private double price;
	
	private LocalDateTime created;

	@PrePersist
	public void onCreated() {
		created = LocalDateTime.now();
	}

}
