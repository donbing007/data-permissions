package com.xforceplus.ultraman.permissions.repository;


import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubCondition;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScope;
import com.xforceplus.ultraman.permissions.repository.entity.SelectDataScopeExample;
import com.xforceplus.ultraman.permissions.repository.entity.SelectFieldScopeExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * 字段数据范围查询.
 *
 * @version 0.1 2019/11/5 16:53
 * @auth dongbin
 * @since 1.8
 */
@Mapper
public interface ScopeSelectRepository {

    @Select({
        "select",
        "fs.id, fs.entity, fs.field",
        "from field_scope fs",
        "inner join role_permissions rp on rp.scope_id=fs.id",
        "inner join role r on rp.role_id=r.id",
        "where r.role_external_id = #{example.roleId}",
        "and r.tenant_id=#{example.tenantId}",
        "and fs.entity=#{example.entity}",
        "and rp.scope_type=0",
        "order by id asc"
    })
    @Results({
        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
        @Result(column = "entity", property = "entity", jdbcType = JdbcType.VARCHAR),
        @Result(column = "field", property = "field", jdbcType = JdbcType.VARCHAR)
    })
    List<FieldScope> selectFieldScopeByExample(@Param("example") SelectFieldScopeExample example);

    @Select({
        "select",
        "dssc.id,dssc.conditions_id,dssc.value_type_id,dssc.operator,dssc.index,dssc.value,dssc.link",
        "from data_scope_sub_condition dssc",
        "inner join data_scope_conditions dsc on dssc.conditions_id=dsc.id",
        "inner join data_scope ds on dsc.data_scope_id=ds.id",
        "inner join role_permissions rp on rp.scope_id=ds.id",
        "inner join role r on rp.role_id=r.id",
        "where r.tenant_id=#{example.tenantId}",
        "and r.role_external_id = #{example.roleId}",
        "and ds.entity=#{example.entity}",
        "and dsc.field=#{example.field}",
        "and rp.scope_type=1",
        "order by dssc.index asc"
    })
    @Results({
        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
        @Result(column = "conditions_id", property = "conditionsId", jdbcType = JdbcType.BIGINT),
        @Result(column = "value_type_id", property = "valueTypeId", jdbcType = JdbcType.BIGINT),
        @Result(column = "operator", property = "operator", jdbcType = JdbcType.VARCHAR),
        @Result(column = "index", property = "index", jdbcType = JdbcType.SMALLINT),
        @Result(column = "value", property = "value", jdbcType = JdbcType.VARCHAR),
        @Result(column = "link", property = "link", jdbcType = JdbcType.TINYINT)
    })
    List<DataScopeSubCondition> selectDataScopeConditionsByExample(@Param("example") SelectDataScopeExample example);

}
