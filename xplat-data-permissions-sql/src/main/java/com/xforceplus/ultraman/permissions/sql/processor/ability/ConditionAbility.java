package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Condition;
import com.xforceplus.ultraman.permissions.sql.define.Conditional;
import com.xforceplus.ultraman.permissions.sql.define.Parentheses;
import com.xforceplus.ultraman.permissions.sql.define.relationship.Relationship;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.util.List;

/**
 * 条件操作定义.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 17:48
 * @since 1.8
 */
public interface ConditionAbility {

    /**
     * 增加条件.
     *
     * @param condition
     * @param conditional
     */
    void add(Condition condition, Conditional conditional, boolean isolation) throws ProcessorException;

    /**
     * 增加多个条件.
     *
     * @param conditions  条件,是一个基于Relationship的二叉树.
     * @param conditional 和已有条件的关系.
     * @param isolation   是否需要和已有条件隔离.
     * @throws ProcessorException 操作异常.
     */
    void add(Relationship conditions, Conditional conditional, boolean isolation) throws ProcessorException;

    /**
     * 删除条件.
     *
     * @param condition 需要删除的目标条件.
     */
    void remove(Condition condition) throws ProcessorException;

    /**
     * 得到当前条件的迭代器.
     *
     * @return 迭代器实例.
     */
    List<Condition> list() throws ProcessorException;
}
