package com.rechargeweb.rechargeweb.Network;

public class ApiUtills {

    private ApiUtills(){}

    public static String BASE_URL = "https://www.rechargewebs.com/web/api/";

    public static ApiService getApiService(){

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
