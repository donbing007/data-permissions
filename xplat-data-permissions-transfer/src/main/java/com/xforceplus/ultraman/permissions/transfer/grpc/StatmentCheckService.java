package com.xforceplus.ultraman.permissions.transfer.grpc;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.perissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.service.RuleCheckService;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.StatmentCheckServiceGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.UserGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import javax.annotation.Resource;

/**
 * @version 0.1 2019/11/14 17:54
 * @auth dongbin
 * @since 1.8
 */
@GRpcService
public class StatmentCheckService extends StatmentCheckServiceGrpc.StatmentCheckServiceImplBase {

    @Resource
    private RuleCheckService ruleCheckService;

    @Override
    public void check(ForStatmentGrpc.Statment request, StreamObserver<ForStatmentGrpc.StatmentResult> responseObserver) {
        String sql = request.getSql();
        UserGrpc.Role role = request.getRole();

        CheckResult result = ruleCheckService.check(sql, new Authorization(role.getId(), role.getTenant()));
        ForStatmentGrpc.StatmentResult.Builder builder = ForStatmentGrpc.StatmentResult.newBuilder();
        builder.setStatus(result.getCode());

        if (result.getBackList() != null) {
            int index = 0;
            for (String field : result.getBackList()) {
                builder.setBackList(index++, field);
            }
        }

        if (result.getNewSql() != null) {
            builder.setNewSql(result.getNewSql());
        }

        responseObserver.onNext(builder.build());

        responseObserver.onCompleted();

    }
}
