package it.discovery.balancer.api.impl;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import it.discovery.balancer.server.ServerDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ActuatorHealthCheckService implements HealthCheckService {

    private final RestTemplate restTemplate;

    private final List<RuntimeServerInfo> servers;

    public ActuatorHealthCheckService(RestTemplate restTemplate,
                                      LoadBalancerConfiguration loadBalancerConfiguration) {
        this.restTemplate = restTemplate;
        servers = loadBalancerConfiguration.getServers()
                .stream()
                .map(serverDefinition -> new RuntimeServerInfo(serverDefinition))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServerDefinition> getAvailableServers() {
        return servers.stream()
                .filter(RuntimeServerInfo::isEnabled)
                .map(RuntimeServerInfo::getServerDefinition)
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    public static class RuntimeServerInfo {
        private final ServerDefinition serverDefinition;

        private boolean enabled;

        private LocalDateTime lastStatusChecked;
    }
}
