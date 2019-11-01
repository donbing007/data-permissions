package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.values.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;

/**
 * JSqlParserFromHandler Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/28/2019
 * @since <pre>Oct 28, 2019</pre>
 */
public class JSqlParserFromHandlerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testIterFrom() throws Exception {

        Map<String,List<From>> data = buildCase();
        Set<String> sqls = data.keySet();
        JSqlParserFromHandler handler;
        for (String sql : sqls) {
            handler = new JSqlParserFromHandler(CCJSqlParserUtil.parse(sql));
            List<From> froms = handler.list();
            List<From> expected = data.get(sql);

            Assert.assertEquals(sql, expected.size(), froms.size());

            if (expected.isEmpty()) {
                continue;
            }

            Iterator<From> fromIterator = froms.iterator();

            int index = 0;
            while(fromIterator.hasNext()) {
                Assert.assertEquals(sql, expected.get(index++), fromIterator.next());
            }
        }
    }

    private Map<String, List<From>> buildCase() {

        Map<String,List<From>> data = new HashMap();
        data.put("select * from t1",
            Arrays.asList(
                new From("t1")
            ));

        data.put("select * from t1 t",
            Arrays.asList(
                new From("t1", new Alias("t"))
            ));

        data.put("select * from t1 a1 inner join t2 a2",
            Arrays.asList(
                new From("t1", new Alias("a1")),
                new From("t2", new Alias("a2"))
            ));

        data.put("select * from (select * from t1) t",
            Arrays.asList(
                new From("", new Alias("t"), true)
            ));

        data.put("select * from (select * from t2) t2 inner join t1 on t1.id=t2.id",
            Arrays.asList(
                new From("", new Alias("t2"), true),
                new From("t1")
            )
        );

        data.put("select * from t1 union select * from t2",
            Arrays.asList(
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
                new From("", new Alias("m"),true)
            )
        );

        return data;
    }
} 
