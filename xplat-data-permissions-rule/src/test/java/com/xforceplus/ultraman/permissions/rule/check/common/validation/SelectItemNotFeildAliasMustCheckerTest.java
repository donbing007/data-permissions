package com.xforceplus.ultraman.permissions.rule.check.common.validation;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.permissions.rule.context.DefaultContext;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class SelectItemNotFeildAliasMustCheckerTest {

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Test
    public void check() {

        SelectItemNotFeildAliasMustChecker checker = new SelectItemNotFeildAliasMustChecker();

        Map<String, Boolean> caseData = buildCase();
        caseData.keySet().stream().forEach(sql -> {

            DefaultContext context = new DefaultContext(sqlParser.parser(sql), auth);
            checker.check(context);

            Assert.assertEquals(sql, caseData.get(sql), context.isRefused());
        });
    }

    private Map<String, Boolean> buildCase() {
        Map<String, Boolean> data = new LinkedHashMap<>();

        data.put("select t1.c1 from t1", false);
        data.put("select t.c1 from (select func(t1.c1,t2.c2) from t1) t", true);
        data.put("select 1+2 from t1 union select c2 from t2", true);

        return data;
    }
}