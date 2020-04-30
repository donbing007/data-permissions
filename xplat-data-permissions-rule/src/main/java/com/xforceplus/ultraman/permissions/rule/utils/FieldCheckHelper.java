package com.xforceplus.ultraman.permissions.rule.utils;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.arithmetic.Arithmeitc;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字段检查的帮助实现.
 *
 * @author dongbin
 * @version 0.1 2019/11/19 20:17
 * @since 1.8
 */
public class FieldCheckHelper {

    /**
     * 检查字段是否在规则表中.
     *
     * @param rules 规则表.
     * @param field 目标字段.
     * @return true 在,false 不在.
     */
    public static boolean checkRule(List<FieldRule> rules, Field field) {
        if (rules == null || rules.isEmpty()) {
            return false;
        }
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

    /**
     * 检查指定的一批字段是否在多个角色中的规则中.
     *
     * @param fieldFromAbility 字段实际来源查询能力实例.
     * @param authorizations   授权信息.
     * @param fields           目标字段列表.
     * @param searcher         规则搜索实例.
     * @return
     */
    public static Collection<Field> checkFieldsRule(
        FieldFromAbility fieldFromAbility,
        Authorizations authorizations,
        Collection<Field> fields,
        Searcher searcher) {

        List<Field> targetFields = new ArrayList<>(fields);

        return targetFields.stream().filter(field -> {

            // 字段实际的来源表,因为一个字段有可能是由子表的多个字段组成而成的,需要每一个字段都有权限.
            List<Map.Entry<Field, From>> fieldFroms = fieldFromAbility.searchRealTableName(field);

            // 当前处理的字段
            Field targetField;
            // 当前处理的表.
            From targetFrom;
            // 当前表在某个角色下的规则.
            List<FieldRule> targetRules;

            // 某个子字段是否在所有角色中的某个角色的字段规则中.true 在,false 不在.
            boolean found;
            for (Map.Entry<Field, From> fieldFromEntry : fieldFroms) {

                targetField = fieldFromEntry.getKey();
                targetFrom = fieldFromEntry.getValue();
                found = false;

                for (Authorization auth : authorizations.getAuthorizations()) {
                    targetRules = searcher.searchFieldRule(auth, targetFrom.getTable());

                    if (checkRule(targetRules, targetField)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    // 从最终列表中删除.
                    return true;
                }
            }

            // 保留在最终列表中.
            return false;

        }).collect(Collectors.toList());
    }

    /**
     * 找出所有的字段,可能的来源为函数,表达式,括号等.
     *
     * @param item 目标元素.
     * @param top  最顶层元素.
     * @param pool key 为实际字段,value 为最上层的字段或函数,表达式..等.
     */
    public static void fillField(Item item, Item top, Map<Field, Item> pool) {
        item.visit(new ItemVisitorAdapter() {
            @Override
            public void visit(Field field) {
                pool.put(field, top);
            }

            @Override
            public void visit(Func func) {
                for (Item p : func.getParameters()) {
                    fillField(p, func, pool);
                }
            }

            @Override
            public void visit(Arithmeitc item) {
                fillField(item.getRight(), top, pool);
                fillField(item.getLeft(), top, pool);
            }

            @Override
            public void visit(Parentheses item) {
                fillField(item.getItem(), top, pool);
            }
        });
    }

}
