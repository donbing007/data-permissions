package com.xforceplus.ultraman.permissions.service.aop;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.result.ManagementStatus;
import com.xforceplus.ultraman.permissions.pojo.result.Result;
import com.xforceplus.ultraman.permissions.pojo.result.service.DataRuleManagementResult;
import com.xforceplus.ultraman.permissions.repository.RoleRepository;
import com.xforceplus.ultraman.permissions.repository.entity.Role;
import com.xforceplus.ultraman.permissions.repository.entity.RoleExample;
import com.xforceplus.ultraman.permissions.service.AbstractBaseTest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * AuthorizationCheckBeforePoint Tester.
 *
 * @author <Authors name>
 * @version 1.0 12/03/2019
 * @since <pre>Dec 3, 2019</pre>
 */
public class AuthorizationCheckBeforePointTest extends AbstractBaseTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testCheckAuth() throws Throwable {

        List<Pack> caseData = buildCase();
        caseData.parallelStream().forEach(pack -> {

            Object returnValue;
            try {
                RoleRepository roleRepository = mock(RoleRepository.class);
                when(roleRepository.selectByExample(any(RoleExample.class))).thenReturn(pack.findRoles);
                when(roleRepository.insert(any(Role.class))).thenReturn(1);

                MethodSignature methodSignature = mock(MethodSignature.class);
                when(methodSignature.getDeclaringType()).thenReturn(TestTarget.class);
                when(methodSignature.getDeclaringTypeName()).thenReturn(TestTarget.class.getName());
                when(methodSignature.getReturnType()).thenReturn(pack.targetMethod.getReturnType());
                when(methodSignature.getMethod()).thenReturn(pack.targetMethod);

                ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
                when(joinPoint.getArgs()).thenReturn(new Object[]{new Authorization("r1", "t1")});
                when(joinPoint.getSignature()).thenReturn(methodSignature);
                when(joinPoint.proceed()).thenReturn(pack.expectedResult);

                AuthorizationCheckBeforePoint point = new AuthorizationCheckBeforePoint();

                injectField(point, "roleRepository", roleRepository);


                returnValue = point.checkAuthorization(joinPoint);

                Assert.assertNotNull(returnValue);
                Assert.assertEquals(pack.expectedResult, returnValue);

            } catch (Throwable ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }

        });
    }

    private class Pack {
        private Method targetMethod;
        private List<Role> findRoles;
        private Object expectedResult;

        public Pack(Method targetMethod, List<Role> findRoles, Object expectedResult) {
            this.targetMethod = targetMethod;
            this.findRoles = findRoles;
            this.expectedResult = expectedResult;
        }
    }

    private List<Pack> buildCase() throws Exception {
        List<Pack> datas = new ArrayList();
        datas.add(
            new Pack(
                TestTarget.class.getMethod("method0", new Class[]{Authorization.class}),
                Collections.emptyList(),
                new Result(ManagementStatus.LOSS) {
                }
            )
        );

        datas.add(
            new Pack(
                TestTarget.class.getMethod("method1", new Class[]{Authorization.class}),
                Collections.emptyList(),
                new Result(ManagementStatus.SUCCESS, "method1") {
                }
            )
        );

        datas.add(
            new Pack(
                TestTarget.class.getMethod("method2", new Class[]{Authorization.class}),
                Collections.emptyList(),
                new Result(ManagementStatus.LOSS) {
                }
            )
        );

        datas.add(
            new Pack(
                TestTarget.class.getMethod("method3", new Class[]{Authorization.class}),
                Collections.emptyList(),
                new DataRuleManagementResult(ManagementStatus.SUCCESS, "method3")
            )
        );

        Role role = new Role();
        role.setRoleExternalId("r1");
        role.setTenantId("t1");
        datas.add(
            new Pack(
                TestTarget.class.getMethod("method0", new Class[]{Authorization.class}),
                Arrays.asList(role),
                new Result(ManagementStatus.SUCCESS, "method0"){}
            )
        );

        datas.add(
            new Pack(
                TestTarget.class.getMethod("method3", new Class[]{Authorization.class}),
                Arrays.asList(role),
                new DataRuleManagementResult(ManagementStatus.SUCCESS, "method3")
            )
        );

        return datas;
    }

    static class TestTarget {

        @AuthorizationCheck(NoAuthorizationPlan.ERROR)
        public Result method0(Authorization auth) {
            return new Result(ManagementStatus.SUCCESS, "method0") {
            };
        }

        @AuthorizationCheck(NoAuthorizationPlan.CREATE)
        public Result method1(Authorization auth) {
            return new Result(ManagementStatus.SUCCESS, "method1") {
            };
        }

        @AuthorizationCheck(NoAuthorizationPlan.ERROR)
        public DataRuleManagementResult method2(Authorization auth) {
            return new DataRuleManagementResult(ManagementStatus.SUCCESS, "method2");
        }

        @AuthorizationCheck(NoAuthorizationPlan.CREATE)
        public DataRuleManagementResult method3(Authorization auth) {
            return new DataRuleManagementResult(ManagementStatus.SUCCESS, "method3");
        }
    }

} 
