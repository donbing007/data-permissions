package com.xforceplus.ultraman.permissions.rule.context;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 上下文默认实现.
 * @version 0.1 2019/11/1 11:45
 * @auth dongbin
 * @since 1.8
 */
public class DefaultCheckContext implements CheckContext {

    private Sql sql;
    private Authorization authorization;
    private Searcher searcher;
    private boolean refused;
    private String refusedCause;
    private List<Item> blackList;

    public DefaultCheckContext(Sql sql) {
        this(sql, null, null);
    }

    public DefaultCheckContext(Sql sql, Authorization authorization) {
        this(sql, authorization, null);
    }

    public DefaultCheckContext(Sql sql, Authorization authorization, Searcher searcher) {
        this.sql = sql;
        this.authorization = authorization;
        blackList = new ArrayList<>();
        this.searcher = searcher;
    }

    @Override
    public Sql sql() {
        return this.sql;
    }

    @Override
    public Sql updateSql(Sql sql) {
        Sql old = this.sql;
        this.sql = sql;
        return old;
    }

    @Override
    public Authorization authorization() {
        return authorization;
    }

    @Override
    public boolean isAnauthorized() {
        return authorization != null;
    }

    @Override
    public boolean isRefused() {
        return this.refused;
    }

    @Override
    public void refused(String cause) {
        refused = true;
        refusedCause = cause;
    }

    @Override
    public void refused() {
        refused("");
    }

    @Override
    public Item[] blackList() {
        return blackList.toArray(new Item[0]);
    }

    @Override
    public void black(Item item) {
        if (this.blackList == null) {
            this.blackList = new LinkedList<>();
        }

        blackList.add(item);
    }

    @Override
    public int blackSize() {
        return this.blackList.size();
    }

    @Override
    public Searcher getSercher() {
        return this.searcher;
    }
}
