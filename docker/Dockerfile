FROM registry.cn-hangzhou.aliyuncs.com/xforceplus/image-javabase:0.2.1

LABEL maintainer="dongbin <dongbin@xforceplus.com>"

RUN mkdir /xplat
WORKDIR /xplat

COPY app.jar /xplat/xplat-data-permissions.jar

EXPOSE 8080
EXPOSE 8206

ENTRYPOINT java $JAVA_OPTS -jar /xplat/xplat-data-permissions.jar