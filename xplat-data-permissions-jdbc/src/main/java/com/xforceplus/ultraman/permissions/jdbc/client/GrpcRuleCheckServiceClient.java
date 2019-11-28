package com.xforceplus.ultraman.permissions.jdbc.client;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.transfer.grpc.client.StatmentCheckClientGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;

import javax.annotation.Resource;

/**
 * grpc 实现.
 * @version 0.1 2019/11/15 14:42
 * @auth dongbin
 * @since 1.8
 */
public class GrpcRuleCheckServiceClient implements RuleCheckServiceClient {

    @Resource
    private StatmentCheckClientGrpc client;

    @Override
    public CheckResult check(String sql, Authorizations authorizations) {

        ForStatmentGrpc.StatmentResult statmentResult = client.check(sql, authorizations);
        CheckResult checkResult = new CheckResult(CheckStatus.getInstance(statmentResult.getStatus()));

        SqlChange change = new SqlChange();
        change.setNewSql(statmentResult.getNewSql());
        change.setBlackList(statmentResult.getBackListList());

        checkResult.addValue(change);

        return checkResult;
    }
}
