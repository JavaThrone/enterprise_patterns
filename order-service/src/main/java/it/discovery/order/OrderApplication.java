package it.discovery.order;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.api.impl.ActuatorHealthCheckService;
import it.discovery.balancer.api.impl.CPUUtilizationLoadBalancer;
import it.discovery.balancer.api.impl.RandomLoadBalancer;
import it.discovery.balancer.client.RetryableRestTemplate;
import it.discovery.balancer.config.CircuitBreakerConfig;
import it.discovery.balancer.config.RetryConfiguration;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import it.discovery.order.client.BookClient;
import it.discovery.order.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories("it.discovery.order.repository")
@EnableScheduling
@EnableRetry
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public BookClient bookClient(RestTemplate restTemplate,
                                 LoadBalancer loadBalancer, CacheConfiguration cacheConfiguration) {
        return new BookClient(restTemplate, loadBalancer, cacheConfiguration);
    }

    @Bean
    @Primary
    @Qualifier("retry")
    public RestTemplate restTemplate(RetryConfiguration retryConfiguration,
                                     CircuitBreakerConfig circuitBreakerConfig) {
        return new RetryableRestTemplate(retryConfiguration, circuitBreakerConfig);
    }

    @Bean
    @Qualifier("no-retry")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HealthCheckService healthCheckService(RestTemplate restTemplate,
                                                 LoadBalancerConfiguration loadBalancerConfiguration) {
        return new ActuatorHealthCheckService(restTemplate, loadBalancerConfiguration);
    }

    @Bean
    @ConditionalOnProperty(prefix = "load.balancer", name = "type", havingValue = "metrics")
    public LoadBalancer metricsLoadBalancer(HealthCheckService healthCheckService) {
        return new CPUUtilizationLoadBalancer(healthCheckService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "load.balancer", name = "type", havingValue = "random")
    public LoadBalancer randomLoadBalancer(HealthCheckService healthCheckService) {
        return new RandomLoadBalancer(healthCheckService);
    }
}
