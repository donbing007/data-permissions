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
public class CanNotAllowSubCheckerTest {

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Test
    public void check() {

        CanNotAllowSubChecker checker = new CanNotAllowSubChecker();

        Map<String, Boolean> caseData = buildCase();
        caseData.keySet().stream().forEach(sql -> {

            DefaultContext context = new DefaultContext(sqlParser.parser(sql), auth);
            checker.check(context);

            Assert.assertEquals(sql, caseData.get(sql), context.isRefused());
        });
    }

    private Map<String, Boolean> buildCase() {
        Map<String, Boolean> data = new LinkedHashMap<>();

        data.put("update t1 set t1.c1=10", false);
        data.put("update t1 set t1.c1=10 where t1.c2 in (select t2.id from t2)", true);
        data.put("delete from t1 where t1.c1=10", false);
        data.put("delete from t1 where t1.c1 in (select t2.c1 from t2)", true);


        return data;
    }
}
