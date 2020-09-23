package com.xforceplus.ultraman.permissions.pojo.rule;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/23 2:30 PM
 */
public class FieldRuleRequest implements Serializable {
    private String entity;
    private List<String> fields;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
