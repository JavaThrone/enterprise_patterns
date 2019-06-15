package it.discovery.balancer.server;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class LoadBalancerConfiguration {

    @NotEmpty
    private List<ServerDefinition> servers;
}
