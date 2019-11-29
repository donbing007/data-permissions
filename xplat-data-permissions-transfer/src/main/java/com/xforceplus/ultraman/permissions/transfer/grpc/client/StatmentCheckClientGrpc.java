package com.xforceplus.ultraman.permissions.transfer.grpc.client;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.StatmentCheckServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * grpc 实现.
 *
 * @version 0.1 2019/11/15 14:42
 * @author dongbin
 * @since 1.8
 */
public class StatmentCheckClientGrpc {

    final Logger logger = LoggerFactory.getLogger(StatmentCheckClientGrpc.class);

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

        logger.info("Xplat-data-permissions-client successfully connects to {}:{}.!",host,port);
    }

    public void destroy() throws InterruptedException {
        channel.shutdown().awaitTermination(30L, TimeUnit.SECONDS);

    }

    public ForStatmentGrpc.StatmentResult check(String sql, Authorizations authorizations) {
        ForStatmentGrpc.Statment.Builder builder = ForStatmentGrpc.Statment.newBuilder().setSql(sql);

        for (Authorization authorization : authorizations.getAuthorizations()) {
            builder.addAuthorization(
                com.xforceplus.ultraman.permissions.transfer.grpc.generate.AuthorizationGrpc.Authorization.newBuilder()
                .setRole(authorization.getRole()).setTenant(authorization.getTenant()).build());
        }

        return stub.check(builder.build());
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
