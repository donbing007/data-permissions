package com.xforceplus.ultraman.permissions.api;

import com.xforceplus.ultraman.permissions.api.assemble.HttpCodeAssembler;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.page.Continuation;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.service.AuthorizationManagementResult;
import com.xforceplus.ultraman.permissions.service.RuleAuthorizationManagementService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dongbin
 * @version 0.1 2019/11/24 23:34
 * @since 1.8
 */
@Api(value = "AuthorizationController")
@RestController
public class AuthorizationController {

    @Resource
    private RuleAuthorizationManagementService ruleAuthorizationManagementService;

    @ApiOperation(
        httpMethod = "GET",
        value = "罗列出授权信息.",
        notes = "",
        response = List.class,
        tags = {"authorization"})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "响应正确.", response = AuthorizationManagementResult.class)}
    )
    @GetMapping(value = "/xpermissions/v1/authorizations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity list(
        @ApiParam(name = "start", value = "开始的 id", defaultValue = "0")
        @RequestParam(value = "start", required = false, defaultValue = "0") long start,

        @ApiParam(name = "limit", value = "需要的数量", defaultValue = "10")
        @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        Continuation continuation = new Continuation(start, limit);
        AuthorizationManagementResult result = ruleAuthorizationManagementService.list(continuation);

        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }

    @ApiOperation(
        httpMethod = "POST",
        value = "创建一个新的授权..",
        notes = "",
        response = ManagementStatus.class,
        tags = {"authorization"})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "响应正确.", response = AuthorizationManagementResult.class)}
    )
    @PostMapping(value = "/xpermissions/v1/authorization", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity save(
        @ApiParam(name = "authorization", value = "授权信息", required = true, example = "{role='string', tenant='string'}")
        @RequestBody Authorization authorization) {

        AuthorizationManagementResult result = ruleAuthorizationManagementService.save(authorization);
        return new ResponseEntity(result, HttpCodeAssembler.assemManagementStatus(result.getStatus(), HttpStatus.OK));
    }
}
