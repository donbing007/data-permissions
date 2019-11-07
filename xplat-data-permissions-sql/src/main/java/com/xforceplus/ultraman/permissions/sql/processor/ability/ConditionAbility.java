package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Condition;
import com.xforceplus.ultraman.permissions.sql.define.Conditional;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.text.ParseException;
import java.util.List;

/**
 * 条件操作定义.
 * @version 0.1 2019/10/25 17:48
 * @auth dongbin
 * @since 1.8
 */
public interface ConditionAbility {

    /**
     * 增加和件.
     * @param condition
     * @param conditional
     */
    void add(Condition condition, Conditional conditional, boolean isolation) throws ProcessorException;

    /**
     * 删除条件.
     * @param condition 需要删除的目标条件.
     */
    void remove(Condition condition) throws ProcessorException;

    /**
     * 得到当前条件的迭代器.
     * @return 迭代器实例.
     */
    List<Condition> list() throws ProcessorException;
}
