package com.rechargeweb.rechargeweb.Model;

public class OperatorByS {

    String operatorName, operatorCode,logo;

    public OperatorByS(String operatorName, String operatorCode, String logo) {
        this.operatorName = operatorName;
        this.operatorCode = operatorCode;
        this.logo = logo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public String getLogo() {
        return logo;
    }
}
