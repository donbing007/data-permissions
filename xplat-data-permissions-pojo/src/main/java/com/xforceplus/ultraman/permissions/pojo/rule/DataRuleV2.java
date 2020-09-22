package com.xforceplus.ultraman.permissions.pojo.rule;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/22 10:57 AM
 */
public class DataRuleV2 implements Serializable {
    private String entity;
    private List<FieldAuthority> fields;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public List<FieldAuthority> getFields() {
        return fields;
    }

    public void setFields(List<FieldAuthority> fields) {
        this.fields = fields;
    }
}
