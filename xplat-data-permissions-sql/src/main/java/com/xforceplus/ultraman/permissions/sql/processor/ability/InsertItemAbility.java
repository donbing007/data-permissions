package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.text.ParseException;
import java.util.List;

/**
 * insert 字段处理.
 * @version 0.1 2019/10/25 18:20
 * @auth dongbin
 * @since 1.8
 */
public interface InsertItemAbility {

    /**
     *
     * @return 迭代器.
     */
    List<Field> list() throws ProcessorException;
}
