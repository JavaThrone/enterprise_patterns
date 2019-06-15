package it.discovery.balancer.server;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServerDefinition {
    private String id;

    private String url;

    private String zone;

    private boolean enabled = true;
}
