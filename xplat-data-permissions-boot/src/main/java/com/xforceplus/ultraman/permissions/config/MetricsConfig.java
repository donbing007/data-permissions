package com.xforceplus.ultraman.permissions.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author dongbin
 * @version 0.1 2019/11/27 17:36
 * @since 1.8
 */
@Configuration
public class MetricsConfig {

    @Resource
    private MeterRegistry registry;

    @Bean("xdpSqlCheckTotal")
    public Counter xdpSqlCheckTotal() {
        return Counter.builder("xdp_sql_check_total").description("SQL check total.").register(registry);
    }


}
