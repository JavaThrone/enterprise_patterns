package it.discovery.balancer.server;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class LoadBalancerConfiguration {

    private List<ServerDefinition> servers;
}
