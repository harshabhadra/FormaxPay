package com.rechargeweb.rechargeweb;

public class Keys {

    static {
        System.loadLibrary("native-lib");
    }

    public native String apiKey();

    public native String memberId();

    public native String apiPassword();

    public native String merchantId();

    public native String payementGatePass();
}
