package com.xforceplus.ultraman.permissions.rule.searcher;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRule;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;

import java.util.List;

/**
 * 规则搜索定义.
 *
 * @version 0.1 2019/11/6 15:40
 * @author dongbin
 * @since 1.8
 */
public interface Searcher {

    /**
     * 搜索指定授权角色相关的关于某个 entity 的字段相关规则信息.
     *
     * @param auth   授权信息.
     * @param entity 多个实体信息.
     * @return 规则列表.
     */
    List<FieldRule> searchFieldRule(Authorization auth, String entity);

    /**
     * 搜索指定授权角色相关的关于某个 entitty 的数据范围规则信息.
     * @param auth 授权信息.
     * @param entity 实体信息.
     * @return 规则列表.
     */
    List<DataRule> searchDataRule(Authorization auth, String entity);
}
