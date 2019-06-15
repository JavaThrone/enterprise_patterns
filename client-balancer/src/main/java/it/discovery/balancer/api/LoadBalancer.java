package it.discovery.balancer.api;

import it.discovery.balancer.server.ServerDefinition;

import java.util.Optional;

public interface LoadBalancer {
    Optional<ServerDefinition> chooseServer();
}
