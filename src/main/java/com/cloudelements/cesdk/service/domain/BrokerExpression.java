package com.cloudelements.cesdk.service.domain;

import java.io.Serializable;

public class BrokerExpression implements Serializable {

    private static final long serialVersionUID = -863439174489160612L;
    String key;
    String value;
    ExpressionOperator operator;

    public BrokerExpression() {}

    public BrokerExpression(String key, String value, ExpressionOperator operator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ExpressionOperator getOperator() {
        return operator;
    }

    public void setOperator(ExpressionOperator operator) {
        this.operator = operator;
    }
}
