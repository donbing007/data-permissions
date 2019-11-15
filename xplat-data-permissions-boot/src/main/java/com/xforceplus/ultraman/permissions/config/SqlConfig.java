package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 0.1 2019/11/14 12:16
 * @auth dongbin
 * @since 1.8
 */
@Configuration
public class SqlConfig {

    @Bean
    public SqlParser sqlParser() {
        return JSqlParser.getInstance();
    }
}
