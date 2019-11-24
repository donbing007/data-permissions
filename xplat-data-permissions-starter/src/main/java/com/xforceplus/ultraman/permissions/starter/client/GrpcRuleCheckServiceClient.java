package com.xforceplus.ultraman.permissions.starter.client;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.transfer.grpc.client.GrpcStatmentCheckClient;
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
    private GrpcStatmentCheckClient client;

    @Override
    public CheckResult check(String sql, Authorization authorization) {

        ForStatmentGrpc.StatmentResult statmentResult = client.check(sql, authorization);
        CheckResult checkResult = new CheckResult(CheckStatus.getInstance(statmentResult.getStatus()));

        SqlChange change = new SqlChange();
        change.setNewSql(statmentResult.getNewSql());
        change.setBlackList(statmentResult.getBackListList());

        checkResult.addValue(change);

        return checkResult;
    }
}
