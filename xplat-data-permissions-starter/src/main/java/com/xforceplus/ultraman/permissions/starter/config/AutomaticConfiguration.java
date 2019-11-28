package com.xforceplus.ultraman.permissions.starter.config;

import com.xforceplus.ultraman.permissions.starter.DataSourceInterceptor;
import com.xforceplus.ultraman.permissions.starter.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.starter.authorization.impl.ContextAuthorizationSearcher;
import com.xforceplus.ultraman.permissions.starter.authorization.impl.MockAuthorizationSearcher;
import com.xforceplus.ultraman.permissions.starter.client.GrpcRuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.utils.DebugStatus;
import com.xforceplus.ultraman.permissions.transfer.grpc.client.StatmentCheckClientGrpc;
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

    private String host = "127.0.0.1";

    private int port = 8206;

    private long heartbeatTimeoutSeconds;

    private long heartbeatIntervalSeconds;

    private String includeRex = "(.*)";

    private boolean debug;

    private AuthSearcherConfig searcherConfig = new AuthSearcherConfig();

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public StatmentCheckClientGrpc grpcStatmentCheckClient() {
        StatmentCheckClientGrpc client = new StatmentCheckClientGrpc();
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

        switch(searcherConfig.getName().toUpperCase()) {
            case AuthSearcherConfig.MOCK_NAME: {
                authorizationSearcher = new MockAuthorizationSearcher(searcherConfig.getRole(), searcherConfig.getTenant());
                break;
            }
            case AuthSearcherConfig.CONTEXT_NAME: {
                authorizationSearcher = new ContextAuthorizationSearcher();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + searcherConfig.getName());
        }

        return authorizationSearcher;
    }

    @Bean
    public DataSourceInterceptor dataSourceInterceptor() {
        return new DataSourceInterceptor(includeRex);
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

    public void setSearcherConfig(AuthSearcherConfig searcherConfig) {
        this.searcherConfig = searcherConfig;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
        DebugStatus.setDebug(debug);
    }
}
