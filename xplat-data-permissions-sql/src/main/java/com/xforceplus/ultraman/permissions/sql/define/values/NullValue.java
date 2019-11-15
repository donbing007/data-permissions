package com.xforceplus.ultraman.permissions.sql.define.values;

import com.xforceplus.ultraman.permissions.sql.define.ItemVisitor;

/**
 * null 值.
 * @version 0.1 2019/10/29 15:49
 * @author dongbin
 * @since 1.8
 */
public class NullValue implements Value {

    private static final NullValue INSTANCE = new NullValue();

    private static final String SHOW_VALUE = "NULL";

    private NullValue() {}

    public static NullValue getInstance() {
        return INSTANCE;
    }

    @Override
    public String getValue() {
        return SHOW_VALUE;
    }

    @Override
    public boolean needQuotes() {
        return false;
    }

    @Override
    public String toSqlString() {
        return SHOW_VALUE;
    }

    @Override
    public void visit(ItemVisitor visitor) {
        visitor.visit(this);
    }

}
