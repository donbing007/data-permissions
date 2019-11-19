package com.xforceplus.ultraman.permissions.transfer.grpc.client;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.StatmentCheckServiceGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.UserGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * grpc 实现.
 *
 * @version 0.1 2019/11/15 14:42
 * @author dongbin
 * @since 1.8
 */
public class GrpcStatmentCheckClient {

    final Logger logger = LoggerFactory.getLogger(GrpcStatmentCheckClient.class);

    private String host = "xplat-data-permissions";
    private int port = 8206;
    private long heartbeatIntervalSeconds = 30;
    private long heartbeatTimeoutSeconds = 30;

    private ManagedChannel channel;
    private StatmentCheckServiceGrpc.StatmentCheckServiceBlockingStub stub;

    public void init() {

        channel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .keepAliveTime(heartbeatIntervalSeconds, TimeUnit.SECONDS)
            .keepAliveTimeout(heartbeatTimeoutSeconds, TimeUnit.SECONDS)
            .build();

        stub = StatmentCheckServiceGrpc.newBlockingStub(channel);

        logger.info("Statment check client start!");
    }

    public void destroy() throws InterruptedException {
        channel.shutdown().awaitTermination(30L, TimeUnit.SECONDS);

    }

    public ForStatmentGrpc.StatmentResult check(String sql, Authorization authorization) {
        ForStatmentGrpc.Statment request = ForStatmentGrpc.Statment.newBuilder()
            .setSql(sql)
            .setRole(
                UserGrpc.Role.newBuilder()
                    .setId(authorization.getRole())
                    .setTenant(authorization.getTenant())).build();

        return stub.check(request);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHeartbeatIntervalSeconds(long heartbeatIntervalSeconds) {
        this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
    }

    public void setHeartbeatTimeoutSeconds(long heartbeatTimeoutSeconds) {
        this.heartbeatTimeoutSeconds = heartbeatTimeoutSeconds;
    }
}
