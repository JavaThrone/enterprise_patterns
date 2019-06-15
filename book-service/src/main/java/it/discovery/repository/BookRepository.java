package it.discovery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.discovery.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
