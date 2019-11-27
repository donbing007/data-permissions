package com.xforceplus.ultraman.permissions.pojo.rule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

/**
 * 规则中可以用的条件.
 * @version 0.1 2019/11/6 16:03
 * @author dongbin
 * @since 1.8
 */
@JsonSerialize(using = RuleConditionOperation.RuleConditionOperationJsonSerializer.class)
@JsonDeserialize(using = RuleConditionOperation.RuleConditionOperationJsonDeserialize.class)
public enum RuleConditionOperation {
    GREATER(">"),
    LESS("<"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_EQUAL(">="),
    LESS_EQUAL("<="),
    LIST("[]"),
    BEFORE("^"),
    AFTER("$"),
    NOT_BEFORE("!^"),
    NOT_AFTER("!$"),
    CONTAINS("()");

    private String symbol;

    private RuleConditionOperation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static RuleConditionOperation getInstance(String symbol) {
        String noSpaceSymbol = symbol.trim();
        for (RuleConditionOperation operator : RuleConditionOperation.values()) {
            if (operator.getSymbol().equals(noSpaceSymbol)) {
                return operator;
            }
        }

        return null;
    }

    /**
     * json 自定义序例化.
     */
    public static class RuleConditionOperationJsonSerializer extends JsonSerializer<RuleConditionOperation> {

        @Override
        public void serialize(RuleConditionOperation value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

            gen.writeString(value.getSymbol());

        }
    }

    /**
     * json 自定义反序例化.
     */
    public static class RuleConditionOperationJsonDeserialize extends JsonDeserializer<RuleConditionOperation> {

        @Override
        public RuleConditionOperation deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

            String value = p.getValueAsString();
            RuleConditionOperation operation = RuleConditionOperation.getInstance(value);
            if (operation == null) {

                // 试图兼容 enum 名称.
                RuleConditionOperation[] values = RuleConditionOperation.values();
                for (RuleConditionOperation v : values) {
                    if (v.name().equals(value)) {
                        operation = v;
                        break;
                    }
                }
            }

            return operation;
        }
    }
}
