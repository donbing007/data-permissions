package com.xforceplus.ultraman.permissions.rule.context;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.perissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Item;

/**
 * 规则检查上下文.
 * @version 0.1 2019/11/1 11:33
 * @author dongbin
 * @since 1.8
 */
public interface Context {

    /**
     * 当前被检查的 sql.
     * @return sql.
     */
    Sql sql();

    /**
     * 更新当前的 sql.
     * @param sql 目标 sql.
     * @return 旧的 sql.
     */
    Sql updateSql(Sql sql);

    /**
     * 是否更新过 sql.
     * @return true 更新过,false 没有.
     */
    boolean isUpdatedSql();

    /**
     * 当前的授权信息.
     * @return 授权信息.
     */
    Authorizations authorization();

    /**
     * 是否未授权.
     * @return true 授权, false 未授权.
     */
    boolean isAnauthorized();

    /**
     * 是否拒绝SQL 执行.
     * @return true 拒绝, false 不拒绝.
     */
    boolean isRefused();

    /**
     * 拒绝执行 SQL.
     * @param cause 理由.
     */
    void refused(String cause);

    /**
     * 拒绝执行 sql.无理由.
     */
    void refused();

    /**
     * 拒绝的理由.
     * @return 理由
     */
    String cause();

    /**
     * 屏蔽的字段,这些字段不会出现在最终执行的 SQL 上了.
     * @return 字段列表.
     */
    Item[] blackList();

    /**
     * 屏蔽某个字段.
     * @param item 目标字段.
     */
    void black(Item item);

    /**
     * 获取屏蔽字段数量.
     * @return 数量.
     */
    int blackSize();

    /**
     * 得到规则搜索实例.
     * @return 实例.
     */
    Searcher getSercher();

}
