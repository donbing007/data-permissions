package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;
import io.grpc.*;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * 指标配置.
 *
 * @author dongbin
 * @version 0.1 2019/11/27 17:36
 * @since 1.8
 */
@Configuration
public class MetricsConfig {

    /**
     * counter 接收到的检查请求次数.
     */
    private static final String STARTED_TOTAL_NAME = "xdp.check.started.total";
    /**
     * counter 完成的请求次数,不论对错.
     */
    private static final String HANDLED_TOTAL_NAME = "xdp.check.handled.total";
    /**
     * counter rpc 状态统计.
     */
    private static final String RPC_STATUS_TOTAL_NAME = "xdp.check.rpc.status.total";
    /**
     * summary 处理延时直方图.
     */
    private static final String HANDLED_LATENCY_SECONDS_NAME = "xdp.check.handled_latency_seconds";

    @Resource(name = "grpcExecutor")
    private ExecutorService grpcExecutor;

    @Bean
    @GRpcGlobalInterceptor
    public ServerInterceptor grpcMonitorServerInterceptor() {
        return new ServerInterceptor() {

            private DistributionSummary handledLatency = DistributionSummary.builder(HANDLED_LATENCY_SECONDS_NAME)
                .description("handled latency seconds.")
                .maximumExpectedValue(30L)
                .publishPercentiles(0.1, 0.5, 0.75, 0.9)
                .register(Metrics.globalRegistry);

            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

                Metrics.counter(STARTED_TOTAL_NAME).increment();

                final long start = System.currentTimeMillis();

                ServerCall<ReqT, RespT> serverCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {

                    @Override
                    public void close(Status status, Metadata trailers) {

                        double latency = (double) (System.currentTimeMillis() - start) / 1000D;

                        handledLatency.record(latency);

                        Metrics.counter(RPC_STATUS_TOTAL_NAME, "status", status.getCode().name()).increment();

                        super.close(status, trailers);
                    }

                    @Override
                    public void sendMessage(RespT message) {
                        try {
                            if (ForStatmentGrpc.StatmentResult.class.equals(message.getClass())) {
                                ForStatmentGrpc.StatmentResult result = (ForStatmentGrpc.StatmentResult) message;
                                CheckStatus status = CheckStatus.getInstance(result.getStatus());
                                Metrics.counter(HANDLED_TOTAL_NAME, "status", status.name()).increment();
                            }
                        } finally {
                            super.sendMessage(message);
                        }
                    }
                };

                return next.startCall(serverCall, headers);
            }
        };
    }

    @Bean
    public ExecutorServiceMetrics executorServiceMetrics() {
        ExecutorServiceMetrics esm = new ExecutorServiceMetrics(grpcExecutor, "xdp", Tags.empty());
        esm.bindTo(Metrics.globalRegistry);
        return esm;
    }
}
