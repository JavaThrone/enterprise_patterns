package it.discovery.balancer.api.impl;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import it.discovery.balancer.server.ServerDefinition;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NoOpHealthCheckService implements HealthCheckService {

    private final LoadBalancerConfiguration loadBalancerConfiguration;
    @Override
    public List<ServerDefinition> getAvailableServers() {
        return loadBalancerConfiguration.getServers();
    }
}
