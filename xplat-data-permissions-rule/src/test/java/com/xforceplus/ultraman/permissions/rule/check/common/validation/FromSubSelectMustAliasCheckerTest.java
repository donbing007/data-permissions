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
 * checer test.
 */
public class FromSubSelectMustAliasCheckerTest {

    private Authorization auth = new Authorization("r1", "t1");
    private SqlParser sqlParser = JSqlParser.getInstance();

    @Test
    public void check() {

        FromSubSelectMustAliasChecker checker = new FromSubSelectMustAliasChecker();
        Map<String, Boolean> caseData = buildCase();
        caseData.keySet().stream().forEach(sql -> {

            DefaultContext context = new DefaultContext(sqlParser.parser(sql), auth);
            checker.check(context);

            Assert.assertEquals(sql, caseData.get(sql), context.isRefused());

        });
    }

    public Map<String, Boolean> buildCase() {
        // key=sql value = is refused.
        Map<String, Boolean> data = new LinkedHashMap<>();

        data.put("select t1.c1 from t1", false);
        data.put("select t.c1 from (select t.c1 from t1)", true);
        data.put("select t.c1 from (select t1.c1 from t1) t", false);
        data.put("select t.c1 from t1 t union select tt.c2 from t2 tt", false);
        data.put("select t.c1 from (select t.c1 from (select t.c1 from t1)) t", true);
        data.put("select t.c1 from (select t.c1 from (select t.c1 from t1) t) t", false);

        return data;
    }
}
