package com.xforceplus.ultraman.permissions.sql.utils;

import com.sun.jmx.remote.internal.ArrayQueue;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.processor.*;
import com.xforceplus.ultraman.permissions.sql.processor.ability.SubSqlAbility;

import java.util.*;

/**
 * 子查询迭代器.
 * @version 0.1 2019/11/8 16:12
 * @auth dongbin
 * @since 1.8
 */
public class SubSqlIterator implements Iterator<Sql> {

    private SubSqlAbility subSqlAbility;
    private Queue<Sql> queue;
    private SqlProcessorVisitor visitor = new SqlProcessorVisitorAdapter() {
        @Override
        public void visit(SelectSqlProcessor processor) {
            queue.addAll(processor.buildSubSqlAbility().list());
        }

        @Override
        public void visit(DeleteSqlProcessor processor) {
            queue.addAll(processor.buildSubSqlAbility().list());
        }

        @Override
        public void visit(UpdateSqlProcessor processor) {
            queue.addAll(processor.buildSubSqlAbility().list());
        }

        @Override
        public void visit(SubSelectSqlProcessor processor) {
            queue.addAll(processor.buildSubSqlAbility().list());
        }
    };

    public SubSqlIterator(SubSqlAbility subSqlAbility) {
        this.subSqlAbility = subSqlAbility;

        queue = new ArrayDeque();

        queue.addAll(subSqlAbility.list());
    }

    @Override
    public boolean hasNext() {

        return !queue.isEmpty();
    }

    @Override
    public Sql next() {

        Sql sql = queue.poll();

        sql.visit(visitor);

        return sql;
    }
}
