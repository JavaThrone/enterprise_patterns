package it.discovery.order.client;

import it.discovery.order.dto.BookDTO;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public class BookClient {

    private final RestTemplate restTemplate;

    private final String loadBalancerUrl;

    public BookClient(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        loadBalancerUrl = env.getProperty("load.balancer.book");
    }

    public BookDTO findById(int id) {
        return restTemplate.getForEntity(loadBalancerUrl + "/book/" + id, BookDTO.class).getBody();

    }
}
