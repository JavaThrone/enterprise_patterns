package it.discovery.order.balancer;

import it.discovery.balancer.config.CircuitBreakerConfig;
import it.discovery.balancer.config.RetryConfiguration;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    @ConfigurationProperties("circuit-config")
    public CircuitBreakerConfig circuitBreakerConfig() {
        return new CircuitBreakerConfig();
    }
}
