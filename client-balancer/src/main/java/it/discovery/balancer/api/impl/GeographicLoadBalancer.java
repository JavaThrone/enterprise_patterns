package it.discovery.balancer.api.impl;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.server.ServerDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Optional;

@Slf4j
public class GeographicLoadBalancer implements LoadBalancer {
    private final HealthCheckService healthCheckService;

    private final String serverZone;

    public GeographicLoadBalancer(HealthCheckService healthCheckService,
                                  Environment env) {
        this.healthCheckService = healthCheckService;
        serverZone = env
                .getProperty("server.zone", "Ukraine");
    }

    @Override
    public Optional<ServerDefinition> chooseServer() {
        var availableServers = healthCheckService.getAvailableServers();
        if (availableServers.isEmpty()) {
            return Optional.empty();
        }

        var validServer = availableServers.stream()
                .filter(server -> server.getZone().equals(serverZone))
                .findFirst();

        return validServer.or(() -> Optional.of(availableServers.get(0)));
    }
}
