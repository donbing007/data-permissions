package com.xforceplus.ultraman.permissions.api;

import com.xforceplus.ultraman.permissions.api.assemble.HttpCodeAssembler;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.FieldRuleManagementResultV2;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRuleRequest;
import com.xforceplus.ultraman.permissions.service.RuleFieldRuleManagementService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字段规则.
 *
 * @author dongbin
 * @version 0.1 2019/11/22 17:45
 * @since 1.8
 */
@Api(value = "FieldRuleController")
@RestController
public class FieldRuleController {

    @Resource
    private RuleFieldRuleManagementService ruleFieldRuleManagementService;

    @ApiOperation(
            httpMethod = "GET",
            value = "罗列出指定授权信息的 entity 中的所有字段规则.",
            notes = "",
            response = FieldRuleManagementResult.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResult.class)}
    )
    @GetMapping(value = "/{tenant}/xpermissions/v1/field/rules", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity list(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "entity", value = "实体", required = false)
            @RequestParam(value = "entity", required = false) String entity,

            @ApiParam(name = "start", value = "开始的 id", defaultValue = "0")
            @RequestParam(value = "start", required = false, defaultValue = "0") long start,

            @ApiParam(name = "limit", value = "需要的数量", defaultValue = "10")
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        Authorization authorization = new Authorization(role, tenant);
        Continuation continuation = new Continuation(start, limit);

        FieldRuleManagementResult result = ruleFieldRuleManagementService.list(authorization, entity, continuation);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "GET",
            value = "罗列出指定授权信息的 entity 中的所有字段规则.",
            notes = "",
            response = FieldRuleManagementResultV2.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResultV2.class)}
    )
    @GetMapping(value = "/{tenant}/xpermissions/v2/field/rules", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity listV2(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "entity", value = "实体", required = false)
            @RequestParam(value = "entity", required = false) String entity
    ) {
        Authorization authorization = new Authorization(role, tenant);
        FieldRuleManagementResultV2 result = ruleFieldRuleManagementService.listV2(authorization, entity);
        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "POST",
            value = "保存新的字段规则或者更新规则.",
            notes = "",
            response = FieldRuleManagementResult.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResult.class)}
    )
    @PostMapping(value = "/{tenant}/xpermissions/v1/field/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity save(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "rule", value = "字段规则", required = true, example = "{entity='entity', field='*'}")
            @RequestBody FieldRule rule) {
        Authorization authorization = new Authorization(role, tenant);

        FieldRuleManagementResult result = ruleFieldRuleManagementService.save(authorization, rule);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "POST",
            value = "新增新的字段规则.",
            notes = "",
            response = FieldRuleManagementResult.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResult.class)}
    )
    @PostMapping(value = "/{tenant}/xpermissions/v2/field/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity insertBatch(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "rule", value = "字段规则", required = true, example = "{entity='entity', field='*'}")
            @RequestBody FieldRuleRequest ruleRequest) {
        Authorization authorization = new Authorization(role, tenant);

        FieldRuleManagementResult result = ruleFieldRuleManagementService.insert(authorization, ruleRequest);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }


    @ApiOperation(
            httpMethod = "PUT",
            value = "修改字段规则.",
            notes = "",
            response = FieldRuleManagementResult.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResult.class)}
    )
    @PutMapping(value = "/{tenant}/xpermissions/v2/field/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateBatch(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "rule", value = "字段规则", required = true, example = "{entity='entity', field='*'}")
            @RequestBody FieldRuleRequest ruleRequest) {
        Authorization authorization = new Authorization(role, tenant);
        ruleFieldRuleManagementService.removeBatch(authorization,ruleRequest.getEntity());
        FieldRuleManagementResult result = ruleFieldRuleManagementService.insert(authorization, ruleRequest);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "DELETE",
            value = " 删除存在的规则..",
            notes = "",
            response = FieldRuleManagementResult.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResult.class)}
    )
    @DeleteMapping(value = "/{tenant}/xpermissions/v1/field/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity remove(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "rule", value = "字段规则", required = true, example = "{id='id',entity='entity', field='*'}")
            @RequestBody FieldRule rule) {
        Authorization authorization = new Authorization(role, tenant);
        FieldRuleManagementResult result = ruleFieldRuleManagementService.remove(authorization, rule);
        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }


    @ApiOperation(
            httpMethod = "DELETE",
            value = " 删除存在的规则..",
            notes = "",
            response = FieldRuleManagementResult.class,
            tags = {"field"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = FieldRuleManagementResult.class)}
    )
    @DeleteMapping(value = "/{tenant}/xpermissions/v2/field/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity removeBatch(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "entity", value = "表名", required = true) @RequestParam String entity) {
        Authorization authorization = new Authorization(role, tenant);
        FieldRuleManagementResult result = ruleFieldRuleManagementService.removeBatch(authorization, entity);
        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }
}
