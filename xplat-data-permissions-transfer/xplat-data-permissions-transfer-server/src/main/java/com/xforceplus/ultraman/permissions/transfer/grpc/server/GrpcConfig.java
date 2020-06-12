package com.xforceplus.ultraman.permissions.transfer.grpc.server;

import io.grpc.internal.GrpcUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version 0.1 2019/11/14 19:03
 * @author dongbin
 * @since 1.8
 */
@ConfigurationProperties(prefix = "grpc")
@Component
@ConditionalOnProperty(prefix = "grpc", name = "enabled", havingValue = "true")
public class GrpcConfig {

    private int maxInboundMetadataBytes = GrpcUtil.DEFAULT_MAX_HEADER_LIST_SIZE;
    private int maxInboundMessageBytes = GrpcUtil.DEFAULT_MAX_MESSAGE_SIZE;
    private long heartbeatIntervalSeconds = 30;
    private long heartbeatTimeoutSeconds = 30;

    public int getMaxInboundMetadataBytes() {
        return maxInboundMetadataBytes;
    }

    public void setMaxInboundMetadataBytes(int maxInboundMetadataBytes) {
        this.maxInboundMetadataBytes = maxInboundMetadataBytes;
    }

    public int getMaxInboundMessageBytes() {
        return maxInboundMessageBytes;
    }

    public void setMaxInboundMessageBytes(int maxInboundMessageBytes) {
        this.maxInboundMessageBytes = maxInboundMessageBytes;
    }

    public long getHeartbeatIntervalSeconds() {
        return heartbeatIntervalSeconds;
    }

    public void setHeartbeatIntervalSeconds(long heartbeatIntervalSeconds) {
        this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
    }

    public long getHeartbeatTimeoutSeconds() {
        return heartbeatTimeoutSeconds;
    }

    public void setHeartbeatTimeoutSeconds(long heartbeatTimeoutSeconds) {
        this.heartbeatTimeoutSeconds = heartbeatTimeoutSeconds;
    }
}
