package it.discovery.balancer.configuration;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.api.impl.ActuatorHealthCheckService;
import it.discovery.balancer.api.impl.CPUUtilizationLoadBalancer;
import it.discovery.balancer.api.impl.RandomLoadBalancer;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoadBalancerAutoConfiguration {
    @Bean
    public HealthCheckService healthCheckService(RestTemplate restTemplate,
                                                 LoadBalancerConfiguration loadBalancerConfiguration) {
        return new ActuatorHealthCheckService(restTemplate, loadBalancerConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(LoadBalancer.class)
    @ConditionalOnProperty(name = "load.balancer.type", value = "random")
    public LoadBalancer loadBalancer(HealthCheckService healthCheckService) {
        return new RandomLoadBalancer(healthCheckService);
    }

    @Bean
    @ConditionalOnProperty(name = "load.balancer.type", value = "metrics")
    public LoadBalancer metricsLoadBalancer(HealthCheckService healthCheckService) {
        return new CPUUtilizationLoadBalancer(healthCheckService);
    }
}
