package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.rule.assembly.DeleteLine;
import com.xforceplus.ultraman.permissions.rule.assembly.InsertLine;
import com.xforceplus.ultraman.permissions.rule.assembly.SelectLine;
import com.xforceplus.ultraman.permissions.rule.assembly.UpdateLine;
import com.xforceplus.ultraman.permissions.rule.check.Checker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 0.1 2019/11/14 11:47
 * @author dongbin
 * @since 1.8
 */
@Configuration
public class LineConfig {

    @Resource
    private List<Checker> checkers;

    @Bean
    public DeleteLine deleteLine() {
        return new DeleteLine(checkers);
    }

    @Bean
    public InsertLine insertLine() {
        return new InsertLine(checkers);
    }

    @Bean
    public SelectLine selectLine() {
        return new SelectLine(checkers);
    }

    @Bean
    public UpdateLine updateLine() {
        return new UpdateLine(checkers);
    }
}
