package com.xforceplus.ultraman.permissions.service.define;

/**
 * 检查后的状态.
 * @version 0.1 2019/11/13 15:22
 * @author dongbin
 * @since 1.8
 */
public enum CheckStatus {

    // 验证通过
    PASS(0),

    // 拒绝
    DENIAL(1),

    // 更新后通过,表示执行语句需要更新
    UPDATE(3),

    // 不支持的语句
    NOT_SUPPORT(4),

    // error
    ERROR(5);

    private int value;

    private CheckStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CheckStatus getInstance(int symbol) {
        for (CheckStatus status : CheckStatus.values()) {
            if (symbol == status.getValue()) {
                return status;
            }
        }

        return null;
    }
}
