package com.xforceplus.ultraman.permissions.service.impl;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.perissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.perissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.rule.assembly.Line;
import com.xforceplus.ultraman.permissions.rule.assembly.LineFactory;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.service.RuleCheckService;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * sql 权限校验服务实现.
 *
 * @version 0.1 2019/11/13 15:20
 * @author dongbin
 * @since 1.8
 */
@Service
public class RuleCheckServiceImpl implements RuleCheckService {

    final Logger logger = LoggerFactory.getLogger(RuleCheckServiceImpl.class);

    @Resource
    private SqlParser sqlParser;

    @Resource
    private LineFactory lineFactory;

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

        Line line = lineFactory.getLine(sql);

        DefaultContext context = new DefaultContext(sql, authorization, searcher);

        /**
         * 执行流水线.有以下分支.
         * 1. 拒绝.
         * 2. 异常.
         * 3. 通过.
         * 4. 更新后通过.
         */
        try {
            line.start(context);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(),ex);

            return new CheckResult(CheckStatus.ERROR.getValue(), ex.getMessage());
        }

        if (context.isRefused()) {

            CheckResult result = new CheckResult(CheckStatus.DENIAL.getValue());
            result.setMessage(context.cause());
            return result;

        } else {

            CheckResult result;
            if (context.isUpdatedSql()) {
                result = new CheckResult(CheckStatus.UPDATE.getValue());
                result.setNewSql(context.sql().toSqlString());
            } else {
                result = new CheckResult(CheckStatus.PASS.getValue());
            }

            if (context.blackSize() > 0) {

                List<String> blackList = new ArrayList(context.blackSize());
                ItemVisitor visitor = new BlackListItemVisitor(blackList);

                for (Item field : context.blackList()) {
                    field.visit(visitor);
                }

                result.setBackList(blackList);
            }

            return result;

        }

    }

    private static class BlackListItemVisitor extends ItemVisitorAdapter {
        private List<String> blackList;

        public BlackListItemVisitor(List<String> blackList) {
            this.blackList = blackList;
        }

        @Override
        public void visit(Field item) {
            if (item.hasAlias()) {
                blackList.add(item.getAlias().getName());
            } else {
                blackList.add(item.getName());
            }
        }

        @Override
        public void visit(Func item) {
            if (item.hasAlias()) {
                blackList.add(item.getAlias().getName());
            }
        }

        @Override
        public void visit(Arithmeitc item) {
            if (item.hasAlias()) {
                blackList.add(item.getAlias().getName());
            }
        }
    }
}
