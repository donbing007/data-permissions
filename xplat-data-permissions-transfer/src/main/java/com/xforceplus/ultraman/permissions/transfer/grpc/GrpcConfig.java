package com.xforceplus.ultraman.permissions.transfer.grpc;

import io.grpc.internal.GrpcUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version 0.1 2019/11/14 19:03
 * @author dongbin
 * @since 1.8
 */
@ConfigurationProperties(prefix = "grpc")
@Component
public class GrpcConfig {

    private int maxInboundMetadataSize = GrpcUtil.DEFAULT_MAX_HEADER_LIST_SIZE;
    private int maxInboundMessageSize = GrpcUtil.DEFAULT_MAX_MESSAGE_SIZE;

    public void setMaxInboundMetadataSize(int maxInboundMetadataSize) {
        this.maxInboundMetadataSize = maxInboundMetadataSize;
    }

    public void setMaxInboundMessageSize(int maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }

    public int getMaxInboundMetadataSize() {
        return maxInboundMetadataSize;
    }

    public int getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }
}
