package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.util.List;
import java.util.Map;

/**
 * 查找指定字段的实际来源表名称.
 *
 * @author dongbin
 * @version 0.1 2019/11/1 15:22
 * @since 1.8
 */
public interface FieldFromAbility {

    /**
     * 搜索指定字段的来源名称.
     * 由于子查询的存在,一个返回字段有可能是由多个字段的组成成的.
     *
     * @param item 字段.
     * @return 来源名称列表.
     */
    List<Map.Entry<Field, From>> searchRealTableName(Item item) throws ProcessorException;
}
