package it.discovery.balancer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Positive;

@Getter
@Setter
@Validated
public class RetryConfiguration {
    @Positive
    private int maxAttempts;

    @Positive
    private long delay;

    @Positive
    private long maxDuration;
}
