package com.xforceplus.ultraman.permissions.sql.define.arithmetic;

/**
 * 运行符号.
 * @version 0.1 2019/10/29 19:08
 * @auth dongbin
 * @since 1.8
 */
public enum ArithmeticSymbol {

    ADDITION("+"),
    SUBTRACTION("-"),
    DIVISION("/"),
    MULTIPLICATION("*"),
    MODULO("%"),
    BITWISE_AND("&"),
    BITWISE_LEFT_SHIFT("<<"),
    BITWISE_RIGHT_SHIFT(">>"),
    BITWISE_OR("|"),
    BITWISE_XOR("^"),
    CONCAT("||"),
    INTEGER_DIVISION("DIV");

    private String symbol;

    private ArithmeticSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static ArithmeticSymbol getInstance(String symbol) {
        String noSpaceSymbol = symbol.trim();
        for (ArithmeticSymbol operator : ArithmeticSymbol.values()) {
            if (operator.getSymbol().equals(noSpaceSymbol)) {
                return operator;
            }
        }

        return null;
    }


}
