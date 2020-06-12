package com.xforceplus.ultraman.permissions.sql.hint.parser;

import com.xforceplus.ultraman.permissions.sql.hint.Hint;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * sql92 标准的注解 hint 解析器.
 * 有可能找到多个相同的配置,以最后找到的为准.
 * <p>
 * 值必须以 HintParser.HINT_FLAG 开头,否则忽略.
 * 值必须以 key=value,key=value 的形式存在,否则解析失败.
 * <p>
 * 格式如下.
 * /* XDP:HINT command * /
 * <p>
 * select t.id from table t /* XDP:HINT ignore=true * /
 *
 * @author dongbin
 * @version 0.1 2020/6/10 17:24
 * @since 1.8
 */
public class SQL92HintParser implements HintParser {

    /**
     * 注释外部开始和结束字符.
     */
    private final static char FLAG_OUT_BORDER = '/';
    /**
     * 注释内部开始和结束字符.
     */
    private final static char FLAG_IN_BORDER = '*';
    /**
     * 注释的 key 和 value 分隔符.
     */
    private final static char VALUE_EQ = '=';
    /**
     * 多个注释的分隔符.
     */
    private final static char VALUE_SEPARATOR = ',';

    @Override
    public Hint parse(String sql) throws SQLException {
        Hint hint = null;
        if (sql == null || sql.isEmpty()) {
            hint = new Hint();
        } else {

            hint = doParse(findAnnotation(sql));

        }

        return hint;
    }

    private Hint doParse(String[] hintTexts) throws SQLException {

        Hint hint = new Hint();
        List<Map.Entry<String, String>> kvs;
        for (String text : hintTexts) {
            if (text.trim().isEmpty()) {
                continue;
            }

            if (isXdpHint(text)) {
                kvs = doParseValue(text);

                for (Map.Entry<String, String> kv : kvs) {

                    try {
                        if (HintParser.KEY_IGNORE.equals(kv.getKey())) {
                            hint.setIgnore(Boolean.parseBoolean(kv.getValue()));
                        }
                    } catch (Exception ex) {
                        throw new SQLException(ex.getMessage(), ex);
                    }
                }
            }
        }

        return hint;
    }

    private List<Map.Entry<String, String>> doParseValue(String text) throws SQLException {
        List<Map.Entry<String, String>> kvs = new ArrayList<>(1);
        StringBuilder buff = new StringBuilder();
        String key = null;
        char[] sqlArray = text.toCharArray();
        char c;
        for (int i = HintParser.HINT_FLAG.length() + 1; i < sqlArray.length; i++) {
            c = sqlArray[i];

            if (c == VALUE_EQ) {

                key = buff.toString();
                buff.delete(0, buff.length());

                checkKey(key);

            } else if (c == VALUE_SEPARATOR) {

                checkKey(key);

                kvs.add(new AbstractMap.SimpleEntry<>(key, buff.toString()));

            } else {

                buff.append(c);
            }
        }

        if (key != null) {
            kvs.add(new AbstractMap.SimpleEntry<>(key, buff.toString()));
        }

        return kvs;
    }

    private void checkKey(String key) throws SQLException {
        if (key == null || key.isEmpty()) {
            throw new SQLException("Incorrect Hint format, must be /* XDP: Hint key0=value0,key1=value1 */");
        }
    }

    private boolean isXdpHint(String text) {
        return text.startsWith(HintParser.HINT_FLAG);
    }

    private String[] findAnnotation(String sql) {
        /**
         * 假设只会找到一个注释段.
         */
        final int expectedOne = 1;

        List<String> annotations = new ArrayList<>(expectedOne);
        StringBuilder buff = new StringBuilder();
        char[] sqlArray = sql.toCharArray();

        // 表示当前的字符为需要的
        boolean need = false;
        char currentChar;
        char nextChar;

        int pos = 0;
        while (pos < sqlArray.length) {
            currentChar = sqlArray[pos++];
            if (need) {

                if (currentChar == FLAG_IN_BORDER) {
                    if (pos < sqlArray.length) {
                        nextChar = sqlArray[pos++];
                        if (nextChar == FLAG_OUT_BORDER) {
                            need = false;

                            if (buff.charAt(buff.length() - 1) == ' ') {
                                buff.deleteCharAt(buff.length() - 1);
                            }

                            annotations.add(buff.toString());

                            buff.delete(0, buff.length());
                        }
                    }
                } else {
                    if (buff.length() == 0 && ' ' == currentChar) {
                        continue;
                    }
                    buff.append(currentChar);
                }
            } else {

                // 试图查找到 '/'
                if (currentChar == FLAG_OUT_BORDER) {
                    if (pos < sqlArray.length - 1) {
                        nextChar = sqlArray[pos++];

                        if (nextChar == FLAG_IN_BORDER) {
                            need = true;
                        }
                    }
                }
            }
        }

        return annotations.toArray(new String[0]);
    }

}
