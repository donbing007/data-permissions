package com.xforceplus.ultraman.permissions.service.impl;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.perissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.rule.assembly.Line;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.service.RuleCheckService;
import com.xforceplus.ultraman.permissions.service.define.CheckStatus;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * sql 权限校验服务实现.
 * @version 0.1 2019/11/13 15:20
 * @auth dongbin
 * @since 1.8
 */
@Service
public class RuleCheckServiceImpl implements RuleCheckService {

    final Logger logger = LoggerFactory.getLogger(RuleCheckServiceImpl.class);

    @Resource
    private SqlParser sqlParser;

    @Resource
    private List<Line> lines;

    @Resource
    private Searcher searcher;

    @Override
    public CheckResult check(String sqlStr, Authorization authorization) {
        Sql sql;
        try {
            sql = sqlParser.parser(sqlStr);
        } catch (Exception ex) {
            logger.error("Unable to parse {}, message is {}.", sqlStr, ex.getMessage());

            return new CheckResult(CheckStatus.ERROR.getValue());
        }

        Line selectedLine = null;
        for (Line line : lines) {
            if (line.isSupport(sql)) {
                selectedLine = line;
                break;
            }
        }

        if (selectedLine == null) {
            return new CheckResult(CheckStatus.NOT_SUPPORT.getValue());
        } else {

            DefaultContext context = new DefaultContext(sql, authorization, searcher);

            /**
             * 执行流水线.有以下分支.
             * 1. 拒绝.
             * 2. 异常.
             * 3. 通过.
             * 4. 更新后通过.
             */
            try {
                selectedLine.start(context);
            } catch (Throwable ex) {
                logger.error("An error occurred while processing {}, msg {}", sqlStr, ex.getMessage());

                return new CheckResult(CheckStatus.ERROR.getValue());
            }

            if (context.isRefused()) {

                CheckResult result = new CheckResult(CheckStatus.DENIAL.getValue());
                result.setMessage(context.cause());
                return result;

            } else {

                if (context.isUpdatedSql()) {
                    CheckResult result = new CheckResult(CheckStatus.UPDATE.getValue());
                    result.setValue(context.sql().toSqlString());
                    return result;
                } else {
                    return new CheckResult(CheckStatus.UPDATE.getValue());
                }

            }
        }

    }
}
