package com.xforceplus.ultraman.permissions.sql.jsqlparser.utils;

import com.xforceplus.ultraman.permissions.sql.define.values.DoubleValue;
import com.xforceplus.ultraman.permissions.sql.define.values.StringValue;
import com.xforceplus.ultraman.permissions.sql.define.values.UnknownValue;
import com.xforceplus.ultraman.permissions.sql.define.values.Value;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * ValueHelpery Tester.
 *
 * @author <Authors name>
 * @version 1.0 10/29/2019
 * @since <pre>Oct 29, 2019</pre>
 */
public class ValueHelperTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: buildValue(Expression value)
     */
    @Test
    public void testBuildValue() throws Exception {
        Map<Expression, Value> caseData = buildCase();

        caseData.keySet().stream().forEach(e -> {
            Assert.assertEquals(caseData.get(e), ConversionHelper.convertValue(e));
        });


    }

    private Map<Expression, Value> buildCase() throws Exception{
        Map<Expression, Value> data = new HashMap<>();

        data.put(CCJSqlParserUtil.parseExpression("10"),
            new com.xforceplus.ultraman.permissions.sql.define.values.LongValue(10));
        data.put(CCJSqlParserUtil.parseExpression("2.3"), new DoubleValue(2.3));
        data.put(CCJSqlParserUtil.parseExpression("'test'"), new StringValue("test"));
        data.put(CCJSqlParserUtil.parseExpression("NULL"),
            com.xforceplus.ultraman.permissions.sql.define.values.NullValue.getInstance());
        data.put(new HexValue("0x32"), UnknownValue.getInstance("0x32"));


        return data;

    }

    /**
     * Method: isValueExpr(Expression value)
     */
    @Test
    public void testIsValueExpr() throws Exception {
        LongValue okExpr = new LongValue("10");
        Assert.assertTrue(ValueHelper.isValueExpr(okExpr));

        AndExpression noExpr = new AndExpression(null, null);
        Assert.assertFalse(ValueHelper.isValueExpr(noExpr));
    }


} 
