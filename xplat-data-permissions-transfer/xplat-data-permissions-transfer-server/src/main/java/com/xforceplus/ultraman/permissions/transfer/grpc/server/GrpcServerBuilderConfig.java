package com.xforceplus.ultraman.permissions.transfer.grpc.server;

import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @version 0.1 2019/11/14 19:02
 * @author dongbin
 * @since 1.8
 */
@ConditionalOnProperty(prefix = "grpc", name = "enabled", havingValue = "true")
@Component
public class GrpcServerBuilderConfig extends GRpcServerBuilderConfigurer {

    @Resource(name = "grpcExecutor")
    private Executor executor;

    @Resource
    private GrpcConfig config;

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
        serverBuilder.executor(executor);
        ((NettyServerBuilder) serverBuilder)
            .maxInboundMetadataSize(config.getMaxInboundMetadataBytes())
            .maxInboundMessageSize(config.getMaxInboundMessageBytes())
            .keepAliveTime(config.getHeartbeatIntervalSeconds(), TimeUnit.SECONDS)
            .keepAliveTimeout(config.getHeartbeatTimeoutSeconds(), TimeUnit.SECONDS)
            .permitKeepAliveWithoutCalls(true)
            .permitKeepAliveTime(1, TimeUnit.SECONDS);
    }
}
