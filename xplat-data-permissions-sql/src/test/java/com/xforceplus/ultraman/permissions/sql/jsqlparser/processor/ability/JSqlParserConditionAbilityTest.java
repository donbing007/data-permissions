package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.ArithmeticSymbol;
import com.xforceplus.ultraman.permissions.sql.define.values.DoubleValue;
import com.xforceplus.ultraman.permissions.sql.define.values.LongValue;
import com.xforceplus.ultraman.permissions.sql.define.values.NullValue;
import com.xforceplus.ultraman.permissions.sql.define.values.StringValue;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.Set;

/**
 * JSqlParserConditionHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/28/2019
 * @since <pre>Oct 28, 2019</pre>
 */
public class JSqlParserConditionAbilityTest {


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testAddCondition() throws Exception {

        Map<String, AddConditionPack> data = buildAddCase();
        Set<String> sqls = data.keySet();
        JSqlParserConditionAbility handler;
        AddConditionPack cp;
        Statement statement;
        for (String sql : sqls) {
            statement = CCJSqlParserUtil.parse(sql);
            handler = new JSqlParserConditionAbility(statement);
            cp = data.get(sql);

            handler.add(cp.newCondition, cp.conditional, cp.isolation);

            Assert.assertEquals(sql, cp.expected, statement.toString());
        }
    }

    private Map<String, AddConditionPack> buildAddCase() throws Exception {
        Map<String, AddConditionPack> data = new LinkedHashMap<>();
        data.put("select * from t1",
            new AddConditionPack(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                Conditional.AND,
                CCJSqlParserUtil.parse("select * from t1 where c1 = 10").toString(),
                false
            ));

        data.put("select * from t1 t where t.c1=10",
            new AddConditionPack(
                new Condition(new Field("t", "c1", null), ConditionOperator.BETWEEN, Arrays.asList(
                    new StringValue("v1"), new StringValue("v2"))),
                Conditional.OR,
                CCJSqlParserUtil.parse("select * from t1 t where (t.c1 = 10) or t.c1 between 'v1' and 'v2'").toString(),
                true
            ));

        data.put("delete from t1 where c1=10",
            new AddConditionPack(
                new Condition(new Field("c2"), ConditionOperator.IN, Arrays.asList(new LongValue(10), new LongValue(30))),
                Conditional.AND,
                CCJSqlParserUtil.parse("delete from t1 where c1=10 and c2 in (10, 30)").toString(),
                false
            ));

        data.put("select * from t1 where c1=2",
            new AddConditionPack(
                new Condition(
                    new Func("BIT_LENGHT", Arrays.asList(new Field("name")), null),
                    ConditionOperator.MINOR_THAN_EQUALS,
                    Arrays.asList(new LongValue(20))
                ),
                Conditional.OR,
                CCJSqlParserUtil.parse("select * from t1 where c1=2 or BIT_LENGHT(name)<=20").toString(),
                false
            ));

        return data;
    }

    private static class AddConditionPack {
        public Condition newCondition;
        public Conditional conditional;
        public String expected;
        public boolean isolation;

        public AddConditionPack(Condition newCondition, Conditional conditional, String expected, boolean isolation) {
            this.newCondition = newCondition;
            this.conditional = conditional;
            this.expected = expected;
            this.isolation = isolation;
        }
    }

    @Test
    public void testRemoveCondition() throws Exception {
        Map<String, RemoveConditionPack> caseData = buildRemoveCase();
        Set<String> sqls = caseData.keySet();
        JSqlParserConditionAbility handler;
        Statement statement;
        RemoveConditionPack pack;
        for (String sql : sqls) {
            statement = CCJSqlParserUtil.parse(sql);
            handler = new JSqlParserConditionAbility(statement);

            pack = caseData.get(sql);
            handler.remove(pack.removeCondition);

            Assert.assertEquals(sql, pack.expectedSql, statement.toString());
        }
    }

    private Map<String, RemoveConditionPack> buildRemoveCase() throws Exception {
        Map<String, RemoveConditionPack> data = new LinkedHashMap<>();

        data.put("select * from t1",
            new RemoveConditionPack(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                CCJSqlParserUtil.parse("select * from t1").toString()));

        data.put("select * from t1 where c1=10",
            new RemoveConditionPack(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                CCJSqlParserUtil.parse("select * from t1").toString()));

        data.put("select * from t1 where c1=10 and c2='10'",
            new RemoveConditionPack(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                CCJSqlParserUtil.parse("select * from t1 where c2='10'").toString()));

        data.put("select * from t1 where c1=10 and c2='10' or c3 like 'test%'",
            new RemoveConditionPack(
                new Condition(new Field("c3"), ConditionOperator.LIKE, new StringValue("test%")),
                CCJSqlParserUtil.parse("select * from t1 where c1=10 and c2='10'").toString()));

        data.put("select * from t1 where c1=10 and c2='10' or c3 is null",
            new RemoveConditionPack(
                new Condition(new Field("c3"), ConditionOperator.IS_NUll, NullValue.getInstance()),
                CCJSqlParserUtil.parse("select * from t1 where c1=10 and c2='10'").toString()));

        data.put("select * from t1 where c1=10 and c2='10' or c3 is null and c4=c1+1",
            new RemoveConditionPack(
                new Condition(
                    new Field("c4"),
                    ConditionOperator.EQUALS,
                    ConversionHelper.convertSmart(CCJSqlParserUtil.parseCondExpression("c1+1"))),
                CCJSqlParserUtil.parse("select * from t1 where c1=10 and c2='10' or c3 is null").toString()));

        data.put("select * from t1 where c1=2 or BIT_LENGHT(name)<=20",
            new RemoveConditionPack(
                new Condition(
                    new Func("BIT_LENGHT", Arrays.asList(new Field("name"))),
                    ConditionOperator.MINOR_THAN_EQUALS, new LongValue(20)),
                CCJSqlParserUtil.parse("select * from t1 where c1=2").toString()
            ));

        data.put("select * from t1 where c1=2+3 or BIT_LENGHT(name)<=20",
            new RemoveConditionPack(
                new Condition(
                    new Field("c1"), ConditionOperator.EQUALS,
                    new Arithmeitc(new LongValue(2), new LongValue(3), ArithmeticSymbol.ADDITION)
                ),
                CCJSqlParserUtil.parse("select * from t1 where BIT_LENGHT(name)<=20").toString()
            ));

        data.put("select group_code,group_name,company_type,company_type_desc,year_of_month,count(distinct case when " +
                "start_date between to_date(year_of_month || '-01', 'yyyy-mm-dd') and add_months(to_date(year_of_month || " +
                "'-01', 'yyyy-mm-dd'), 1) - 1 then tax_num end) bqzj,count(distinct case when end_date between to_date(year_" +
                "of_month || '-01', 'yyyy-mm-dd') and add_months(to_date(year_of_month || '-01', 'yyyy-mm-dd'), 1) - 1 " +
                "then tax_num end) bqjs from (select a.group_code,a.group_name,a.tax_num,c.company_type,case " +
                "when c.company_type = 'c' then '中心客户' when c.company_type = 's' then '供应商' when c.company_type = 'cs' " +
                "then '中心客户&供应商' end company_type_desc,year_of_month,min(start_date) start_date,max(end_date) " +
                "end_date from dim_company_business_info a left join (select year_of_month from dim_date where " +
                "year_of_month between '2018-02' and '2018-08' group by year_of_month) " +
                "b on 1 = 1 inner join dim_company_operation_info c on a.tax_num = " +
                "c.tax_num where start_date <= to_date(year_of_month || '-01', 'yyyy-mm-dd') and a.group_code = " +
                "'TAFUJI84255' and c.company_type = 's' group by a.group_code,a.group_name,a.tax_num,c.company_type,case " +
                "when c.company_type = 'c' then '中心客户' when c.company_type = 's' then '供应商' when c.company_type = " +
                "'cs' then '中心客户&供应商' end,year_of_month) m " +
                "where m.group_code='test' group by group_code,group_name,year_of_month,company_type,company_type_desc",
            new RemoveConditionPack(
                new Condition(new Field("m", "group_code", null), ConditionOperator.EQUALS, new StringValue("test")),
                CCJSqlParserUtil.parse("select group_code,group_name,company_type,company_type_desc,year_of_month,count(distinct case when " +
                    "start_date between to_date(year_of_month || '-01', 'yyyy-mm-dd') and add_months(to_date(year_of_month || " +
                    "'-01', 'yyyy-mm-dd'), 1) - 1 then tax_num end) bqzj,count(distinct case when end_date between to_date(year_" +
                    "of_month || '-01', 'yyyy-mm-dd') and add_months(to_date(year_of_month || '-01', 'yyyy-mm-dd'), 1) - 1 " +
                    "then tax_num end) bqjs from (select a.group_code,a.group_name,a.tax_num,c.company_type,case " +
                    "when c.company_type = 'c' then '中心客户' when c.company_type = 's' then '供应商' when c.company_type = 'cs' " +
                    "then '中心客户&供应商' end company_type_desc,year_of_month,min(start_date) start_date,max(end_date) " +
                    "end_date from dim_company_business_info a left join (select year_of_month from dim_date where " +
                    "year_of_month between '2018-02' and '2018-08' group by year_of_month) " +
                    "b on 1 = 1 inner join dim_company_operation_info c on a.tax_num = " +
                    "c.tax_num where start_date <= to_date(year_of_month || '-01', 'yyyy-mm-dd') and a.group_code = " +
                    "'TAFUJI84255' and c.company_type = 's' group by a.group_code,a.group_name,a.tax_num,c.company_type,case " +
                    "when c.company_type = 'c' then '中心客户' when c.company_type = 's' then '供应商' when c.company_type = " +
                    "'cs' then '中心客户&供应商' end,year_of_month) m " +
                    "group by group_code,group_name,year_of_month,company_type,company_type_desc").toString()
            )
        );

        return data;
    }

    private static class RemoveConditionPack {
        private Condition removeCondition;
        private String expectedSql;

        public RemoveConditionPack(Condition removeCondition, String expectedSql) {
            this.removeCondition = removeCondition;
            this.expectedSql = expectedSql;
        }
    }


    @Test
    public void testIterCondition() throws Exception {

        Map<String, List<Condition>> caseData = buildIterCase();
        Set<String> sqls = caseData.keySet();

        JSqlParserConditionAbility handler;
        for (String sql : sqls) {
            handler = new JSqlParserConditionAbility(CCJSqlParserUtil.parse(sql));

            List<Condition> current = handler.list();

            Assert.assertEquals(sql, caseData.get(sql).size(), current.size());
            List<Condition> expected = caseData.get(sql);
            for (int i = 0; i < expected.size(); i++) {
                Assert.assertEquals(sql, expected.get(i), current.get(i));
            }
        }

    }

    private Map<String, List<Condition>> buildIterCase() throws Exception {
        Map<String, List<Condition>> data = new LinkedHashMap<>();

        data.put("select * from t1", Collections.emptyList());
        data.put("select * from t1 where c1 = 10 and c2 != 'test' and c3 like 'test%' and c4 in ('1','2','3') and c5 between 20 and 50",
            Arrays.asList(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                new Condition(new Field("c2"), ConditionOperator.NOT_EQUALS, new StringValue("test")),
                new Condition(new Field("c3"), ConditionOperator.LIKE, new StringValue("test%")),
                new Condition(new Field("c4"), ConditionOperator.IN, Arrays.asList(new StringValue("1"), new StringValue("2"), new StringValue("3"))),
                new Condition(new Field("c5"), ConditionOperator.BETWEEN, Arrays.asList(new LongValue(20), new LongValue(50)))
            ));

        data.put("select * from t1 t where t.c1 = 10 and t.c2 != 'test' and t.c3 like 'test%' and t.c4 in ('1','2','3') and t.c5 between 20 and 50",
            Arrays.asList(
                new Condition(new Field("t", "c1", null), ConditionOperator.EQUALS, new LongValue(10)),
                new Condition(new Field("t", "c2", null), ConditionOperator.NOT_EQUALS, new StringValue("test")),
                new Condition(new Field("t", "c3", null), ConditionOperator.LIKE, new StringValue("test%")),
                new Condition(new Field("t", "c4", null), ConditionOperator.IN, Arrays.asList(new StringValue("1"), new StringValue("2"), new StringValue("3"))),
                new Condition(new Field("t", "c5", null), ConditionOperator.BETWEEN, Arrays.asList(new LongValue(20), new LongValue(50)))
            ));

        data.put("update t1 set c1=200 where  c1 = 10 and c2 != 'test' and c3 like 'test%' and c4 in ('1','2','3') and c5 between 20 and 50",
            Arrays.asList(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                new Condition(new Field("c2"), ConditionOperator.NOT_EQUALS, new StringValue("test")),
                new Condition(new Field("c3"), ConditionOperator.LIKE, new StringValue("test%")),
                new Condition(new Field("c4"), ConditionOperator.IN, Arrays.asList(new StringValue("1"), new StringValue("2"), new StringValue("3"))),
                new Condition(new Field("c5"), ConditionOperator.BETWEEN, Arrays.asList(new LongValue(20), new LongValue(50)))
            ));

        data.put("delete from t1 where  c1 = 10 and c2 != 'test' and c3 like 'test%' and c4 in ('1','2','3') and c5 between 20 and 50",
            Arrays.asList(
                new Condition(new Field("c1"), ConditionOperator.EQUALS, new LongValue(10)),
                new Condition(new Field("c2"), ConditionOperator.NOT_EQUALS, new StringValue("test")),
                new Condition(new Field("c3"), ConditionOperator.LIKE, new StringValue("test%")),
                new Condition(new Field("c4"), ConditionOperator.IN, Arrays.asList(new StringValue("1"), new StringValue("2"), new StringValue("3"))),
                new Condition(new Field("c5"), ConditionOperator.BETWEEN, Arrays.asList(new LongValue(20), new LongValue(50)))
            ));

        data.put("select * from t1 where a=a+1",
            Arrays.asList(
                new Condition(
                    new Field("a"),
                    ConditionOperator.EQUALS,
                    ConversionHelper.convertSmart(CCJSqlParserUtil.parseCondExpression("a+1")))
            ));

        data.put("select * from t1 where BIT_LENGTH(name) > 20",
            Arrays.asList(
                new Condition(
                    new Func("BIT_LENGTH",
                        Arrays.asList(new Field("name"))
                    ),
                    ConditionOperator.GREATER_THAN,
                    new LongValue(20)
                )
            ));

        data.put("select * from t1 where c1 not in (select * from t2) and c2 not in (1,2,3)",
            Arrays.asList(
                new Condition(
                    new Field("c2"),
                    ConditionOperator.NOT_IN,
                    Arrays.asList(new LongValue(1), new LongValue(2), new LongValue(3)))
            )
        );

        data.put("select * from t1 where c1 = any(select * from t2) and c2=1",
            Arrays.asList(
                new Condition(new Field("c2"), ConditionOperator.EQUALS, Arrays.asList(new LongValue(1)))
            )
        );

        data.put("select * from t1 where c1 = some(select * from t2) and c2=1",
            Arrays.asList(
                new Condition(new Field("c2"), ConditionOperator.EQUALS, Arrays.asList(new LongValue(1)))
            )
        );

        data.put("select * from t1 where exists (select * from t2 where c22=1)", Collections.emptyList());

        data.put("select * from t1 where c1=10 union select * from t2", Collections.emptyList());

        data.put("select * from t1 where c1=(1+2)*c4 and c2=c3+1 and c5=(3.2+c7)",
            Arrays.asList(
                new Condition(
                    new Field("c1"), ConditionOperator.EQUALS,
                    Arrays.asList(
                        new Arithmeitc(
                            new Parentheses(
                                new Arithmeitc(new LongValue(1), new LongValue(2), ArithmeticSymbol.ADDITION)
                            ),
                            new Field("c4"), ArithmeticSymbol.MULTIPLICATION)
                    )
                ),

                new Condition(
                    new Field("c2"), ConditionOperator.EQUALS,
                    Arrays.asList(
                        new Arithmeitc(new Field("c3"), new LongValue(1), ArithmeticSymbol.ADDITION)
                    )
                ),

                new Condition(
                    new Field("c5"), ConditionOperator.EQUALS,
                    Arrays.asList(
                        new Parentheses(
                            new Arithmeitc(new DoubleValue(3.2D), new Field("c7"), ArithmeticSymbol.ADDITION)
                        )
                    )
                )
            )
        );

        data.put("select group_code,group_name,company_type,company_type_desc,year_of_month,count(distinct case when " +
                "start_date between to_date(year_of_month || '-01', 'yyyy-mm-dd') and add_months(to_date(year_of_month || " +
                "'-01', 'yyyy-mm-dd'), 1) - 1 then tax_num end) bqzj,count(distinct case when end_date between to_date(year_" +
                "of_month || '-01', 'yyyy-mm-dd') and add_months(to_date(year_of_month || '-01', 'yyyy-mm-dd'), 1) - 1 " +
                "then tax_num end) bqjs from (select a.group_code,a.group_name,a.tax_num,c.company_type,case " +
                "when c.company_type = 'c' then '中心客户' when c.company_type = 's' then '供应商' when c.company_type = 'cs' " +
                "then '中心客户&供应商' end company_type_desc,year_of_month,min(start_date) start_date,max(end_date) " +
                "end_date from dim_company_business_info a left join (select year_of_month from dim_date where " +
                "year_of_month between '2018-02' and '2018-08' group by year_of_month) " +
                "b on 1 = 1 inner join dim_company_operation_info c on a.tax_num = " +
                "c.tax_num where start_date <= to_date(year_of_month || '-01', 'yyyy-mm-dd') and a.group_code = " +
                "'TAFUJI84255' and c.company_type = 's' group by a.group_code,a.group_name,a.tax_num,c.company_type,case " +
                "when c.company_type = 'c' then '中心客户' when c.company_type = 's' then '供应商' when c.company_type = " +
                "'cs' then '中心客户&供应商' end,year_of_month) m " +
                "where m.group_code='test' group by group_code,group_name,year_of_month,company_type,company_type_desc",
            Arrays.asList(
                new Condition(new Field("m", "group_code", null), ConditionOperator.EQUALS, new StringValue("test"))
            )
        );

        return data;
    }

} 
