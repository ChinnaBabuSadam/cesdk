package com.cloudelements.cesdk.service.domain;

import org.apache.commons.lang.StringUtils;

public enum ExpressionOperator {
    AND("AND"),
    OR("OR"),
    EQUALS("="),
    NOTEQUALS("!="),
    LESSTHAN("<"),
    GREATERTHAN(">"),
    LESSTHANEQUALS("<="),
    GREATERTHANEQUALS(">="),
    LIKE("LIKE"),
    ILIKE("ILIKE"),
    NOT_LIKE("NOT LIKE"),
    IN("IN"),
    ISNULL("IS NULL"),
    ISNOTNULL("IS NOT NULL"),
    SIMILARTO("SIMILAR TO");

    private String symbol;

    private ExpressionOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static ExpressionOperator fromString(String opString) {
        if (StringUtils.isEmpty(opString)) { return null; }

        for (ExpressionOperator expressionOperator : ExpressionOperator.values()) {
            if (StringUtils.equalsIgnoreCase(opString, expressionOperator.getSymbol())) {
                return expressionOperator;
            }
        }
        return null;
    }

}
