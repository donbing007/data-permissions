package com.xforceplus.ultraman.permissions.jdbc.authorization;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;

/**
 * 授权信息搜索定义.
 * @version 0.1 2019/11/15 17:16
 * @author dongbin
 * @since 1.8
 */
public interface AuthorizationSearcher {

    /**
     * 搜索授权信息.
     * @return 授权信息.
     */
    Authorizations search();
}
