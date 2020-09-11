package com.xforceplus.ultraman.permissions.jdbc.parser;


/**
 * parse the sql which contains variable
 */
public interface VariableParser {

    /**
     * the name of the parser
     * @return
     */
    String getName();

    /**
     * Parse the variable in the sql where condition
     * @param sql
     * @return
     */
    String parse(String sql);
}
