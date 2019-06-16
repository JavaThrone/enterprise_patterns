package it.discovery.balancer.client;

import it.discovery.balancer.RetryConfiguration;
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

    public RetryableRestTemplate(RetryConfiguration retryConfiguration) {
        retryPolicy = new RetryPolicy<>()
                .withDelay(Duration.ofMillis(
                        retryConfiguration.getDelay()))
                .withMaxAttempts(retryConfiguration.getMaxAttempts())
                .withMaxDuration(Duration.ofMillis(
                        retryConfiguration.getMaxDuration()));
    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method,
                              RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        //retry the operation if HTTP request failed
        //(either exception or non-200 code)
        return super.doExecute(url, method,
                requestCallback, responseExtractor);
    }
}
