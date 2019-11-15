package com.xforceplus.ultraman.permissions.config;

import com.xforceplus.ultraman.permissions.rule.check.common.ConditionsChecker;
import com.xforceplus.ultraman.permissions.rule.check.common.validation.*;
import com.xforceplus.ultraman.permissions.rule.check.insert.InsertFieldChecker;
import com.xforceplus.ultraman.permissions.rule.check.select.SelectFieldChecker;
import com.xforceplus.ultraman.permissions.rule.check.update.UpdateSetFieldChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 0.1 2019/11/14 11:41
 * @auth dongbin
 * @since 1.8
 */
@Configuration
public class CheckerConfig {

    @Bean
    public AllFieldCannotUseChecker allFieldCannotUseChecker() {
        return new AllFieldCannotUseChecker();
    }

    @Bean
    public CanNotAllowSubChecker canNotAllowSubChecker() {
        return new CanNotAllowSubChecker();
    }

    @Bean
    public FromSubSelectMustAliasChecker fromSubSelectMustAliasChecker() {
        return new FromSubSelectMustAliasChecker();
    }

    @Bean
    public SelectItemRefMustChecker selectItemRefMustChecker() {
        return new SelectItemRefMustChecker();
    }

    @Bean
    public SelectItemNotFeildAliasMustChecker selectItemNotFeildAliasMustChecker() {
        return new SelectItemNotFeildAliasMustChecker();
    }

    @Bean
    public ConditionsChecker conditionsChecker() {
        return new ConditionsChecker();
    }

    @Bean
    public InsertFieldChecker insertFieldChecker() {
        return new InsertFieldChecker();
    }

    @Bean
    public SelectFieldChecker selectFieldChecker() {
        return new SelectFieldChecker();
    }

    @Bean
    public UpdateSetFieldChecker updateSetFieldChecker() {
        return new UpdateSetFieldChecker();
    }
}
