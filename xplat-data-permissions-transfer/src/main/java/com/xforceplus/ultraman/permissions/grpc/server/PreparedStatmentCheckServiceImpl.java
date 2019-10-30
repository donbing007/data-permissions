package com.xforceplus.ultraman.permissions.grpc.server;

import com.xforceplus.ultraman.permissions.grpc.ForPreparedStatmentGrpc;
import com.xforceplus.ultraman.permissions.grpc.PreparedStatmentCheckServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @version 0.1 2019/10/24 09:56
 * @auth dongbin
 * @since 1.8
 */
public class PreparedStatmentCheckServiceImpl
    extends PreparedStatmentCheckServiceGrpc.PreparedStatmentCheckServiceImplBase {

    @Override
    public void check(ForPreparedStatmentGrpc.PreparedStatment request,
                      StreamObserver<ForPreparedStatmentGrpc.PreparedStatmentResult> responseObserver) {
        super.check(request, responseObserver);
    }
}
