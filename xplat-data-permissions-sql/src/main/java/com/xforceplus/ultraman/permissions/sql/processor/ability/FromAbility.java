package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.text.ParseException;
import java.util.List;

/**
 * From 子句.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 17:50
 * @since 1.8
 */
public interface FromAbility {

    /**
     * 迭代当前 SQL 的来源表.
     *
     * @return 迭代器.
     */
    List<From> list() throws ProcessorException;

}
