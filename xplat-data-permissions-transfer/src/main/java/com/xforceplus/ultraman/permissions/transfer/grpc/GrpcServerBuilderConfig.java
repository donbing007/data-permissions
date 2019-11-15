package com.xforceplus.ultraman.permissions.transfer.grpc;

import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @version 0.1 2019/11/14 19:02
 * @author dongbin
 * @since 1.8
 */
@ConditionalOnProperty(prefix = "grpc", name = "enabled", havingValue = "true")
@Component
public class GrpcServerBuilderConfig extends GRpcServerBuilderConfigurer {

    @Resource
    private Executor executor;

    @Resource
    private GrpcConfig config;

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
        serverBuilder.executor(executor);
        ((NettyServerBuilder) serverBuilder)
            .maxInboundMetadataSize(config.getMaxInboundMetadataSize())
            .maxInboundMessageSize(config.getMaxInboundMessageSize());
    }
}
