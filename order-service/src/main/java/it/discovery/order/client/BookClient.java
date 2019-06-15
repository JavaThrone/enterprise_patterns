package it.discovery.order.client;

import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.server.ServerDefinition;
import it.discovery.order.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class BookClient {

    private final RestTemplate restTemplate;

    private final LoadBalancer loadBalancer;

    public ResponseEntity<BookDTO> findById(int id) {
        ServerDefinition definition = loadBalancer.chooseServer()
                .orElseThrow();
        return restTemplate.getForEntity(definition.getUrl() + "/book/" + id, BookDTO.class);
    }
}
