package com.xforceplus.ultraman.permissions.rule.convert.condition.operator;

import com.xforceplus.ultraman.permissions.pojo.rule.RuleConditionOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dongbin
 * @version 0.1 2019/11/11 18:51
 * @since 1.8
 */
public class ConditionOperationConvertingFactory {

    private static final Map<RuleConditionOperation, ConditionOperationConverting> instancs = new HashMap();

    static {
        instancs.put(RuleConditionOperation.EQUAL, new EqualsOperationConverting());
        instancs.put(RuleConditionOperation.GREATER, new GreaterOperationConverting());
        instancs.put(RuleConditionOperation.GREATER_EQUAL, new GreaterEqualsOperationConverting());
        instancs.put(RuleConditionOperation.LESS, new LessOperationConverting());
        instancs.put(RuleConditionOperation.LESS_EQUAL, new LessEqualsOperationConverting());
        instancs.put(RuleConditionOperation.LIST, new ListOperationConverging());
        instancs.put(RuleConditionOperation.NOT_EQUAL, new NotEqualsOperationConverting());
        instancs.put(RuleConditionOperation.AFTER, new FuzzyOperationConverting());
        instancs.put(RuleConditionOperation.BEFORE, new FuzzyOperationConverting());
        instancs.put(RuleConditionOperation.CONTAINS, new FuzzyOperationConverting());
        instancs.put(RuleConditionOperation.NOT_AFTER, new FuzzyNotEqualsOperationConverting());
        instancs.put(RuleConditionOperation.NOT_BEFORE, new FuzzyNotEqualsOperationConverting());
    }

    public static ConditionOperationConverting getConverting(RuleConditionOperation operation) {
        return instancs.get(operation);
    }
}
