package com.xforceplus.ultraman.permissions.repository;

import com.xforceplus.ultraman.permissions.repository.entity.*;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

/**
 * @version 0.1 2019/11/6 09:59
 * @author dongbin
 * @since 1.8
 */
public class ScopeSelectRepositoryTest {

    private SqlSessionFactory sessionFactory;
    private EmbeddedDatabase dataSource;
    private SqlSession session;

    @Before
    public void before() throws Exception {
        dataSource = buildDataSource();

        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        Environment environment = new Environment("test", transactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.addMappers("com.xforceplus.ultraman.permissions.repository");

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        sessionFactory = builder.build(configuration);
        session = sessionFactory.openSession(true);
    }

    @After
    public void after() throws Exception {

        if (session != null) {
            session.close();
        }

        if (dataSource != null) {
            dataSource.shutdown();
            dataSource = null;
        }
    }

    @Test
    public void testSelectColumnScope() throws Exception {

        ScopeSelectRepository repository = session.getMapper(ScopeSelectRepository.class);

        SelectScopeExample selectScopeExample = new SelectScopeExample();
        selectScopeExample.setRoleId("r1");
        selectScopeExample.setTenantId("t1");
        selectScopeExample.setEntity("t1");
        List<FieldScope> fieldScopes = repository.selectFieldScopeByExample(selectScopeExample);


        FieldScopeRepository columnScopeRepository = session.getMapper(FieldScopeRepository.class);
        FieldScopeExample columnScopeExample = new FieldScopeExample();
        columnScopeExample.createCriteria().andEntityEqualTo("t1");
        columnScopeExample.setOrderByClause("id asc");

        List<FieldScope> expected = columnScopeRepository.selectByExample(columnScopeExample);

        Assert.assertEquals(fieldScopes.toString(),expected.size(), fieldScopes.size());
        Assert.assertEquals(expected.toString(), fieldScopes.toString());
    }

    @Test
    public void testSelectDataScope() throws Exception {
        ScopeSelectRepository repository = session.getMapper(ScopeSelectRepository.class);
        SelectScopeExample selectDataScopeExample = new SelectScopeExample();
        selectDataScopeExample.setRoleId("r1");
        selectDataScopeExample.setTenantId("t1");
        selectDataScopeExample.setEntity("t1");
        selectDataScopeExample.setField("c1");
        List<DataScopeSubCondition> conditions = repository.selectDataScopeConditionsByExample(selectDataScopeExample);

        DataScopeRepository dataScopeRepository = session.getMapper(DataScopeRepository.class);
        DataScopeExample dataScopeExample = new DataScopeExample();
        dataScopeExample.createCriteria().andEntityEqualTo("t1");
        DataScope dataScope = dataScopeRepository.selectByExample(dataScopeExample).get(0);

        DataScopeConditionsRepository dataScopeConditionsRepository =
            session.getMapper(DataScopeConditionsRepository.class);

        DataScopeConditionsExample dataScopeConditionsExample = new DataScopeConditionsExample();
        dataScopeConditionsExample.createCriteria()
            .andDataScopeIdEqualTo(dataScope.getId())
            .andFieldEqualTo("c1");
        DataScopeConditions dataScopeConditions =
            dataScopeConditionsRepository.selectByExample(dataScopeConditionsExample).get(0);

        DataScopeSubConditionRepository dataScopeSubConditionRepository =
            session.getMapper(DataScopeSubConditionRepository.class);
        DataScopeSubConditionExample dataScopeSubConditionExample = new DataScopeSubConditionExample();
        dataScopeSubConditionExample.createCriteria()
            .andConditionsIdEqualTo(dataScopeConditions.getId());
        dataScopeSubConditionExample.setOrderByClause("index asc");
        List<DataScopeSubCondition> expected =
            dataScopeSubConditionRepository.selectByExample(dataScopeSubConditionExample);

        Assert.assertEquals(conditions.toString(), expected.size(), conditions.size());
        Assert.assertEquals(expected.toString(), conditions.toString());
    }

    private EmbeddedDatabase buildDataSource() throws Exception {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
            .setName("MODE=MySQL;DB_CLOSE_ON_EXIT=false")
            .setScriptEncoding("utf8")
            .addScripts("classpath:/db/schema/mysql.sql")
            .addScripts("classpath:/db/data/scope-select.sql")
            .build();
    }

}
