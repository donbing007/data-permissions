package com.xforceplus.ultraman.permissions.grpc.server;

import com.xforceplus.ultraman.permissions.grpc.ForStatmentGrpc;
import com.xforceplus.ultraman.permissions.grpc.StatmentCheckServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @version 0.1 2019/10/24 09:56
 * @auth dongbin
 * @since 1.8
 */
public class StatmentCheckServiceImpl extends StatmentCheckServiceGrpc.StatmentCheckServiceImplBase {
    @Override
    public void check(ForStatmentGrpc.Statment request,
                      StreamObserver<ForStatmentGrpc.StatmentResult> responseObserver) {

    }
}
