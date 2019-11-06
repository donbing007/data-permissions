package com.xforceplus.ultraman.permissions.repository;

import com.xforceplus.ultraman.permissions.repository.entity.DataScopeValueType;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeValueTypeExample;
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
public interface DataScopeValueTypeRepository {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @SelectProvider(type=DataScopeValueTypeSqlProvider.class, method="countByExample")
    long countByExample(DataScopeValueTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @DeleteProvider(type=DataScopeValueTypeSqlProvider.class, method="deleteByExample")
    int deleteByExample(DataScopeValueTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @Delete({
        "delete from data_scope_value_type",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @Insert({
        "insert into data_scope_value_type (`name`, descr)",
        "values (#{name,jdbcType=VARCHAR}, #{descr,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(DataScopeValueType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @InsertProvider(type=DataScopeValueTypeSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(DataScopeValueType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @SelectProvider(type=DataScopeValueTypeSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="descr", property="descr", jdbcType=JdbcType.VARCHAR)
    })
    List<DataScopeValueType> selectByExample(DataScopeValueTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, `name`, descr",
        "from data_scope_value_type",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="descr", property="descr", jdbcType=JdbcType.VARCHAR)
    })
    DataScopeValueType selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeValueTypeSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") DataScopeValueType record, @Param("example") DataScopeValueTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeValueTypeSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") DataScopeValueType record, @Param("example") DataScopeValueTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @UpdateProvider(type=DataScopeValueTypeSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(DataScopeValueType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    @Update({
        "update data_scope_value_type",
        "set `name` = #{name,jdbcType=VARCHAR},",
          "descr = #{descr,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DataScopeValueType record);
}