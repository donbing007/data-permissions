package com.xforceplus.ultraman.permissions.pojo.result;

/**
 * 保存状态码.
 * @version 0.1 2019/11/21 16:03
 * @author dongbin
 * @since 1.8
 */
public enum ManagementStatus {

    UNKNOWN(0), // 未知.
    SUCCESS(1), // 成功
    FAIL(2), // 发生错误.
    LOSS(3), // 丢失
    REPETITION(4), // 数据重复
    INVALID_PARAMETER(5); //错误的参数


    private int value;

    private ManagementStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ManagementStatus getInstance(int symbol) {
        for (ManagementStatus status : ManagementStatus.values()) {
            if (status.getValue() == symbol) {
                return status;
            }
        }

        return UNKNOWN;
    }
}
