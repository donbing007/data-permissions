package com.xforceplus.ultraman.permissions.rule.convert;

/**
 * 转换.
 * @version 0.1 2019/11/11 16:39
 * @auth dongbin
 * @since 1.8
 */
public interface Converting<R,V> {

    /**
     * 转换目标为另一个实例.
     * @param value 目标.
     * @return 结果.
     */
    R convert(V value);
}
