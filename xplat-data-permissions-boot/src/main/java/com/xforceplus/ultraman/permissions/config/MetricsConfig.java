package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;
import io.grpc.*;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Metrics;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 指标配置.
 *
 * @author dongbin
 * @version 0.1 2019/11/27 17:36
 * @since 1.8
 */
@Configuration
public class MetricsConfig implements SchedulingConfigurer {

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
    /**
     * gauge 当前活跃工作者数量.
     */
    private static final String ACTIVE_WORKER = "xdp.check.active.worker";
    /**
     * gauge 堆积的任务数量.
     */
    private static final String STACKING_TASK = "xdp.check.stacking.count";

    @Resource(name = "grpcExecutor")
    private Executor grpcExecutor;

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

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (grpcExecutor != null) {

            taskRegistrar.addFixedDelayTask(() -> {

                if (ThreadPoolExecutor.class.isInstance(grpcExecutor)) {
                    Metrics.gauge(ACTIVE_WORKER, ((ThreadPoolExecutor) grpcExecutor).getActiveCount());
                    Metrics.gauge(STACKING_TASK, ((ThreadPoolExecutor) grpcExecutor).getQueue().size());
                }
            }, 30);
        }
    }
}
