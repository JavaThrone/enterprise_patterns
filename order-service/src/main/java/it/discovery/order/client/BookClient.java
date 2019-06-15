package it.discovery.order.client;

import it.discovery.order.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class BookClient {

    private final RestTemplate restTemplate;

    public ResponseEntity<BookDTO> findById(int id) {
        return restTemplate.getForEntity("/" + id, BookDTO.class);
    }
}
