package com.xforceplus.ultraman.permissions.sql;


import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.SqlType;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.SqlProcessorVisitor;

import java.util.List;

/**
 * SQL解析结果表示.
 * @version 0.1 2019/10/25 16:03
 * @author dongbin
 * @since 1.8
 */
public interface Sql {

    /**
     * 得到当前 SQL 的操作实例.
     * 通过此实例可以对 SQL 进行修改.
     *
     * @return 操作实例.
     */
    SqlProcessor buildProcessor();

    /**
     * 使用访问者模式得到处理器实例.
     * @param visitor 访问器.
     */
    void visit(SqlProcessorVisitor visitor);

    /**
     * 得到当前 SQL 的类型.
     * @return 类型实例.
     */
    SqlType type();

    /**
     * 是否联合查询.
     * @return true 联合查询,false 非联合查询.
     */
    boolean isUnion();

    /**
     * 是否子句.
     * @return true 子句,false 非子句.
     */
    boolean isSub();

    /**
     * sql 字符串表示.
     * @return sql 字符串.
     */
    String toSqlString();

}
