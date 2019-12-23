package com.rechargeweb.rechargeweb;

public class Roffer {

    private String rs;
    private String desc;

    public Roffer(String rs, String desc) {
        this.rs = rs;
        this.desc = desc;
    }
    public Roffer(String rs) {
        this.rs = rs;
    }

    public String getRs() {
        return rs;
    }

    public String getDesc() {
        return desc;
    }
}
