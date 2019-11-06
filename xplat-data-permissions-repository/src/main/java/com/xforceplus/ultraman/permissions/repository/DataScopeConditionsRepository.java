package com.xforceplus.ultraman.permissions.repository;

import com.xforceplus.ultraman.permissions.repository.entity.DataScopeConditions;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeConditionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface DataScopeConditionsRepository {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @SelectProvider(type=DataScopeConditionsSqlProvider.class, method="countByExample")
    long countByExample(DataScopeConditionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @DeleteProvider(type=DataScopeConditionsSqlProvider.class, method="deleteByExample")
    int deleteByExample(DataScopeConditionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @Delete({
        "delete from data_scope_conditions",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @Insert({
        "insert into data_scope_conditions (data_scope_id, field)",
        "values (#{dataScopeId,jdbcType=BIGINT}, #{field,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(DataScopeConditions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @InsertProvider(type=DataScopeConditionsSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(DataScopeConditions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @SelectProvider(type=DataScopeConditionsSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="data_scope_id", property="dataScopeId", jdbcType=JdbcType.BIGINT),
        @Result(column="field", property="field", jdbcType=JdbcType.VARCHAR)
    })
    List<DataScopeConditions> selectByExample(DataScopeConditionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, data_scope_id, field",
        "from data_scope_conditions",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="data_scope_id", property="dataScopeId", jdbcType=JdbcType.BIGINT),
        @Result(column="field", property="field", jdbcType=JdbcType.VARCHAR)
    })
    DataScopeConditions selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeConditionsSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") DataScopeConditions record, @Param("example") DataScopeConditionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeConditionsSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") DataScopeConditions record, @Param("example") DataScopeConditionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeConditionsSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(DataScopeConditions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_conditions
     *
     * @mbg.generated
     */
    @Update({
        "update data_scope_conditions",
        "set data_scope_id = #{dataScopeId,jdbcType=BIGINT},",
          "field = #{field,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DataScopeConditions record);
}