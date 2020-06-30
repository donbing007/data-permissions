package com.xforceplus.ultraman.permissions.jdbc.authorization.impl;

import com.xforceplus.tenant.security.core.context.UserInfoHolder;
import com.xforceplus.tenant.security.core.domain.IAuthorizedUser;
import com.xforceplus.tenant.security.token.domain.IRole;
import com.xforceplus.ultraman.permissions.jdbc.authorization.AuthorizationSearcher;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 依赖用户中心的授权信息上下文传递. 从线程上下文中拿取授权信息.
 *
 * @author dongbin
 * @version 0.1 2019/11/19 15:38
 * @since 1.8
 */
public class ContextAuthorizationSearcher implements AuthorizationSearcher {

  @Override
  public Authorizations search() {

    IAuthorizedUser authorizedUser = UserInfoHolder.get();
    Set<IRole> roles = null;
    if (authorizedUser == null) {
      roles = Collections.emptySet();
    } else {
      roles = authorizedUser.getRoles();
    }

    List<Authorization> authorizations;
    if (roles != null) {
      authorizations = roles.stream()
          .map(r -> new Authorization(Long.toString(r.getId()), Long.toString(r.getTenantId())))
          .collect(Collectors.toList());
    } else {
      authorizations = Collections.emptyList();
    }

    return new Authorizations(authorizations);
  }
}
