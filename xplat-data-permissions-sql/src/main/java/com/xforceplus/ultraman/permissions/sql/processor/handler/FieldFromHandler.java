package com.xforceplus.ultraman.permissions.sql.processor.handler;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;

import java.util.AbstractMap;
import java.util.List;

/**
 * 查找指定字段的实际来源表名称.
 * @version 0.1 2019/11/1 15:22
 * @auth dongbin
 * @since 1.8
 */
public interface FieldFromHandler {

    /**
     * 搜索指定字段的来源名称.
     * 由于子查询的存在,一个返回字段有可能是由多个字段的组成成的.
     *
     * @param field 字段.
     * @return 来源名称列表.
     */
    List<AbstractMap.SimpleEntry<Field, From>> searchRealTableName(Field field);
}
