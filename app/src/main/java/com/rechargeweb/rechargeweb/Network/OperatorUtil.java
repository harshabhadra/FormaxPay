package com.rechargeweb.rechargeweb.Network;

public class OperatorUtil {

    private static String RECH_URL = "http://api.rechapi.com/";

    public static OperatorServices getOperatorServices(){

        return RetrofitClient2.getClient(RECH_URL).create(OperatorServices.class);
    }
}
