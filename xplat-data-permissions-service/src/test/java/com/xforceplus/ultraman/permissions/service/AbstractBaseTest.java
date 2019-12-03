package com.xforceplus.ultraman.permissions.service;

import org.junit.Ignore;

import java.lang.reflect.Field;

/**
 * @author dongbin
 * @version 0.1 2019/12/3 12:30
 * @since 1.8
 */
@Ignore
public abstract class AbstractBaseTest {

    // 注入字段.
    protected static void injectField(Object target, String fieldName, Object value) {
        Field field;
        try {
            field = target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (field == null) {
            throw new RuntimeException("Can not found field " + fieldName + ".");
        }


        if (field.getType().isInstance(value)) {
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("Type mismatch!");
        }
    }
}
