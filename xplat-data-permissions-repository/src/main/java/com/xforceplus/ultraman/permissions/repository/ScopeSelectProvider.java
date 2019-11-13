package com.xforceplus.ultraman.permissions.repository;

import com.xforceplus.ultraman.permissions.repository.entity.SelectScopeExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

/**
 * SQL socke provider.
 * @version 0.1 2019/11/11 13:26
 * @auth dongbin
 * @since 1.8
 */
public class ScopeSelectProvider {

    public String selectFieldScopeByExample(@Param("example") SelectScopeExample example) {
        SQL sql = new SQL();
        sql.SELECT("fs.id, fs.entity, fs.field")
            .FROM("field_scope fs")
            .INNER_JOIN("role_permissions rp on rp.scope_id=fs.id")
            .INNER_JOIN("role r on rp.role_id=r.id")
            .WHERE("r.role_external_id = #{example.roleId}")
            .WHERE("r.tenant_id=#{example.tenantId}")
            .WHERE("fs.entity=#{example.entity}")
            .WHERE("rp.scope_type=0");
        if (example.getField() != null) {
            sql.WHERE("fs.entity=#{example.field}");
        }
        sql.ORDER_BY("id asc");

        return sql.toString();
    }

    public String selectDataScopeConditionsByExample(@Param("example") SelectScopeExample example) {
        SQL sql = new SQL();
        sql.SELECT(
            "dssc.id,dssc.conditions_id,dssc.field,dssc.value_type_id,dssc.operation," +
                "dssc.index,dssc.value,dssc.link")
            .FROM("data_scope_sub_condition dssc")
            .INNER_JOIN("data_scope_conditions dsc on dssc.conditions_id=dsc.id")
            .INNER_JOIN("data_scope ds on dsc.data_scope_id=ds.id")
            .INNER_JOIN("role_permissions rp on rp.scope_id=ds.id")
            .INNER_JOIN("role r on rp.role_id=r.id")
            .WHERE("r.tenant_id=#{example.tenantId}")
            .WHERE("r.role_external_id = #{example.roleId}")
            .WHERE("ds.entity=#{example.entity}")
            .WHERE("rp.scope_type=1");
        if (example.getField() != null) {
            sql.WHERE("dsc.field=#{example.field}")
                .ORDER_BY("dssc.index asc");
        } else {
            sql.ORDER_BY("dssc.conditions_id asc, dssc.index asc");
        }

        return sql.toString();
    }
}