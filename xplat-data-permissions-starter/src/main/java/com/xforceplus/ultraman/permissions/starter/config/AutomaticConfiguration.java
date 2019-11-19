package com.xforceplus.ultraman.permissions.starter.config;

import com.xforceplus.ultraman.permissions.starter.DataSourceInterceptor;
import com.xforceplus.ultraman.permissions.starter.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.starter.authorization.impl.MockAuthorizationSearcher;
import com.xforceplus.ultraman.permissions.starter.client.GrpcRuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.transfer.grpc.client.GrpcStatmentCheckClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 0.1 2019/10/23 14:31
 * @author dongbin
 * @since 1.8
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.boot.context.properties.bind.Binder")
@ConfigurationProperties(prefix = "xplat.data.permissions")
@EnableConfigurationProperties(AutomaticConfiguration.class)
public class AutomaticConfiguration {

    private String host;

    private int port;

    private long heartbeatTimeoutSeconds;

    private long heartbeatIntervalSeconds;

    private AuthorizationSearcherConfig searcher;

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public GrpcStatmentCheckClient grpcStatmentCheckClient() {
        GrpcStatmentCheckClient client = new GrpcStatmentCheckClient();
        if (host != null) {
            client.setHost(host);
        }
        if (port > 0) {
            client.setPort(port);
        }
        if (heartbeatTimeoutSeconds > 0) {
            client.setHeartbeatTimeoutSeconds(heartbeatTimeoutSeconds);
        }
        if (heartbeatIntervalSeconds > 0) {
            client.setHeartbeatIntervalSeconds(heartbeatIntervalSeconds);
        }

        return client;
    }

    @Bean
    public RuleCheckServiceClient ruleCheckServiceClient() {
        return new GrpcRuleCheckServiceClient();
    }

    @Bean
    public AuthorizationSearcher authorizationSearcher() {
        AuthorizationSearcher authorizationSearcher;
        switch(searcher.getName().toUpperCase()) {
            case "MOCK": {
                MockAuthorizationSearcher s = new MockAuthorizationSearcher();
                s.set(searcher.getRole(), searcher.getTenant());

                authorizationSearcher = s;
                break;
            }
            case "DEFAULT": {

            }
            default:
                throw new IllegalStateException("Unexpected value: " + searcher.getName());
        }

        return authorizationSearcher;
    }

    @Bean
    public DataSourceInterceptor dataSourceInterceptor() {
        return new DataSourceInterceptor();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHeartbeatTimeoutSeconds(long heartbeatTimeoutSeconds) {
        this.heartbeatTimeoutSeconds = heartbeatTimeoutSeconds;
    }

    public void setHeartbeatIntervalSeconds(long heartbeatIntervalSeconds) {
        this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
    }

    public void setSearcher(AuthorizationSearcherConfig searcher) {
        this.searcher = searcher;
    }
}
