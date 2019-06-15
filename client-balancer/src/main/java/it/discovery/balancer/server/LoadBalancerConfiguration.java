package it.discovery.balancer.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Validated
public class LoadBalancerConfiguration {

    @NotEmpty
    private List<ServerDefinition> servers;
}
