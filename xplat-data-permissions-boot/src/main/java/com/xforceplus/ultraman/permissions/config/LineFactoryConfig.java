package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.rule.assembly.*;
import com.xforceplus.ultraman.permissions.rule.check.Checker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 0.1 2019/11/14 11:30
 * @author dongbin
 * @since 1.8
 */
@Configuration
public class LineFactoryConfig {

    @Resource
    private List<Line> lines;

    @Bean
    public LineFactory lineFactory() {
        return new LineFactory(lines);
    }
}
