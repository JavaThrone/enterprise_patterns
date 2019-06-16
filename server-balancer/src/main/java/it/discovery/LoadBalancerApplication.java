package it.discovery;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.api.impl.ActuatorHealthCheckService;
import it.discovery.balancer.api.impl.CPUUtilizationLoadBalancer;
import it.discovery.balancer.api.impl.RandomLoadBalancer;
import it.discovery.balancer.client.RetryableRestTemplate;
import it.discovery.balancer.config.CacheConfiguration;
import it.discovery.balancer.config.CircuitBreakerConfig;
import it.discovery.balancer.config.RetryConfiguration;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class LoadBalancerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadBalancerApplication.class, args);
	}

	@Bean
	@ConfigurationProperties("load.balancer")
	public LoadBalancerConfiguration loadBalancerConfiguration() {
		return new LoadBalancerConfiguration();
	}

	@Bean
	@ConfigurationProperties("retry-config")
	public RetryConfiguration retryConfiguration() {
		return new RetryConfiguration();
	}

	@Bean
	@ConfigurationProperties("circuit-config")
	public CircuitBreakerConfig circuitBreakerConfig() {
		return new CircuitBreakerConfig();
	}


	@Bean
	@ConfigurationProperties("cache-config")
	public CacheConfiguration cacheConfiguration() {
		return new CacheConfiguration();
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
	@ConditionalOnProperty(prefix = "load.balancer", name = "type", havingValue = "random")
	public LoadBalancer randomLoadBalancer(HealthCheckService healthCheckService) {
		return new RandomLoadBalancer(healthCheckService);
	}
}
