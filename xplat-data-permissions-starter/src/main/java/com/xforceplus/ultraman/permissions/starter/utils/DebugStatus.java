package com.xforceplus.ultraman.permissions.starter.utils;

/**
 * debug 状态帮助.
 * @version 0.1 2019/11/26 19:21
 * @author dongbin
 * @since 1.8
 */
public class DebugStatus {

    private static boolean DEBUG = false;

    /**
     * 设置 debug 状态.
     * @param debug true 处于 debug 状态,false 非 debug 状态.
     */
    public static void setDebug(boolean debug) {
        DebugStatus.DEBUG = debug;
    }

    /**
     * 当前是否为 debug 状态.
     * @return true 是, false 不是.
     */
    public static boolean isDebug() {
        return DebugStatus.DEBUG;
    }
}
