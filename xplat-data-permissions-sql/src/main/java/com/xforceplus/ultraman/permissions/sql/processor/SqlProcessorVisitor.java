package com.xforceplus.ultraman.permissions.sql.processor;

/**
 * sql processor 访问器.
 *
 * @author dongbin
 * @version 0.1 2019/10/31 19:25
 * @since 1.8
 */
public interface SqlProcessorVisitor {

    /**
     * 当是 select 时被调用.
     *
     * @param processor 目标.
     */
    void visit(SelectSqlProcessor processor);

    /**
     * 当是 delete 时被调用.
     *
     * @param processor 目标.
     */
    void visit(DeleteSqlProcessor processor);

    /**
     * 当是 insert 时被调用.
     *
     * @param processor 目标.
     */
    void visit(InsertSqlProcessor processor);

    /**
     * 当是 update 时被调用.
     *
     * @param processor 目标.
     */
    void visit(UpdateSqlProcessor processor);

    /**
     * 当是 子查询 时被调用.
     *
     * @param processor 目标.
     */
    void visit(SubSelectSqlProcessor processor);

    /**
     * 当是无法操作 SQL 时被计用.
     *
     * @param processor 目标.
     */
    void visit(UnableOperateSqlProcessor processor);
}
