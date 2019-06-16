package it.discovery.order.client;

import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.server.ServerDefinition;
import it.discovery.order.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class BookClient {

    private final RestTemplate restTemplate;

    private final LoadBalancer loadBalancer;

    @Retryable(include = {ResourceAccessException.class, NoSuchElementException.class}, maxAttempts = 3)
    public ResponseEntity<BookDTO> findById(int id) {
        ServerDefinition definition = loadBalancer.chooseServer()
                .orElseThrow();
        return restTemplate.getForEntity(definition.getUrl() + "/book/" + id, BookDTO.class);
    }
}
