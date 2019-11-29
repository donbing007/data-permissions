package com.xforceplus.ultraman.permissions.starter;

import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.jdbc.client.RuleCheckServiceClient;
import com.xforceplus.ultraman.permissions.starter.define.BeanNameDefine;
import com.xforceplus.ultraman.permissions.starter.jdbc.PermissionsDataSourceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;

/**
 * DataSource 包装者.用以将非 Spring 托管的 DataSource 进行增强.
 *
 * @author dongbin
 * @version 0.1 2019/11/29 13:26
 * @since 1.8
 */
public class DataSourceWrapper implements ApplicationContextAware {

    static final Logger logger = LoggerFactory.getLogger(DataSourceWrapper.class);

    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (APPLICATION_CONTEXT == null) {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    /**
     * 包装一个数据源,不支持二次包装.
     *
     * @param dataSource 目标的 DataSource.
     * @return 包装后的 DataSource.
     */
    public static DataSource wrap(DataSource dataSource) {
        if (PermissionsDataSourceWrapper.class.isInstance(dataSource)) {

            PermissionsDataSourceWrapper wrapper = (PermissionsDataSourceWrapper) dataSource;

            logger.error(
                "Cannot enhance the already enhanced data source, the original data source type is {}..",
                wrapper.getOriginal().getClass().getName());

            return dataSource;

        }

        RuleCheckServiceClient ruleClient = (RuleCheckServiceClient) APPLICATION_CONTEXT.getBean(
            BeanNameDefine.RULE_CHECK_CLIENT);

        if (ruleClient == null) {
            throw new IllegalStateException("Did not find the right \"RuleCheckServiceClient\" instance.");
        }

        AuthorizationSearcher authorizationSearcher = (AuthorizationSearcher) APPLICATION_CONTEXT.getBean(
            BeanNameDefine.AUTHORIZATION_SEARCHER);

        if (authorizationSearcher == null) {
            throw new IllegalStateException("Did not find the right \"AuthorizationSearcher\" instance.");
        }

        PermissionsDataSourceWrapper wrapper =
            new PermissionsDataSourceWrapper(ruleClient, authorizationSearcher, dataSource);


        logger.info("Success enhancement {}.", dataSource.getClass().getName());

        return wrapper;
    }
}
