package com.xforceplus.ultraman.permissions.transfer.grpc.generate;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: ForStatment.proto")
public final class StatmentCheckServiceGrpc {

  private StatmentCheckServiceGrpc() {}

  public static final String SERVICE_NAME = "com.xforceplus.ultraman.permissions.StatmentCheckService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment,
      com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult> getCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "check",
      requestType = com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment.class,
      responseType = com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment,
      com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult> getCheckMethod() {
    io.grpc.MethodDescriptor<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment, com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult> getCheckMethod;
    if ((getCheckMethod = StatmentCheckServiceGrpc.getCheckMethod) == null) {
      synchronized (StatmentCheckServiceGrpc.class) {
        if ((getCheckMethod = StatmentCheckServiceGrpc.getCheckMethod) == null) {
          StatmentCheckServiceGrpc.getCheckMethod = getCheckMethod = 
              io.grpc.MethodDescriptor.<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment, com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.xforceplus.ultraman.permissions.StatmentCheckService", "check"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult.getDefaultInstance()))
                  .setSchemaDescriptor(new StatmentCheckServiceMethodDescriptorSupplier("check"))
                  .build();
          }
        }
     }
     return getCheckMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StatmentCheckServiceStub newStub(io.grpc.Channel channel) {
    return new StatmentCheckServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StatmentCheckServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StatmentCheckServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StatmentCheckServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StatmentCheckServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class StatmentCheckServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void check(com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment request,
        io.grpc.stub.StreamObserver<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCheckMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment,
                com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult>(
                  this, METHODID_CHECK)))
          .build();
    }
  }

  /**
   */
  public static final class StatmentCheckServiceStub extends io.grpc.stub.AbstractStub<StatmentCheckServiceStub> {
    private StatmentCheckServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StatmentCheckServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatmentCheckServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StatmentCheckServiceStub(channel, callOptions);
    }

    /**
     */
    public void check(com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment request,
        io.grpc.stub.StreamObserver<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StatmentCheckServiceBlockingStub extends io.grpc.stub.AbstractStub<StatmentCheckServiceBlockingStub> {
    private StatmentCheckServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StatmentCheckServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatmentCheckServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StatmentCheckServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult check(com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment request) {
      return blockingUnaryCall(
          getChannel(), getCheckMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StatmentCheckServiceFutureStub extends io.grpc.stub.AbstractStub<StatmentCheckServiceFutureStub> {
    private StatmentCheckServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StatmentCheckServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatmentCheckServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StatmentCheckServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult> check(
        com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StatmentCheckServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StatmentCheckServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK:
          serviceImpl.check((com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.Statment) request,
              (io.grpc.stub.StreamObserver<com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.StatmentResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StatmentCheckServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StatmentCheckServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.xforceplus.ultraman.permissions.transfer.grpc.generate.ForStatmentGrpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StatmentCheckService");
    }
  }

  private static final class StatmentCheckServiceFileDescriptorSupplier
      extends StatmentCheckServiceBaseDescriptorSupplier {
    StatmentCheckServiceFileDescriptorSupplier() {}
  }

  private static final class StatmentCheckServiceMethodDescriptorSupplier
      extends StatmentCheckServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StatmentCheckServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StatmentCheckServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StatmentCheckServiceFileDescriptorSupplier())
              .addMethod(getCheckMethod())
              .build();
        }
      }
    }
    return result;
  }
}
