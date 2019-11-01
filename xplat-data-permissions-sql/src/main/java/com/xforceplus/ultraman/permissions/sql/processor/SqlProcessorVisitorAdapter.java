package com.xforceplus.ultraman.permissions.sql.processor;

/**
 * 桥接方法,只需要实现相关方法即可.
 * @version 0.1 2019/10/31 19:30
 * @auth dongbin
 * @since 1.8
 */
public abstract class SqlProcessorVisitorAdapter implements SqlProcessorVisitor {
    @Override
    public void visit(SelectSqlProcessor processor) {

    }

    @Override
    public void visit(DeleteSqlProcessor processor) {

    }

    @Override
    public void visit(InsertSqlProcessor processor) {

    }

    @Override
    public void visit(UpdateSqlProcessor processor) {

    }

    @Override
    public void visit(SubSelectSqlProcessor processor) {

    }

    @Override
    public void visit(UnableOperateSqlProcessor processor) {

    }
}
