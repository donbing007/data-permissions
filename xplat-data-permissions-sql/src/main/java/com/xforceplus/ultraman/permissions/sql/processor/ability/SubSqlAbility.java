package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.text.ParseException;
import java.util.List;

/**
 * 子查询处理.
 * @version 0.1 2019/10/31 13:52
 * @auth dongbin
 * @since 1.8
 */
public interface SubSqlAbility {

    /**
     * 列出当前的所有子句.
     * @return 子句列表.
     */
    List<Sql> list() throws ProcessorException;
}
