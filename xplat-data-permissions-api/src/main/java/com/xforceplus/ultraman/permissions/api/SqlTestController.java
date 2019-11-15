package com.xforceplus.ultraman.permissions.api;

import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.perissions.pojo.result.service.CheckResult;
import com.xforceplus.ultraman.permissions.api.assemble.HttpCodeAssembler;
import com.xforceplus.ultraman.permissions.service.RuleCheckService;
import com.xforceplus.ultraman.permissions.service.define.CheckStatus;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version 0.1 2019/11/14 15:32
 * @author dongbin
 * @since 1.8
 */
@Api(value = "SqlTestController")
@RestController("test")
public class SqlTestController {

    @Resource
    private RuleCheckService ruleCheckService;

    @ApiOperation(value = "测试 SQL 的规则验证.", notes = "", response = CheckResult.class, tags = {"sql",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "得到规则验证后的结果.", response = CheckResult.class)}
    )
    @GetMapping(value = "/{tenant}/{role}/v1/sql", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity test(
        @ApiParam(name = "sql", value = "原始 sql", required = true) @RequestParam("sql") String sql,
        @ApiParam(name = "tenant", value = "发起 SQL 的当前租户.", required = true) @PathVariable("tenant") String tenant,
        @ApiParam(name = "role", value = "发起 SQL 的当前租户角色.", required = true) @PathVariable("role") String role) {

        CheckResult result = ruleCheckService.check(sql, new Authorization(role, tenant));

        return new ResponseEntity(
            result,
            HttpCodeAssembler.assem(CheckStatus.getInstance(result.getCode()), HttpStatus.OK));

    }
}
