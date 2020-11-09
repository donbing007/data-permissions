package com.xforceplus.ultraman.permissions.jdbc.parser;

import com.google.common.collect.Sets;
import com.xforceplus.tenant.security.core.context.UserInfoHolder;
import com.xforceplus.tenant.security.core.domain.AuthorizedUser;
import com.xforceplus.ultraman.permissions.jdbc.parser.impl.TaxVariableParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 7:21 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserInfoHolder.class)
public class TaxVariableParserTest {

    @Test
    public void testParse() {
        AuthorizedUserService service = mock(AuthorizedUserService.class);
        when(service.getUserTaxNums(12L)).thenReturn(Sets.newHashSet("taxno1", "taxno2"));
        AuthorizedUser user = new AuthorizedUser();
        user.setId(12L);
        PowerMockito.mockStatic(UserInfoHolder.class);
        when(UserInfoHolder.get()).thenReturn(user);
        TaxVariableParser parser = new TaxVariableParser(service);
        String dest = parser.parse("select * from t1 where (t1.c1=5000) and (t1.c1 IN ('@TAX@'))");
        Assert.assertEquals(dest, "select * from t1 where (t1.c1=5000) and (t1.c1 IN ('taxno1','taxno2'))");
    }

    @Test
    public void testParse2() {
        AuthorizedUserService service = mock(AuthorizedUserService.class);
        when(service.getUserTaxNums(12L)).thenReturn(Sets.newHashSet("taxno1", "taxno2"));
        AuthorizedUser user = new AuthorizedUser();
        user.setId(12L);
        PowerMockito.mockStatic(UserInfoHolder.class);
        when(UserInfoHolder.get()).thenReturn(user);
        TaxVariableParser parser = new TaxVariableParser(service);
        String dest = parser.parse("SELECT IFNULL(t.authCode, '已勾选') AS authCode, it.invoiceType AS invoiceType, it.invoiceName, IFNULL(t.total, 0) AS total, IFNULL(t.tax_amount, 0) AS tax_amount, IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM (SELECT '已勾选' AS authCode, t1.invoice_type, COUNT(1) AS total, SUM(t1.tax_amount) AS tax_amount, SUM(t1.amount_without_tax) AS amount_without_tax FROM auth.aut_tax_invoice t1 WHERE (t1.auth_response_time >= STR_TO_DATE('2020-11-01', '%Y-%m-%d') AND t1.auth_response_time <= STR_TO_DATE('2020-11-30', '%Y-%m-%d') AND t1.auth_status IN (4, 7, 8)) AND (t1.purchaser_tax_no IN ('@TAX@')) GROUP BY t1.invoice_type) t RIGHT JOIN ((SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_period ts LIMIT 1) UNION ALL (SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_period tv LIMIT 1) UNION ALL (SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_period tct LIMIT 1) UNION ALL (SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_period thg LIMIT 1)) it ON t.invoice_type = it.invoiceType UNION ALL SELECT IFNULL(t.authCode, '未勾选') AS authCode, it.invoiceType AS invoiceType, it.invoiceName, IFNULL(t.total, 0) AS total, IFNULL(t.tax_amount, 0) AS tax_amount, IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM (SELECT '未勾选' AS authCode, t2.invoice_type, COUNT(1) AS total, SUM(t2.tax_amount) AS tax_amount, SUM(t2.amount_without_tax) AS amount_without_tax FROM auth.aut_tax_invoice t2 WHERE (t2.auth_status IN (0, 1, 2, 3, 5, 6)) AND (t2.purchaser_tax_no IN ('@TAX@')) GROUP BY t2.invoice_type) t RIGHT JOIN ((SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_period ts LIMIT 1) UNION ALL (SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_period tv LIMIT 1) UNION ALL (SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_period tct LIMIT 1) UNION ALL (SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_period thg LIMIT 1)) it ON t.invoice_type = it.invoiceType UNION ALL SELECT IFNULL(t.authCode, '不抵扣') AS authCode, it.invoiceType AS invoiceType, it.invoiceName, IFNULL(t.total, 0) AS total, IFNULL(t.tax_amount, 0) AS tax_amount, IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM (SELECT '不抵扣' AS authCode, t3.invoice_type, COUNT(1) AS total, SUM(t3.tax_amount) AS tax_amount, SUM(t3.amount_without_tax) AS amount_without_tax FROM auth.aut_tax_invoice t3 WHERE (t3.auth_response_time >= STR_TO_DATE('2020-11-01', '%Y-%m-%d') AND t3.auth_response_time <= STR_TO_DATE('2020-11-30', '%Y-%m-%d') AND t3.auth_use = 2) AND (t3.purchaser_tax_no IN ('@TAX@')) GROUP BY t3.invoice_type) t RIGHT JOIN ((SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_period ts LIMIT 1) UNION ALL (SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_period tv LIMIT 1) UNION ALL (SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_period tct LIMIT 1) UNION ALL (SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_period thg LIMIT 1)) it ON t.invoice_type = it.invoiceType");
        Assert.assertEquals("SELECT IFNULL(t.authCode, '已勾选') AS authCode, it.invoiceType AS invoiceType, it.invoiceName, IFNULL(t.total, 0) AS total, IFNULL(t.tax_amount, 0) AS tax_amount, IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM (SELECT '已勾选' AS authCode, t1.invoice_type, COUNT(1) AS total, SUM(t1.tax_amount) AS tax_amount, SUM(t1.amount_without_tax) AS amount_without_tax FROM auth.aut_tax_invoice t1 WHERE (t1.auth_response_time >= STR_TO_DATE('2020-11-01', '%Y-%m-%d') AND t1.auth_response_time <= STR_TO_DATE('2020-11-30', '%Y-%m-%d') AND t1.auth_status IN (4, 7, 8)) AND (t1.purchaser_tax_no IN ('taxno1','taxno2')) GROUP BY t1.invoice_type) t RIGHT JOIN ((SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_period ts LIMIT 1) UNION ALL (SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_period tv LIMIT 1) UNION ALL (SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_period tct LIMIT 1) UNION ALL (SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_period thg LIMIT 1)) it ON t.invoice_type = it.invoiceType UNION ALL SELECT IFNULL(t.authCode, '未勾选') AS authCode, it.invoiceType AS invoiceType, it.invoiceName, IFNULL(t.total, 0) AS total, IFNULL(t.tax_amount, 0) AS tax_amount, IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM (SELECT '未勾选' AS authCode, t2.invoice_type, COUNT(1) AS total, SUM(t2.tax_amount) AS tax_amount, SUM(t2.amount_without_tax) AS amount_without_tax FROM auth.aut_tax_invoice t2 WHERE (t2.auth_status IN (0, 1, 2, 3, 5, 6)) AND (t2.purchaser_tax_no IN ('taxno1','taxno2')) GROUP BY t2.invoice_type) t RIGHT JOIN ((SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_period ts LIMIT 1) UNION ALL (SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_period tv LIMIT 1) UNION ALL (SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_period tct LIMIT 1) UNION ALL (SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_period thg LIMIT 1)) it ON t.invoice_type = it.invoiceType UNION ALL SELECT IFNULL(t.authCode, '不抵扣') AS authCode, it.invoiceType AS invoiceType, it.invoiceName, IFNULL(t.total, 0) AS total, IFNULL(t.tax_amount, 0) AS tax_amount, IFNULL(t.amount_without_tax, 0) AS amount_without_tax FROM (SELECT '不抵扣' AS authCode, t3.invoice_type, COUNT(1) AS total, SUM(t3.tax_amount) AS tax_amount, SUM(t3.amount_without_tax) AS amount_without_tax FROM auth.aut_tax_invoice t3 WHERE (t3.auth_response_time >= STR_TO_DATE('2020-11-01', '%Y-%m-%d') AND t3.auth_response_time <= STR_TO_DATE('2020-11-30', '%Y-%m-%d') AND t3.auth_use = 2) AND (t3.purchaser_tax_no IN ('taxno1','taxno2')) GROUP BY t3.invoice_type) t RIGHT JOIN ((SELECT 's' AS invoiceType, '专用发票' AS invoiceName FROM auth.aut_period ts LIMIT 1) UNION ALL (SELECT 'v' AS invoiceType, '机动车统一销售发票' AS invoiceName FROM auth.aut_period tv LIMIT 1) UNION ALL (SELECT 'ct' AS invoiceType, '通行费电子发票' AS invoiceName FROM auth.aut_period tct LIMIT 1) UNION ALL (SELECT 'hg' AS invoiceType, '海关缴款文书' AS invoiceName FROM auth.aut_period thg LIMIT 1)) it ON t.invoice_type = it.invoiceType", dest);
    }


}
