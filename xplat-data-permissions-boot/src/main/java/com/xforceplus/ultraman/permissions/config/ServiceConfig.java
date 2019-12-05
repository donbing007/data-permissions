package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.rule.searcher.impl.DefaultSearcherImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 0.1 2019/11/14 11:49
 * @author dongbin
 * @since 1.8
 */
@Configuration
public class ServiceConfig {

    @Bean
    public Searcher searcher() {
        return new DefaultSearcherImpl();
    }

}
