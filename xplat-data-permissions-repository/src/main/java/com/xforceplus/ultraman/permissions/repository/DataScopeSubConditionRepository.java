package com.xforceplus.ultraman.permissions.repository;

import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubCondition;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubConditionExample;
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
public interface DataScopeSubConditionRepository {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @SelectProvider(type=DataScopeSubConditionSqlProvider.class, method="countByExample")
    long countByExample(DataScopeSubConditionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @DeleteProvider(type=DataScopeSubConditionSqlProvider.class, method="deleteByExample")
    int deleteByExample(DataScopeSubConditionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @Delete({
        "delete from data_scope_sub_condition",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @Insert({
        "insert into data_scope_sub_condition (conditions_id, value_type_id, ",
        "`operation`, `index`, ",
        "`value`, link)",
        "values (#{conditionsId,jdbcType=BIGINT}, #{valueTypeId,jdbcType=BIGINT}, ",
        "#{operation,jdbcType=VARCHAR}, #{index,jdbcType=SMALLINT}, ",
        "#{value,jdbcType=VARCHAR}, #{link,jdbcType=TINYINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(DataScopeSubCondition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @InsertProvider(type=DataScopeSubConditionSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(DataScopeSubCondition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @SelectProvider(type=DataScopeSubConditionSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="conditions_id", property="conditionsId", jdbcType=JdbcType.BIGINT),
        @Result(column="value_type_id", property="valueTypeId", jdbcType=JdbcType.BIGINT),
        @Result(column="operation", property="operation", jdbcType=JdbcType.VARCHAR),
        @Result(column="index", property="index", jdbcType=JdbcType.SMALLINT),
        @Result(column="value", property="value", jdbcType=JdbcType.VARCHAR),
        @Result(column="link", property="link", jdbcType=JdbcType.TINYINT)
    })
    List<DataScopeSubCondition> selectByExample(DataScopeSubConditionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, conditions_id, value_type_id, `operation`, `index`, `value`, link",
        "from data_scope_sub_condition",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="conditions_id", property="conditionsId", jdbcType=JdbcType.BIGINT),
        @Result(column="value_type_id", property="valueTypeId", jdbcType=JdbcType.BIGINT),
        @Result(column="operation", property="operation", jdbcType=JdbcType.VARCHAR),
        @Result(column="index", property="index", jdbcType=JdbcType.SMALLINT),
        @Result(column="value", property="value", jdbcType=JdbcType.VARCHAR),
        @Result(column="link", property="link", jdbcType=JdbcType.TINYINT)
    })
    DataScopeSubCondition selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeSubConditionSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") DataScopeSubCondition record, @Param("example") DataScopeSubConditionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeSubConditionSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") DataScopeSubCondition record, @Param("example") DataScopeSubConditionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeSubConditionSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(DataScopeSubCondition record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_sub_condition
     *
     * @mbg.generated
     */
    @Update({
        "update data_scope_sub_condition",
        "set conditions_id = #{conditionsId,jdbcType=BIGINT},",
          "value_type_id = #{valueTypeId,jdbcType=BIGINT},",
          "`operation` = #{operation,jdbcType=VARCHAR},",
          "`index` = #{index,jdbcType=SMALLINT},",
          "`value` = #{value,jdbcType=VARCHAR},",
          "link = #{link,jdbcType=TINYINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DataScopeSubCondition record);
}