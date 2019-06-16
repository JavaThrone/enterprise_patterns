package it.discovery.balancer.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.config.CacheConfiguration;
import it.discovery.balancer.dto.BookDTO;
import it.discovery.balancer.server.ServerDefinition;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("book")
public class BookLoadBalancer {

    private final LoadBalancer loadBalancer;

    private Cache<Integer, BookDTO> cache;

    private final RestTemplate restTemplate;

    public BookLoadBalancer(LoadBalancer loadBalancer, CacheConfiguration cacheConfiguration, RestTemplate restTemplate) {
        this.loadBalancer = loadBalancer;
        cache = Caffeine.newBuilder()
                .maximumSize(cacheConfiguration.getMaxSize())
                .expireAfterAccess(Duration.ofMillis(cacheConfiguration.getExpiresAfterAccess()))
                .build();
        this.restTemplate = restTemplate;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BookDTO> getBooks() {
        ServerDefinition definition = loadBalancer.chooseServer()
                .orElseThrow();
        return restTemplate.getForEntity(definition.getUrl() + "/book", List.class).getBody();
    }

    @GetMapping("{id}")
    public BookDTO findBookById(@PathVariable int id) {
        return cache.get(id, bookId -> {
            ServerDefinition definition = loadBalancer.chooseServer()
                    .orElseThrow();
            return restTemplate.getForEntity(definition.getUrl() + "/book/" + bookId, BookDTO.class).getBody();
        });
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void saveBook(@Valid @RequestBody BookDTO book) {
//        bookRepository.save(book);
//    }
}
