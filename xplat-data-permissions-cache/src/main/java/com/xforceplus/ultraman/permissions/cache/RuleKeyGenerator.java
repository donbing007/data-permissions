package com.xforceplus.ultraman.permissions.cache;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * service 的缓存 key 生成器.
 * 需要在编译时指定 "-parameters" 参数.
 * 否则无法取得目标参数方法实际名称,造成查找 entity 失败.
 *
 * @version 0.1 2019/11/22 10:52
 * @auth dongbin
 * @since 1.8
 */
@Component("ruleSearchKeyGenerator")
public class RuleKeyGenerator implements KeyGenerator {

    private static final String ENTITY_FIELD_NAME = "entity";
    private static final String FIELD_RULE_FLAG = "FieldRule";
    private static final String DATA_RULE_FLAG = "DataRule";

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public Object generate(Object target, Method method, Object... params) {

        StringBuilder buff = new StringBuilder();
        if (target.getClass().getName().contains(FIELD_RULE_FLAG)
            || method.getName().contains(FIELD_RULE_FLAG)) {

            buff.append("rule-field-");

        } else if (target.getClass().getName().contains(DATA_RULE_FLAG)
            || method.getName().contains(DATA_RULE_FLAG)) {

            buff.append("rule-data-");

        } else {

            throw new RuntimeException("Can not support " + target.getClass());
        }


        String entity = null;
        try {
            entity = searchEntity(method, params);
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        if (entity == null) {
            throw new RuntimeException("The cache key cannot be constructed because the entity value cannot be located.");
        }


        Authorization authorization = null;
        for (Object param : params) {
            if (Authorization.class.isInstance(param)) {
                authorization = (Authorization) param;
                break;
            }
        }

        if (authorization == null) {
            throw new RuntimeException("Unable to locate authorization information.");
        }


        buff.append(authorization.getTenant())
            .append("-")
            .append(authorization.getRole())
            .append("-")
            .append(entity == null ? "NULL" : entity);

        return buff.toString();
    }

    private String searchEntity(Method method, Object... params) throws Throwable {
        Parameter[] parameters = method.getParameters();
        int index = 0;
        String entity;

        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        for (Parameter parameter : parameters) {
            entity = getEntity(parameter, parameterNames[index], params[index++]);
            if (entity != null) {
                return entity;
            }
        }

        return null;
    }

    private String getEntity(Parameter parameter, String parameterName, Object param) throws Throwable {

        if (isEntity(parameterName, parameter.getType())) {
            return param.toString();
        }

        Field[] fields = param.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isEntity(field.getName(), field.getType())) {
                field.setAccessible(true);
                return field.get(param).toString();
            }
        }

        return null;
    }

    private boolean isEntity(String name, Class clazz) {
        return ENTITY_FIELD_NAME.equals(name) && String.class.equals(clazz);
    }
}
