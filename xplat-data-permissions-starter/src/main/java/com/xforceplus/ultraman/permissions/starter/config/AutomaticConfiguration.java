package com.xforceplus.ultraman.permissions.starter.config;

import com.google.common.collect.Maps;
import com.xforceplus.ultraman.permissions.cache.CaffeineCacheEnum;
import com.xforceplus.ultraman.permissions.cache.XplatCacheManager;
import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.authorization.impl.ContextAuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.authorization.impl.MockAuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.client.GrpcRuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.jdbc.parser.AuthorizedUserService;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParser;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParserManager;
import com.xforceplus.ultraman.permissions.jdbc.parser.http.HttpClient;
import com.xforceplus.ultraman.permissions.jdbc.parser.impl.AuthorizedUserServiceImpl;
import com.xforceplus.ultraman.permissions.jdbc.parser.impl.TaxVariableParser;
import com.xforceplus.ultraman.permissions.jdbc.utils.DebugStatus;
import com.xforceplus.ultraman.permissions.starter.DataSourceInterceptor;
import com.xforceplus.ultraman.permissions.starter.DataSourceWrapper;
import com.xforceplus.ultraman.permissions.starter.define.BeanNameDefine;
import com.xforceplus.ultraman.permissions.transfer.grpc.client.StatmentCheckClientGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.Map;

import static com.xforceplus.ultraman.permissions.starter.define.BeanNameDefine.TAX_NO_PARSER;

/**
 * 自动配置.
 *
 * @author dongbin
 * @version 0.1 2019/10/23 14:31
 * @since 1.8
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.boot.context.properties.bind.Binder")
@ConfigurationProperties(prefix = "xplat.data.permissions")
@EnableConfigurationProperties({AutomaticConfiguration.class})
public class AutomaticConfiguration {

    private String host = "127.0.0.1";

    private int port = 8206;

    private long heartbeatTimeoutSeconds = 30;

    private long heartbeatIntervalSeconds = 30;

    private long readTimeoutMs = 200;

    private String includeRex = "(.*)";

    private boolean debug = false;

    @Value("${xplat.data.permissions.tenant.token-refresh-in-seconds:7200}")
    private int MAX_TOKEN_LIFE;
    @Value("${xplat.data.permissions.tenant.connection-time-out:10}")
    private int CLIENT_CONN_TIMEOUT;
    @Value("${xplat.data.permissions.tenant.read-time-out:60}")
    private int CLIENT_READ_TIMEOUT;

    @Resource
    private AuthSearcherConfig searcherConfig;

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

        if (readTimeoutMs > 0) {
            client.setReadTimeMs(readTimeoutMs);
        }

        return client;
    }

    @Bean(BeanNameDefine.RULE_CHECK_CLIENT)
    public RuleCheckServiceClient ruleCheckServiceClient() {
        return new GrpcRuleCheckServiceClient();
    }

    @Bean(BeanNameDefine.AUTHORIZATION_SEARCHER)
    public AuthorizationSearcher authorizationSearcher() {
        AuthorizationSearcher authorizationSearcher;

        switch (searcherConfig.getName().toUpperCase()) {
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
    @DependsOn({
            BeanNameDefine.DATA_SOURCE_WRAPPER,
            BeanNameDefine.RULE_CHECK_CLIENT,
            BeanNameDefine.AUTHORIZATION_SEARCHER
    })
    @ConditionalOnProperty(prefix = "xplat.data.permissions", name = "manual", havingValue = "false", matchIfMissing = true)
    public DataSourceInterceptor dataSourceInterceptor() {
        return new DataSourceInterceptor(includeRex);
    }

    @Bean(BeanNameDefine.DATA_SOURCE_WRAPPER)
    public DataSourceWrapper dataSourceWrapper() {
        return new DataSourceWrapper();
    }

    @Bean
    public AuthorizedUserService authorizedUserService() {
        return new AuthorizedUserServiceImpl();
    }

    @Bean(TAX_NO_PARSER)
    public VariableParser variableParser(AuthorizedUserService service) {
        return new TaxVariableParser(service);
    }

    @Bean(BeanNameDefine.VARIABLE_PARSE_MANAGER)
    public VariableParserManager variableParserManager() {
        return new VariableParserManager();
    }

    @Bean(BeanNameDefine.XPLAT_CACHE_MANAGER)
    public XplatCacheManager xplatCacheManager() {
        XplatCacheManager xplatCacheManager = new XplatCacheManager();
        Map<String, String> caffeineSpecs = Maps.newHashMap();
        Arrays.stream(CaffeineCacheEnum.values()).forEach(item -> {
            caffeineSpecs.put(item.cacheName(), item.cacheSpec());
        });
        xplatCacheManager.setCacheSpecs(caffeineSpecs);
        xplatCacheManager.setCacheSpecification(CaffeineCacheEnum.DEFAULT_CACHE_NAME.cacheSpec());
        return xplatCacheManager;
    }

    @Bean
    public HttpClient httpClient() {
        HttpClient client = new HttpClient(CLIENT_CONN_TIMEOUT, CLIENT_READ_TIMEOUT, MAX_TOKEN_LIFE);
        return client;
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

    public void setIncludeRex(String includeRex) {
        this.includeRex = includeRex;
    }

    public void setReadTimeoutMs(long readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }
}
