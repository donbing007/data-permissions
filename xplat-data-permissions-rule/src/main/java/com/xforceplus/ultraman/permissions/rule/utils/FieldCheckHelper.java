package com.xforceplus.ultraman.permissions.rule.utils;

import com.xforceplus.ultraman.perissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.sql.define.Field;

import java.util.List;

/**
 * 字段检查的帮助实现.
 * @version 0.1 2019/11/19 20:17
 * @author dongbin
 * @since 1.8
 */
public class FieldCheckHelper {

    /**
     * 检查字段是否在规则表中.
     * @param rules 规则表.
     * @param field 目标字段.
     * @return true 在,false 不在.
     */
    public static boolean checkRule(List<FieldRule> rules, Field field) {
        // 唯一的规则
        final int onlyOne = 1;
        if (rules.size() == onlyOne
            && rules.get(0).getField().equals("*")) {
            return true;
        } else {
            for (FieldRule rule : rules) {
                if (!rule.getField().equals("*")) {
                    if (rule.getField().equals(field.getName())) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
