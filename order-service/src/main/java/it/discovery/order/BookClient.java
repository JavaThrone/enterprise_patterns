package it.discovery.order;

import it.discovery.order.dto.BookDTO;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BookClient {

    private final String baseUrl;

    private final RestTemplate restTemplate;

    public BookClient(Environment env) {
        baseUrl = env.getProperty("book.service.url", "http://localhost:8080/");
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<BookDTO> findById(int id) {
        return restTemplate.getForEntity(baseUrl + "/" + id, BookDTO.class);
    }
}
