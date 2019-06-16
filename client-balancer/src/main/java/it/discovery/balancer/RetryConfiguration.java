package it.discovery.balancer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;

@Getter @Setter
public class RetryConfiguration {
    @Positive
    private int maxAttempts;

    @Positive
    private long delay;

    @Positive
    private long maxDuration;
}
