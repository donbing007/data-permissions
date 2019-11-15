package com.xforceplus.ultraman.permissions.sql.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.UpdateSet;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;

import java.text.ParseException;
import java.util.List;

/**
 * update set.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 18:28
 * @since 1.8
 */
public interface UpdateSetAbility {

    /**
     * 迭代 update set 区域.
     *
     * @return 迭代器.
     */
    List<UpdateSet> list() throws ProcessorException;
}
