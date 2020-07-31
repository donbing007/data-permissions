// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Base.proto

package com.xforceplus.ultraman.permissions.transfer.grpc.generate;

public final class AuthorizationGrpc {
  private AuthorizationGrpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface AuthorizationOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.xforceplus.ultraman.permissions.Authorization)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 租房标识
     * </pre>
     *
     * <code>string tenant = 1;</code>
     */
    String getTenant();
    /**
     * <pre>
     * 租房标识
     * </pre>
     *
     * <code>string tenant = 1;</code>
     */
    com.google.protobuf.ByteString
        getTenantBytes();

    /**
     * <pre>
     * 角色标识.
     * </pre>
     *
     * <code>string role = 2;</code>
     */
    String getRole();
    /**
     * <pre>
     * 角色标识.
     * </pre>
     *
     * <code>string role = 2;</code>
     */
    com.google.protobuf.ByteString
        getRoleBytes();
  }
  /**
   * Protobuf type {@code com.xforceplus.ultraman.permissions.Authorization}
   */
  public  static final class Authorization extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.xforceplus.ultraman.permissions.Authorization)
      AuthorizationOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Authorization.newBuilder() to construct.
    private Authorization(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Authorization() {
      tenant_ = "";
      role_ = "";
    }

      @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Authorization(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
          throw new NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
                String s = input.readStringRequireUtf8();

              tenant_ = s;
              break;
            }
            case 18: {
                String s = input.readStringRequireUtf8();

              role_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
        return AuthorizationGrpc.internal_static_com_xforceplus_ultraman_permissions_Authorization_descriptor;
    }

      protected FieldAccessorTable
        internalGetFieldAccessorTable() {
          return AuthorizationGrpc.internal_static_com_xforceplus_ultraman_permissions_Authorization_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Authorization.class, Builder.class);
    }

    public static final int TENANT_FIELD_NUMBER = 1;
      private volatile Object tenant_;
    /**
     * <pre>
     * 租房标识
     * </pre>
     *
     * <code>string tenant = 1;</code>
     */
    public String getTenant() {
        Object ref = tenant_;
        if (ref instanceof String) {
            return (String) ref;
      } else {
            com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
            String s = bs.toStringUtf8();
        tenant_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 租房标识
     * </pre>
     *
     * <code>string tenant = 1;</code>
     */
    public com.google.protobuf.ByteString
        getTenantBytes() {
        Object ref = tenant_;
        if (ref instanceof String) {
            com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        tenant_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ROLE_FIELD_NUMBER = 2;
      private volatile Object role_;
    /**
     * <pre>
     * 角色标识.
     * </pre>
     *
     * <code>string role = 2;</code>
     */
    public String getRole() {
        Object ref = role_;
        if (ref instanceof String) {
            return (String) ref;
      } else {
            com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
            String s = bs.toStringUtf8();
        role_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 角色标识.
     * </pre>
     *
     * <code>string role = 2;</code>
     */
    public com.google.protobuf.ByteString
        getRoleBytes() {
        Object ref = role_;
        if (ref instanceof String) {
            com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        role_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getTenantBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, tenant_);
      }
      if (!getRoleBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, role_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getTenantBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, tenant_);
      }
      if (!getRoleBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, role_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

      @Override
      public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
          if (!(obj instanceof Authorization)) {
        return super.equals(obj);
      }
          Authorization other = (Authorization) obj;

      boolean result = true;
      result = result && getTenant()
          .equals(other.getTenant());
      result = result && getRole()
          .equals(other.getRole());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

      @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + TENANT_FIELD_NUMBER;
      hash = (53 * hash) + getTenant().hashCode();
      hash = (37 * hash) + ROLE_FIELD_NUMBER;
      hash = (53 * hash) + getRole().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

      public static Authorization parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

      public static Authorization parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

      public static Authorization parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

      public static Authorization parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

      public static Authorization parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }

      public static Authorization parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }

      public static Authorization parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }

      public static Authorization parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

      public static Authorization parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }

      public static Authorization parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

      public static Authorization parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }

      public static Authorization parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }

      public static Builder newBuilder(Authorization prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

      @Override
    protected Builder newBuilderForType(
          BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.xforceplus.ultraman.permissions.Authorization}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.xforceplus.ultraman.permissions.Authorization)
        AuthorizationOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
          return AuthorizationGrpc.internal_static_com_xforceplus_ultraman_permissions_Authorization_descriptor;
      }

        protected FieldAccessorTable
          internalGetFieldAccessorTable() {
            return AuthorizationGrpc.internal_static_com_xforceplus_ultraman_permissions_Authorization_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                Authorization.class, Builder.class);
      }

      // Construct using com.xforceplus.ultraman.permissions.transfer.grpc.generate.AuthorizationGrpc.Authorization.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        tenant_ = "";

        role_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
          return AuthorizationGrpc.internal_static_com_xforceplus_ultraman_permissions_Authorization_descriptor;
      }

        public Authorization getDefaultInstanceForType() {
            return Authorization.getDefaultInstance();
      }

        public Authorization build() {
            Authorization result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

        public Authorization buildPartial() {
            Authorization result = new Authorization(this);
        result.tenant_ = tenant_;
        result.role_ = role_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof Authorization) {
              return mergeFrom((Authorization) other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

        public Builder mergeFrom(Authorization other) {
            if (other == Authorization.getDefaultInstance()) return this;
        if (!other.getTenant().isEmpty()) {
          tenant_ = other.tenant_;
          onChanged();
        }
        if (!other.getRole().isEmpty()) {
          role_ = other.role_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
          Authorization parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (Authorization) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

        private Object tenant_ = "";
      /**
       * <pre>
       * 租房标识
       * </pre>
       *
       * <code>string tenant = 1;</code>
       */
      public String getTenant() {
          Object ref = tenant_;
          if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
              String s = bs.toStringUtf8();
          tenant_ = s;
          return s;
        } else {
              return (String) ref;
        }
      }
      /**
       * <pre>
       * 租房标识
       * </pre>
       *
       * <code>string tenant = 1;</code>
       */
      public com.google.protobuf.ByteString
          getTenantBytes() {
          Object ref = tenant_;
        if (ref instanceof String) {
            com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          tenant_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 租房标识
       * </pre>
       *
       * <code>string tenant = 1;</code>
       */
      public Builder setTenant(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        tenant_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 租房标识
       * </pre>
       *
       * <code>string tenant = 1;</code>
       */
      public Builder clearTenant() {

        tenant_ = getDefaultInstance().getTenant();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 租房标识
       * </pre>
       *
       * <code>string tenant = 1;</code>
       */
      public Builder setTenantBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        tenant_ = value;
        onChanged();
        return this;
      }

        private Object role_ = "";
      /**
       * <pre>
       * 角色标识.
       * </pre>
       *
       * <code>string role = 2;</code>
       */
      public String getRole() {
          Object ref = role_;
          if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
              String s = bs.toStringUtf8();
          role_ = s;
          return s;
        } else {
              return (String) ref;
        }
      }
      /**
       * <pre>
       * 角色标识.
       * </pre>
       *
       * <code>string role = 2;</code>
       */
      public com.google.protobuf.ByteString
          getRoleBytes() {
          Object ref = role_;
        if (ref instanceof String) {
            com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          role_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 角色标识.
       * </pre>
       *
       * <code>string role = 2;</code>
       */
      public Builder setRole(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        role_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 角色标识.
       * </pre>
       *
       * <code>string role = 2;</code>
       */
      public Builder clearRole() {

        role_ = getDefaultInstance().getRole();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 角色标识.
       * </pre>
       *
       * <code>string role = 2;</code>
       */
      public Builder setRoleBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        role_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.xforceplus.ultraman.permissions.Authorization)
    }

    // @@protoc_insertion_point(class_scope:com.xforceplus.ultraman.permissions.Authorization)
    private static final Authorization DEFAULT_INSTANCE;
    static {
        DEFAULT_INSTANCE = new Authorization();
    }

      public static Authorization getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Authorization>
        PARSER = new com.google.protobuf.AbstractParser<Authorization>() {
      public Authorization parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Authorization(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Authorization> parser() {
      return PARSER;
    }

      @Override
    public com.google.protobuf.Parser<Authorization> getParserForType() {
      return PARSER;
    }

      public Authorization getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_xforceplus_ultraman_permissions_Authorization_descriptor;
    private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_xforceplus_ultraman_permissions_Authorization_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
      String[] descriptorData = {
      "\n\nBase.proto\022#com.xforceplus.ultraman.pe" +
      "rmissions\"-\n\rAuthorization\022\016\n\006tenant\030\001 \001" +
      "(\t\022\014\n\004role\030\002 \001(\tBQ\n:com.xforceplus.ultra" +
      "man.permissions.transfer.grpc.generateB\021" +
      "AuthorizationGrpcH\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_xforceplus_ultraman_permissions_Authorization_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_xforceplus_ultraman_permissions_Authorization_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_xforceplus_ultraman_permissions_Authorization_descriptor,
        new String[]{"Tenant", "Role",});
  }

  // @@protoc_insertion_point(outer_class_scope)
}