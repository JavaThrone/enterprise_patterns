package it.discovery.balancer.stats;

import it.discovery.balancer.server.ServerDefinition;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServerStats {

    private ServerDefinition serverDefinition;

    private double cpuUtilization;

    private double jvmMemoryMax;

    private double jvmMemoryUsed;

    private int tomcatSessions;

    private int tomcatSessionsCurrent;

    public ServerStats(ServerDefinition serverDefinition, double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
        this.serverDefinition = serverDefinition;
    }
}
