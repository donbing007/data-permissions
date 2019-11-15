package com.xforceplus.ultraman.permissions;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @version 0.1 2019/10/23 14:31
 * @author dongbin
 * @since 1.8
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.boot.context.properties.bind.Binder")
public class AutoConfig {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 20).build();


    }
}
