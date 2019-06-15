package it.discovery.balancer.api.impl;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.server.LoadBalancerConfiguration;
import it.discovery.balancer.server.ServerDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ActuatorHealthCheckService implements HealthCheckService {

    private final RestTemplate restTemplate;

    private final List<RuntimeServerInfo> servers;

    public ActuatorHealthCheckService(RestTemplate restTemplate,
                                      LoadBalancerConfiguration loadBalancerConfiguration) {
        this.restTemplate = restTemplate;
        servers = loadBalancerConfiguration.getServers()
                .stream()
                .map(serverDefinition -> new RuntimeServerInfo(
                        serverDefinition))
                .collect(Collectors.toList());
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void checkStatus() {
        servers.stream()
                .forEach(server -> {
                    server.setAlive(isAlive(
                            server.getServerDefinition()));
                    server.setLastStatusChecked(LocalDateTime.now());
                });
    }

    private boolean isAlive(ServerDefinition serverDefinition) {
        try {
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(serverDefinition.getUrl()
                    + "/actuator/health", Map.class);
            return responseEntity.getStatusCode().is2xxSuccessful()
                    && responseEntity.getBody().get("status")
                    .equals("UP");
        } catch (RestClientException ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public List<ServerDefinition> getAvailableServers() {
        return servers.stream()
                .filter(RuntimeServerInfo::isAlive)
                .map(RuntimeServerInfo::getServerDefinition)
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    public static class RuntimeServerInfo {
        private final ServerDefinition serverDefinition;

        private boolean alive;

        private LocalDateTime lastStatusChecked;
    }
}
