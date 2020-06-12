package com.xforceplus.ultraman.permissions.sql.hint.parser;

import com.xforceplus.ultraman.permissions.sql.hint.Hint;

import java.sql.SQLException;

/**
 * hint parser.
 *
 * @author dongbin
 * @version 0.1 2020/6/10 17:18
 * @since 1.8
 */
public interface HintParser {

    /**
     * hint 的标识.
     */
    String HINT_FLAG = "XDP:HINT";

    /**
     * 是否忽略权限.
     */
    String KEY_IGNORE = "ignore";

    /**
     * 解析目标 SQL 中的提示.
     *
     * @param sql 需要解析的目标 SQL.
     * @return 解析得到的 hint.
     * @throws SQLException 解析失败.
     */
    Hint parse(String sql) throws SQLException;

}
