package com.rechargeweb.rechargeweb.Model;

public class AepsLogIn {

    private String agentId;
    private String message;

    public AepsLogIn(String agentId, String message) {
        this.agentId = agentId;
        this.message = message;
    }

    public AepsLogIn(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getMessage() {
        return message;
    }
}
