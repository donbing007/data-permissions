package com.xforceplus.ultraman.permissions.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version 0.1 2019/11/14 13:59
 * @author dongbin
 * @since 1.8
 */
@Configuration
@ConfigurationProperties(prefix = "executor")
@ConditionalOnProperty(prefix = "executor", name = "enabled", havingValue = "true")
public class ExecutorConfig {

    private String workerName = "executor";
    private int maxWorkerSize = Runtime.getRuntime().availableProcessors();
    private int maxTaskSize = 1000;

    @Bean
    public Executor executor() {
        int cpuCore = Runtime.getRuntime().availableProcessors();

        int core = 0, max = 0;
        if (maxWorkerSize <= cpuCore) {

            core = cpuCore;
            max = cpuCore;

        } else {

            core = cpuCore;
            max = maxWorkerSize;

        }


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            core,
            max,
            100L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(maxTaskSize),
            new ThreadFactory() {

                private final AtomicLong number = new AtomicLong();

                @Override
                public Thread newThread(Runnable r) {
                    Thread newThread = Executors.defaultThreadFactory().newThread(r);
                    newThread.setName(workerName + "-" + number.getAndIncrement());
                    newThread.setDaemon(true);
                    return newThread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                executor.shutdown();
            }
        }));


        return executor;
    }
}
