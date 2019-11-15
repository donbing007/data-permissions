package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * checker test.
 */
public class AllFieldCannotUseCheckerTest {

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Test
    public void check() {

        AllFieldCannotUseChecker checker = new AllFieldCannotUseChecker();

        Map<String, Boolean> caseData = buildCase();
        caseData.keySet().stream().forEach(sql -> {

            DefaultContext context = new DefaultContext(sqlParser.parser(sql), auth);
            checker.check(context);
            Assert.assertEquals(sql, caseData.get(sql), context.isRefused());
        });

    }

    private Map<String, Boolean> buildCase() {
        Map<String, Boolean> data = new LinkedHashMap<>();

        data.put("select * from t1", true);
        data.put("select c1 from t1", false);
        data.put("select t.c1 from (select * from t1) t", true);
        data.put("select t.c1 from t union select * from t2", true);
        data.put(
            "select count(t1.c1), tt.c2 from t1 inner join (select t2.c1,t2.c2 from (select * from t2) t2) tt on tt.id=t.id",
            true);


        return data;
    }
}
