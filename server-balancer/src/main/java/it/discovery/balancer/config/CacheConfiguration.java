package it.discovery.balancer.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheConfiguration {
    private int maxSize;

    private long expiresAfterAccess;
}
