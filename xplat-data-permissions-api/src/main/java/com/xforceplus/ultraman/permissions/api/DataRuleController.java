package com.xforceplus.ultraman.permissions.api;

import com.xforceplus.ultraman.permissions.api.assemble.HttpCodeAssembler;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResult;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResultV2;
import com.xforceplus.ultraman.permissions.pojo.rule.DataRule;
import com.xforceplus.ultraman.permissions.service.RuleDataRuleManagementService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongbin
 * @version 0.1 2019/11/24 23:49
 * @since 1.8
 */
@Api(value = "DataRuleController")
@RestController
public class DataRuleController {
    @Resource
    private RuleDataRuleManagementService ruleDataRuleManagementService;

    @ApiOperation(
            httpMethod = "GET",
            value = "罗列出指定授权信息的 entity 中的所有字段规则.",
            notes = "",
            response = DataRuleManagementResult.class,
            tags = {"data"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = DataRuleManagementResult.class)}
    )
    @GetMapping(value = "/{tenant}/xpermissions/v1/data/rules", produces = {MediaType.APPLICATION_JSON_VALUE})
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

        DataRuleManagementResult result = ruleDataRuleManagementService.list(authorization, entity, continuation);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "GET",
            value = "罗列出指定授权信息的 entity 中的所有字段规则.",
            notes = "",
            response = DataRuleManagementResult.class,
            tags = {"data"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = DataRuleManagementResult.class)}
    )
    @GetMapping(value = "/{tenant}/xpermissions/v2/data/rules", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity listRule(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "entity", value = "实体", required = false)
            @RequestParam(value = "entity", required = false) String entity
            ) {

        Authorization authorization = new Authorization(role, tenant);

        DataRuleManagementResultV2 result = ruleDataRuleManagementService.listV2(authorization, entity);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "POST",
            value = "保存新的字段规则或者更新规则.",
            notes = "",
            response = DataRuleManagementResult.class,
            tags = {"data"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = DataRuleManagementResult.class)}
    )
    @PostMapping(value = "/{tenant}/xpermissions/v1/data/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity save(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "rule", value = "字段规则", required = true)
            @RequestBody DataRule rule) {

        Authorization authorization = new Authorization(role, tenant);

        DataRuleManagementResult result = ruleDataRuleManagementService.save(authorization, rule);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
            httpMethod = "DELETE",
            value = " 删除存在的规则..",
            notes = "",
            response = DataRuleManagementResult.class,
            tags = {"data"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "响应正确.", response = DataRuleManagementResult.class)}
    )
    @DeleteMapping(value = "/{tenant}/xpermissions/v1/data/rule", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity remove(
            @ApiParam(name = "tenant", value = "租户", required = true) @PathVariable("tenant") String tenant,
            @ApiParam(name = "role", value = "角色", required = true) @RequestParam("role") String role,
            @ApiParam(name = "rule", value = "字段规则", required = true)
            @RequestBody DataRule rule) {

        Authorization authorization = new Authorization(role, tenant);
        DataRuleManagementResult result = ruleDataRuleManagementService.remove(authorization, rule);
        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }
}
