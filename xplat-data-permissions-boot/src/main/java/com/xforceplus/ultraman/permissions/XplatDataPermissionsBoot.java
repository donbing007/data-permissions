package com.xforceplus.ultraman.permissions;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动主类.
 * @version 0.1 2019/11/14 10:19
 * @author dongbin
 * @since 1.8
 */
@SpringBootApplication
@MapperScan("com.xforceplus.ultraman.permissions.repository")
@EnableCaching
@EnableConfigurationProperties
@EnableSwagger2Doc
@EnableScheduling
public class XplatDataPermissionsBoot {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(XplatDataPermissionsBoot.class);
        app.run(args);
    }
}
