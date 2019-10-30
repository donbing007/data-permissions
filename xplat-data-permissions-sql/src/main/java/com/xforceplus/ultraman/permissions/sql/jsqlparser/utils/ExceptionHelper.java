package com.xforceplus.ultraman.permissions.sql.jsqlparser.utils;

import net.sf.jsqlparser.JSQLParserException;

import java.text.ParseException;

/**
 * 异常处理帮助方便工具.
 * @version 0.1 2019/10/26 14:49
 * @auth dongbin
 * @since 1.8
 */
public class ExceptionHelper {

    public static ParseException toParseException(JSQLParserException e) {
        if (e.getCause() instanceof ParseException) {
            ParseException parseException = (ParseException) e.getCause();
            return  new ParseException(e.getMessage(),parseException.getErrorOffset());
        } else {
            return new ParseException(e.getMessage(), -1);
        }
    }
}
