package com.cloudelements.cesdk.service.domain;

import java.io.Serializable;

public class BrokerExpression implements Serializable {

    private static final long serialVersionUID = -863439174489160612L;
    String attribute;
    String value;
    ExpressionOperator operator;

    public BrokerExpression() {}

    public BrokerExpression(String attribute, String value, ExpressionOperator operator) {
        this.attribute = attribute;
        this.value = value;
        this.operator = operator;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
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
