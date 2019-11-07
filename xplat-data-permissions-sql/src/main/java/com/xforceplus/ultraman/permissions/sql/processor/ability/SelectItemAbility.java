package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Func;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.text.ParseException;
import java.util.List;

/**
 * select 字段列表.
 * @version 0.1 2019/10/25 17:51
 * @auth dongbin
 * @since 1.8
 */
public interface SelectItemAbility {

    /**
     * 删除一个函数.
     * @param func 函数.
     */
    void remove(Func func) throws ProcessorException;

    /**
     * 删除字段.
     * @param field 目标字段.
     */
    void remove(Field field) throws ProcessorException;

    /**
     * 迭代当前的 column 列表.
     * @return 迭代器.
     */
    List<Item> list() throws ProcessorException;
}
