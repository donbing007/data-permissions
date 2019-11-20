package com.xforceplus.ultraman.permissions.rule.context;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.perissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.perissions.pojo.rule.DataRule;
import com.xforceplus.ultraman.perissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.define.Item;

import java.util.*;

/**
 * 上下文默认实现.
 * @version 0.1 2019/11/1 11:45
 * @author dongbin
 * @since 1.8
 */
public class DefaultContext implements Context {

    private static final Searcher NULL_SEARCHER = new NullSearcher();

    private Sql sql;
    private Authorizations authorizations;
    private Searcher searcher;
    private boolean refused;
    private String refusedCause;
    private List<Item> blackList;
    private boolean updated;

    public DefaultContext(Sql sql) {
        this.sql = sql;
    }

    public DefaultContext(Sql sql, Authorization authorization) {
        this(sql, new Authorizations(Arrays.asList(authorization)));
    }

    public DefaultContext(Sql sql, Authorizations authorizations) {
        this(sql, authorizations, NULL_SEARCHER);
    }

    public DefaultContext(Sql sql, Authorization authorization, Searcher searcher) {
        this(sql, new Authorizations(Arrays.asList(authorization)), searcher);
    }

    public DefaultContext(Sql sql, Authorizations authorizations, Searcher searcher) {
        this.sql = sql;
        this.authorizations = authorizations;
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

        updated = true;

        return old;
    }

    @Override
    public boolean isUpdatedSql() {
        return updated;
    }

    @Override
    public Authorizations authorization() {
        return authorizations;
    }

    @Override
    public boolean isAnauthorized() {
        return authorizations != null;
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
    public String cause() {
        return refusedCause;
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

    /**
     * 默认的搜索器,实际不会搜索任何规则.
     */
    private static class NullSearcher implements Searcher {

        @Override
        public List<FieldRule> searchFieldRule(Authorization auth, String entity) {
            return Collections.emptyList();
        }

        @Override
        public List<DataRule> searchDataRule(Authorization auth, String entity) {
            return Collections.emptyList();
        }
    }
}
