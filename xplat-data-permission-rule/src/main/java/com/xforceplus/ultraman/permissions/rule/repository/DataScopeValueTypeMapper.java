package com.xforceplus.ultraman.permissions.rule.repository;

import com.xforceplus.ultraman.permissions.rule.repository.entity.DataScopeValueType;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataScopeValueTypeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    int insert(DataScopeValueType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    DataScopeValueType selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    List<DataScopeValueType> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_scope_value_type
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DataScopeValueType record);
}