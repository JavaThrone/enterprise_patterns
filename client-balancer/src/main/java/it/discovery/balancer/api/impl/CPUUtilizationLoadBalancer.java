package it.discovery.balancer.api.impl;

import it.discovery.balancer.api.HealthCheckService;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.server.ServerDefinition;
import it.discovery.balancer.stats.ServerStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class CPUUtilizationLoadBalancer implements LoadBalancer {

    private final HealthCheckService healthCheckService;

    private final Map<String, ServerStats> serverStats =
            new ConcurrentHashMap<>();

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Optional<ServerDefinition> chooseServer() {
        if(serverStats.isEmpty()) {
            return Optional.empty();
        }

        return serverStats.entrySet().stream()
                .sorted(Comparator.comparingDouble(s -> s.getValue()
                        .getCpuUtilization())).findFirst()
                .map(entry -> entry.getValue().getServerDefinition());
    }

    @Scheduled(fixedDelay = 120000)
    private void queryServerStats() {
        healthCheckService.getAvailableServers()
         .parallelStream().forEach(server -> {
            serverStats.put(server.getId(),
                    queryMetricsAndSaveResult(server));
        });
    }

    private ServerStats queryMetricsAndSaveResult(ServerDefinition serverDefinition) {
        String url = serverDefinition.getUrl();
        try {
            log.info("Pinging server ... " + url);
            Map<String, Object> status = restTemplate.getForObject(url +
                    "/actuator/metrics/system.cpu.usage", Map.class);
            List<Map<String, Object>> procs =
                    (List<Map<String, Object>>) status.get("measurements");

            double utilization = NumberUtils.toDouble(procs.get(0).get("value").toString());
            return new ServerStats(serverDefinition, utilization);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            //TODO fix
            return null;
        }
    }
}
