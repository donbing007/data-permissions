package com.xforceplus.ultraman.permissions.rule.check;

import com.xforceplus.ultraman.permissions.rule.context.Context;

/**
 * 规则校验策略.
 * @version 0.1 2019/11/1 11:32
 * @auth dongbin
 * @since 1.8
 */
public interface Checker {

    /**
     * 规则检查.
     * @param context 规则检查上下文.
     */
    void check(Context context);
}
