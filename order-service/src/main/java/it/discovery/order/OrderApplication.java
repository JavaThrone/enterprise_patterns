package it.discovery.order;

import it.discovery.order.client.BookClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public BookClient bookClient(RestTemplate restTemplate, Environment env) {
        return new BookClient(restTemplate, env);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
