package it.discovery.balancer.api.impl;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.server.ServerDefinition;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    private final HealthCheckService healthCheckService;

    public RandomLoadBalancer(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @Override
    public Optional<ServerDefinition> chooseServer() {
        List<ServerDefinition> availableServers = healthCheckService.getAvailableServers();
        if (availableServers.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(availableServers.get(random.nextInt(availableServers.size())));
    }
}
