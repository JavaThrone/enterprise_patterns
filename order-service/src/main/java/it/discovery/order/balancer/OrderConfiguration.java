package it.discovery.order.balancer;

import it.discovery.balancer.RetryConfiguration;
import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.api.impl.ActuatorHealthCheckService;
import it.discovery.balancer.api.impl.RandomLoadBalancer;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
public class OrderConfiguration {

    @Bean
    @ConfigurationProperties("load.balancer")
    public LoadBalancerConfiguration loadBalancerConfiguration() {
        return new LoadBalancerConfiguration();
    }

    @Bean
    @ConfigurationProperties("retry-config")
    public RetryConfiguration retryConfiguration() {
        return  new RetryConfiguration();
    }

    @Bean
    public HealthCheckService healthCheckService(RestTemplate restTemplate) {
        return new ActuatorHealthCheckService(restTemplate, loadBalancerConfiguration());
    }

    @Bean
    public LoadBalancer loadBalancer(HealthCheckService healthCheckService) {
        return new RandomLoadBalancer(healthCheckService);
    }
}
