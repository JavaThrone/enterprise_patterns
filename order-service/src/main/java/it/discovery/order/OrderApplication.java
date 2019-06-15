package it.discovery.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories("it.discovery.order.repository")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public BookClient bookClient(RestTemplate restTemplate) {
        return new BookClient(restTemplate);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder
                                     restTemplateBuilder,
                                     Environment env) {
        String baseUrl = env
                .getProperty("book.service.url",
                        "http://localhost:8080/book");

        return restTemplateBuilder
                .rootUri(baseUrl)
                .build();
    }
}
