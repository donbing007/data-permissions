package com.xforceplus.ultraman.permissions.repository;


import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubCondition;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScope;
import com.xforceplus.ultraman.permissions.repository.entity.SelectScopeExample;
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

    @SelectProvider(type = ScopeSelectProvider.class, method = "selectFieldScopeByExample")
    @Results({
        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
        @Result(column = "entity", property = "entity", jdbcType = JdbcType.VARCHAR),
        @Result(column = "field", property = "field", jdbcType = JdbcType.VARCHAR)
    })
    List<FieldScope> selectFieldScopeByExample(@Param("example") SelectScopeExample example);

    @SelectProvider(type = ScopeSelectProvider.class, method = "selectDataScopeConditionsByExample")
    @Results({
        @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
        @Result(column = "conditions_id", property = "conditionsId", jdbcType = JdbcType.BIGINT),
        @Result(column = "field", property = "field", jdbcType = JdbcType.VARCHAR),
        @Result(column = "value_type_id", property = "valueTypeId", jdbcType = JdbcType.BIGINT),
        @Result(column = "operator", property = "operator", jdbcType = JdbcType.VARCHAR),
        @Result(column = "index", property = "index", jdbcType = JdbcType.SMALLINT),
        @Result(column = "value", property = "value", jdbcType = JdbcType.VARCHAR),
        @Result(column = "link", property = "link", jdbcType = JdbcType.TINYINT)
    })
    List<DataScopeSubCondition> selectDataScopeConditionsByExample(@Param("example") SelectScopeExample example);

}
