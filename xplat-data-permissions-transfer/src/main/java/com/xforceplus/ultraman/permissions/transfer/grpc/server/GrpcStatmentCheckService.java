package com.xforceplus.ultraman.permissions.transfer.grpc.server;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.service.RuleCheckService;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc;
import com.xforceplus.ultraman.permissions.transfer.grpc.generate.StatmentCheckServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @version 0.1 2019/11/14 17:54
 * @author dongbin
 * @since 1.8
 */
@GRpcService
public class GrpcStatmentCheckService extends StatmentCheckServiceGrpc.StatmentCheckServiceImplBase {

    @Resource
    private RuleCheckService ruleCheckService;

    @Override
    public void check(ForStatmentGrpc.Statment request, StreamObserver<ForStatmentGrpc.StatmentResult> responseObserver) {
        String sql = request.getSql();
        List<com.xforceplus.ultraman.permissions.transfer.grpc.generate.AuthorizationGrpc.Authorization> authorizationList
            = request.getAuthorizationList();

        Authorizations authorizations = new Authorizations(authorizationList.parallelStream().map(
            a -> new Authorization(a.getRole(), a.getTenant())).collect(Collectors.toList()));

        CheckResult result = ruleCheckService.check(sql, authorizations);
        ForStatmentGrpc.StatmentResult.Builder builder = ForStatmentGrpc.StatmentResult.newBuilder();
        builder.setStatus(result.getStatus().getValue());

        Optional<SqlChange> sqlChangeOptional = result.streamValues().findFirst();
        if (sqlChangeOptional.isPresent()) {
            SqlChange change = sqlChangeOptional.get();
            if (change.getBlackList() != null) {
                for (String field : change.getBlackList()) {
                    builder.addBackList(field);
                }
            }

            if (change.getNewSql() != null) {
                builder.setNewSql(change.getNewSql());
            }
        }

        responseObserver.onNext(builder.build());

        responseObserver.onCompleted();

    }
}
