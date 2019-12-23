package com.rechargeweb.rechargeweb;

public class Plans {

    private String rs;

    private String desc;

    private String validity;

    private String lastUpdate;

    public Plans(String rs, String desc, String validity, String lastUpdate) {
        this.rs = rs;
        this.desc = desc;
        this.validity = validity;
        this.lastUpdate = lastUpdate;
    }

    public Plans(String rs) {
        this.rs = rs;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
