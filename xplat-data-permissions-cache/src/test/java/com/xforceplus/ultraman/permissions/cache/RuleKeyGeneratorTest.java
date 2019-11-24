package com.xforceplus.ultraman.permissions.cache;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * RuleKeyGenerator Tester.
 *
 * @author <Authors name>
 * @version 1.0 11/24/2019
 * @since <pre>Nov 24, 2019</pre>
 */
public class RuleKeyGeneratorTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testKeyBuild() throws Exception {

        RuleKeyGenerator generator = new RuleKeyGenerator();
        FieldRuleTarget target = new FieldRuleTarget();

        String role = "r1";
        String tenant = "t1";
        String entity = "user";
        String key;
        key = (String) generator.generate(
            target,
            target.getClass().getMethod("successMethod0", new Class[]{Authorization.class, String.class}),
            new Authorization(role,tenant), entity);
        Assert.assertEquals("rule-field-t1-r1-user", key);

        key = (String) generator.generate(target,
            target.getClass().getMethod("successMethod1", new Class[]{Authorization.class, ParameterEntity.class}),
            new Authorization(role, tenant), new ParameterEntity(new Object(), entity));
        Assert.assertEquals("rule-field-t1-r1-user", key);

        key = (String) generator.generate(target,
            target.getClass().getMethod("successMethod2", new Class[]{Authorization.class, ParameterEntity.class}),
            new Authorization(role, tenant), new ParameterEntity(new Object(), entity));
        Assert.assertEquals("rule-field-t1-r1-user", key);

        try {
            key = (String) generator.generate(target,
                target.getClass().getMethod("noAuthorizationMethod0", new Class[]{String.class}),
                entity);
            Assert.fail("Should throw exception, cause not authorization!");
        } catch (Exception ex) {
        }

        try {
            key = (String) generator.generate(target,
                target.getClass().getMethod("noEntityMethod1", new Class[]{Authorization.class}),
                new Authorization(role, tenant));
            Assert.fail("Should throw exception, cause not entity!");
        } catch (Exception ex) {
        }

    }


    private static class FieldRuleTarget {

        public void successMethod0(Authorization authorization, String entity) {

        }

        public void successMethod1(Authorization authorization, ParameterEntity parameterEntity) {

        }

        public void successMethod2(Authorization authorization, ParameterEntity entity) {

        }

        public void noAuthorizationMethod0(String entity) {

        }

        public void noEntityMethod1(Authorization authorization) {

        }
    }

    private static class ParameterEntity {
        private Object p1;
        private String entity;

        public ParameterEntity(Object p1, String entity) {
            this.p1 = p1;
            this.entity = entity;
        }
    }

}
