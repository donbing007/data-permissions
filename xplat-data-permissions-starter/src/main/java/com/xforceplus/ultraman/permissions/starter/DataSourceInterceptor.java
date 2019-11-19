package com.xforceplus.ultraman.permissions.starter;

import com.xforceplus.ultraman.permissions.starter.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.starter.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.jdbc.PermissionsDataSourceWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * data source 拦截器.
 * @version 0.1 2019/11/15 15:29
 * @auth dongbin
 * @since 1.8
 */
public class DataSourceInterceptor implements BeanPostProcessor {

    @Resource
    private RuleCheckServiceClient client;

    @Resource
    private AuthorizationSearcher authorizationSearcher;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (DataSource.class.isInstance(bean)) {
            return new PermissionsDataSourceWrapper(client, authorizationSearcher, (DataSource) bean);
        } else {
            return bean;
        }
    }
}
