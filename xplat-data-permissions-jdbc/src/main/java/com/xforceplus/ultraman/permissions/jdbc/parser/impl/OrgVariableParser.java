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
 * 创建时间: 2020/9/8 6:13 PM
 */
public class OrgVariableParser implements VariableParser {

    private static final Logger logger = LoggerFactory.getLogger(OrgVariableParser.class);
    private static final String DEFAULT_ORG_ID = "0";


    private AuthorizedUserService userService;

    public OrgVariableParser(AuthorizedUserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return Variable.ORG_VARIABLE;
    }

    @Override
    public String parse(String sql) {
        Set<Long> orgIds = userService.getUserOrgIds(UserInfoHolder.get().getId());
        Set<String> formatOrgIds = orgIds.stream().map(item -> item.toString()).collect(Collectors.toSet());
        if (formatOrgIds.size() == 0) {
            formatOrgIds.add(DEFAULT_ORG_ID);
        }
        String formatStr = String.join(",", formatOrgIds);
        logger.info("User id : {},orgId : {}", UserInfoHolder.get().getId(), formatStr);
        return sql.replace(String.format("'%s'", getName()), formatStr);
    }
}
