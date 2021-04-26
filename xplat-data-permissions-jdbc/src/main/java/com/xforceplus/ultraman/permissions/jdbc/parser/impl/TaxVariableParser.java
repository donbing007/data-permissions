package com.xforceplus.ultraman.permissions.jdbc.parser.impl;

import com.xforceplus.tenant.security.core.context.UserInfoHolder;
import com.xforceplus.ultraman.permissions.jdbc.parser.AuthorizedUserService;
import com.xforceplus.ultraman.permissions.jdbc.parser.Variable;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 6:13 PM
 */
public class TaxVariableParser implements VariableParser {

    private static final Logger logger = LoggerFactory.getLogger(TaxVariableParser.class);

    private AuthorizedUserService userService;

    private static final String DEFAULT_TAX_NUM = "''";

    public TaxVariableParser(AuthorizedUserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return Variable.TAX_VARIABLE;
    }

    @Override
    public String parse(String sql) {
        Set<String> taxNums = userService.getUserTaxNums(UserInfoHolder.get().getId());
        logger.info("User id : {},taxNums : {}", UserInfoHolder.get().getId(), String.join(",", taxNums));
        Set<String> formatTaxNums = taxNums.stream().map(item -> String.format("'%s'", item)).collect(Collectors.toSet());
        if (formatTaxNums.size() == 0) {
            formatTaxNums.add(DEFAULT_TAX_NUM);
        }
        return sql.replace(String.format("'%s'", getName()), String.join(",", formatTaxNums));
    }
}
