package com.xforceplus.ultraman.permissions.service.impl;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.check.SqlChange;
import com.xforceplus.ultraman.permissions.pojo.result.CheckStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.rule.assembly.Line;
import com.xforceplus.ultraman.permissions.rule.assembly.LineFactory;
import com.xforceplus.ultraman.permissions.rule.context.Context;
import com.xforceplus.ultraman.permissions.service.AbstractBaseTest;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.define.Alias;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import net.sf.jsqlparser.JSQLParserException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

/**
 * RuleCheckServiceImpl Tester.
 *
 * @author dongbin
 * @version 1.0 11/13/2019
 * @since <pre>Nov 13, 2019</pre>
 */
public class RuleCheckServiceImplTest extends AbstractBaseTest {

    private SqlParser sqlParser = JSqlParser.getInstance();
    private Authorizations authorizations = new Authorizations(Arrays.asList(new Authorization("r1", "t1")));

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: check(String sqlStr, Authorization authorization)
     */
    @Test
    public void testCheck() throws Exception {
        Map<String, Pack> caseData = buildCase();

        caseData.keySet().stream().forEach(sql -> {
            RuleCheckServiceImpl impl = new RuleCheckServiceImpl();
            Pack pack = caseData.get(sql);

            injectField(impl, "lineFactory", pack.lineFactory);
            injectField(impl, "sqlParser", JSqlParser.getInstance());

            CheckResult result = impl.check(sql, authorizations);
            Assert.assertEquals(pack.expectedResult, result);

        });
    }

    private static class Pack {
        private LineFactory lineFactory;
        private CheckResult expectedResult;

        public Pack(LineFactory lineFactory, CheckResult expectedResult) {
            this.lineFactory = lineFactory;
            this.expectedResult = expectedResult;
        }
    }

    private Map<String, Pack> buildCase() throws JSQLParserException {
        Map<String, Pack> data = new LinkedHashMap<>();
        data.put(
            "select t1.c1 from t1",
            new Pack(
                new LineFactory(Arrays.asList(new MockLine(
                    new SqlType[]{SqlType.SELECT},
                    CheckStatus.DENIAL,
                    "",
                    Collections.emptyList(),
                    sqlParser.parser("select t1.c1 from t1")
                ))),
                new CheckResult(CheckStatus.DENIAL, "")
            )
        );

        data.put(
            "select t1.c2 from t1",
            new Pack(
                new LineFactory(Arrays.asList(new MockLine(
                    new SqlType[]{SqlType.SELECT},
                    CheckStatus.PASS,
                    "",
                    Collections.emptyList(),
                    null
                ))),
                new CheckResult(CheckStatus.PASS, "")
            )
        );

        data.put(
            "select t1.c1 from t1",
            new Pack(
                new LineFactory(Arrays.asList(new MockLine(
                    new SqlType[]{SqlType.SELECT},
                    CheckStatus.UPDATE,
                    "",
                    Collections.emptyList(),
                    sqlParser.parser("select t1.c1 from t2")
                ))),
                new CheckResult(CheckStatus.UPDATE,
                    new SqlChange(sqlParser.parser("select t1.c1 from t2").toSqlString()),"")
            )
        );

        data.put(
            "select t1.c2,count(1) count from t1",
            new Pack(
                new LineFactory(Arrays.asList(new MockLine(
                    new SqlType[]{SqlType.SELECT},
                    CheckStatus.UPDATE,
                    "",
                    Arrays.asList(
                        new Func("count", Arrays.asList(new LongValue(1)),new Alias("count"))
                    ),
                    sqlParser.parser("select t1.c2 from t2")
                ))),
                new CheckResult(CheckStatus.UPDATE, new SqlChange(
                    sqlParser.parser("select t1.c2 from t2").toSqlString(),
                    Arrays.asList("count")),"")
            )
        );

        return data;
    }

    private static class MockLine implements Line {

        private SqlType[] types;
        private CheckStatus status;
        private String msg;
        private List<Item> blackList;
        private Sql newSql;

        public MockLine(SqlType[] types, CheckStatus status, String msg, List<Item> blackList, Sql newSql) {
            this.types = types;
            this.status = status;
            this.msg = msg;
            this.blackList = blackList;
            this.newSql = newSql;
        }

        @Override
        public void start(Context context) throws Throwable {
            switch (status) {
                case DENIAL: {
                    context.refused(msg);
                    break;
                }
                case UPDATE: {
                    context.updateSql(newSql);
                    break;
                }
            }

            if (blackList != null) {
                blackList.stream().forEach(i -> context.black(i));
            }
        }

        @Override
        public boolean isSupport(Sql sql) {
            for (SqlType type : types) {
                if (sql.type() == type) {
                    return true;
                }
            }

            return false;
        }
    }

}
