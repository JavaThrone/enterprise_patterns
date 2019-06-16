package it.discovery.order.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.server.ServerDefinition;
import it.discovery.order.config.CacheConfiguration;
import it.discovery.order.dto.BookDTO;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.NoSuchElementException;

public class BookClient {

    private final RestTemplate restTemplate;

    private final LoadBalancer loadBalancer;

    private Cache<Integer, BookDTO> cache;

    public BookClient(RestTemplate restTemplate,
                      LoadBalancer loadBalancer, CacheConfiguration cacheConfiguration) {
        this.restTemplate = restTemplate;
        this.loadBalancer = loadBalancer;
        cache = Caffeine.newBuilder()
                .maximumSize(cacheConfiguration.getMaxSize())
                .expireAfterAccess(Duration.ofMillis(cacheConfiguration.getExpiresAfterAccess()))
                .build();
    }

    @Retryable(include = {ResourceAccessException.class, NoSuchElementException.class},
            maxAttemptsExpression = "${retry-config.maxAttempts}")
    public BookDTO findById(int id) {
        return cache.get(id, bookId -> {
            ServerDefinition definition = loadBalancer.chooseServer()
                    .orElseThrow();
            return restTemplate.getForEntity(definition.getUrl() + "/book/" + id, BookDTO.class).getBody();
        });
    }
}
