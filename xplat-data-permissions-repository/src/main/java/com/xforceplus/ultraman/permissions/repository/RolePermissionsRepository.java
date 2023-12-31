package com.xforceplus.ultraman.permissions.repository;

import com.xforceplus.ultraman.permissions.repository.entity.RolePermissions;
import com.xforceplus.ultraman.permissions.repository.entity.RolePermissionsExample;
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
public interface RolePermissionsRepository {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @SelectProvider(type=RolePermissionsSqlProvider.class, method="countByExample")
    long countByExample(RolePermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @DeleteProvider(type=RolePermissionsSqlProvider.class, method="deleteByExample")
    int deleteByExample(RolePermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @Delete({
        "delete from role_permissions",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @Insert({
        "insert into role_permissions (role_id, scope_id, ",
        "scope_type)",
        "values (#{roleId,jdbcType=BIGINT}, #{scopeId,jdbcType=BIGINT}, ",
        "#{scopeType,jdbcType=TINYINT})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(RolePermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @InsertProvider(type=RolePermissionsSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(RolePermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @SelectProvider(type=RolePermissionsSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
        @Result(column="scope_id", property="scopeId", jdbcType=JdbcType.BIGINT),
        @Result(column="scope_type", property="scopeType", jdbcType=JdbcType.TINYINT)
    })
    List<RolePermissions> selectByExample(RolePermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "id, role_id, scope_id, scope_type",
        "from role_permissions",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
        @Result(column="scope_id", property="scopeId", jdbcType=JdbcType.BIGINT),
        @Result(column="scope_type", property="scopeType", jdbcType=JdbcType.TINYINT)
    })
    RolePermissions selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @UpdateProvider(type=RolePermissionsSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") RolePermissions record, @Param("example") RolePermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @UpdateProvider(type=RolePermissionsSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") RolePermissions record, @Param("example") RolePermissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @UpdateProvider(type=RolePermissionsSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(RolePermissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permissions
     *
     * @mbg.generated
     */
    @Update({
        "update role_permissions",
        "set role_id = #{roleId,jdbcType=BIGINT},",
          "scope_id = #{scopeId,jdbcType=BIGINT},",
          "scope_type = #{scopeType,jdbcType=TINYINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(RolePermissions record);
}