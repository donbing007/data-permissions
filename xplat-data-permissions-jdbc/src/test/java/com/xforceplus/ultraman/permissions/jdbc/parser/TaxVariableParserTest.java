package com.xforceplus.ultraman.permissions.jdbc.parser;

import com.google.common.collect.Sets;
import com.xforceplus.tenant.security.core.context.UserInfoHolder;
import com.xforceplus.tenant.security.core.domain.AuthorizedUser;
import com.xforceplus.ultraman.permissions.jdbc.parser.impl.AuthorizedUserServiceImpl;
import com.xforceplus.ultraman.permissions.jdbc.parser.impl.TaxVariableParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 7:21 PM
 */
public class TaxVariableParserTest {

    @Test
    public void testParse() {
        AuthorizedUserService service = mock(AuthorizedUserService.class);
        when(service.getUserTaxNums("u1")).thenReturn(Sets.newHashSet("taxno1", "taxno2"));
        AuthorizedUser user = new AuthorizedUser();
        user.setUserCode("u1");
        try (MockedStatic<UserInfoHolder> theMock = Mockito.mockStatic(UserInfoHolder.class)) {
            ((MockedStatic) theMock).when(UserInfoHolder::get).thenReturn(user);
        }
        TaxVariableParser parser = new TaxVariableParser(service);
        String dest = parser.parse("select * from t1 where (t1.c1=5000) and (t1.c1 IN ('@TAX@'))");
        Assert.assertEquals(dest, "select * from t1 where (t1.c1=5000) and (t1.c1 IN ('taxno1','taxno2'))");
    }
}
