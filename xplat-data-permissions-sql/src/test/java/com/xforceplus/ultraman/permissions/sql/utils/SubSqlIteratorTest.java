package com.xforceplus.ultraman.permissions.sql.utils;

import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSql;
import com.xforceplus.ultraman.permissions.sql.processor.DeleteSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SelectSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitorAdapter;
import com.xforceplus.ultraman.permissions.sql.processor.UpdateSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SubSqlAbility;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SubSqlIteratorTest {

    @Test
    public void iterator() throws Exception {
        Map<String, List<String>> caseData = buildListCase();
        caseData.keySet().stream().forEach(sql -> {

            try {
                new JSql(CCJSqlParserUtil.parse(sql)).visit(new SqlProcessorVisitorAdapter() {
                    @Override
                    public void visit(SelectSqlProcessor processor) {
                        check(processor.buildSubSqlAbility());
                    }

                    @Override
                    public void visit(DeleteSqlProcessor processor) {
                        check(processor.buildSubSqlAbility());
                    }

                    @Override
                    public void visit(UpdateSqlProcessor processor) {
                        check(processor.buildSubSqlAbility());
                    }

                    private void check(SubSqlAbility ability) {
                        SubSqlIterator iter = new SubSqlIterator(ability);

                        Map<String, Object> expected = caseData.get(sql).stream()
                            .collect(Collectors.toMap(Function.identity(), String::length));

                        while (iter.hasNext()) {
                            expected.remove(iter.next().toSqlString());
                        }

                        Assert.assertEquals(sql,0, expected.size());

                    }
                });

            } catch(Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        });

    }

    private Map<String, List<String>> buildListCase() throws Exception {
        Map<String, List<String>> data = new LinkedHashMap<>();

        data.put("select * from t1",
            Arrays.asList()
        );

        data.put("select * from (select * from t1) t",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from (select * from t1) t union select * from t2",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from (select * from t1) t").toString(),
                CCJSqlParserUtil.parse("select * from t2").toString(),
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from (select * from t1) t union select * from t2 union all select * from t3",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from (select * from t1) t").toString(),
                CCJSqlParserUtil.parse("select * from t2").toString(),
                CCJSqlParserUtil.parse("select * from t3").toString(),
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from (select * from (select * from t1)) t",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from (select * from t1)").toString(),
                CCJSqlParserUtil.parse("select * from t1").toString()
            )
        );

        data.put("select * from t where t.c1 in (select * from t2 union select * from t3)",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from t2").toString(),
                CCJSqlParserUtil.parse("select * from t3").toString()
            )
        );

        data.put("update t1, (select * from t2 union select * from t3) t2 set t1.id=t2",
            Arrays.asList(
                CCJSqlParserUtil.parse("select * from t2").toString(),
                CCJSqlParserUtil.parse("select * from t3").toString()
            )
        );

        return data;
    }

}