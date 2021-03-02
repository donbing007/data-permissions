package com.xforceplus.ultraman.permissions.jdbc.parser.impl;

import com.xforceplus.tenant.security.core.context.UserInfoHolder;
import com.xforceplus.ultraman.permissions.jdbc.parser.AuthorizedUserService;
import com.xforceplus.ultraman.permissions.jdbc.parser.Variable;
import com.xforceplus.ultraman.permissions.jdbc.parser.VariableParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2021/3/1 5:52 PM
 */
public class CompanyVariableParser implements VariableParser {

    private static final Logger logger = LoggerFactory.getLogger(CompanyVariableParser.class);

    private AuthorizedUserService userService;

    public CompanyVariableParser(AuthorizedUserService service) {
        this.userService = service;
    }

    @Override
    public String getName() {
        return Variable.COMPANY_VARIABLE;
    }

    @Override
    public String parse(String sql) {
        Set<Long> companyIds = userService.getUserCompanyIds(UserInfoHolder.get().getId());
        Set<String> formatCompanyIds = companyIds.stream().map(item -> item.toString()).collect(Collectors.toSet());
        String formatStr = String.join(",", formatCompanyIds);
        logger.info("User id : {},companyIds : {}", UserInfoHolder.get().getId(), formatStr);
        return sql.replace(String.format("'%s'", getName()), formatStr);
    }
}
