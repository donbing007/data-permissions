package com.xforceplus.ultraman.permissions.pojo.rule;

import java.util.List;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/22 11:13 AM
 */
public class FieldAuthority {
    private Long id;
    private String name;
    private List<DataRuleCondition> conditions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataRuleCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<DataRuleCondition> conditions) {
        this.conditions = conditions;
    }
}
