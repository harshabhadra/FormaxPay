package com.rechargeweb.rechargeweb.Model;

public class NumberDetect {

    private String service;
    private String location;
    private String circleId;
    private String opId;

    public NumberDetect(String service, String location, String circleId, String opId) {
        this.service = service;
        this.location = location;
        this.circleId = circleId;
        this.opId = opId;
    }

    public NumberDetect(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }
}
