package it.discovery.balancer.api;

import it.discovery.balancer.server.ServerDefinition;

import java.util.List;

public interface HealthCheckService {

    List<ServerDefinition> getAvailableServers();
}
