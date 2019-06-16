package it.discovery.balancer.client;

import it.discovery.balancer.config.CircuitBreakerConfig;
import it.discovery.balancer.config.RetryConfiguration;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.FailsafeExecutor;
import net.jodah.failsafe.RetryPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;

public class RetryableRestTemplate extends RestTemplate{

    private final RetryPolicy<Object> retryPolicy;

    private final CircuitBreaker<Object> circuitBreaker;

    public RetryableRestTemplate(RetryConfiguration retryConfiguration,
                                 CircuitBreakerConfig circuitBreakerConfig) {
        retryPolicy = new RetryPolicy<>()
                .handle(RestClientException.class)
                .withDelay(Duration.ofMillis(
                        retryConfiguration.getDelay()))
                .withMaxAttempts(retryConfiguration.getMaxAttempts())
                .withMaxDuration(Duration.ofMillis(
                        retryConfiguration.getMaxDuration()));
        circuitBreaker = new CircuitBreaker<>()
                .handle(RestClientException.class)
                .withFailureThreshold(circuitBreakerConfig.getFailureThresholds())
                .withSuccessThreshold(circuitBreakerConfig.getSuccessThresholds())
                .withDelay(Duration.ofMillis(circuitBreakerConfig.getDelay()));

    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method,
                              RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        FailsafeExecutor<Object> failsafe = Failsafe.with(retryPolicy, circuitBreaker);
        //failsafe.run();
        //retry the operation if HTTP request failed
        //(either exception or non-200 code)
        return super.doExecute(url, method,
                requestCallback, responseExtractor);
    }
}
