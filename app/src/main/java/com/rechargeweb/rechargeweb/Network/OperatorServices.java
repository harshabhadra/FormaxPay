package com.rechargeweb.rechargeweb.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OperatorServices {

    @GET("rech_plan.php?")
    Call<String> getPlans(@Query("format") String format,
                          @Query("token") String token,
                          @Query("type") String rechargeType,
                          @Query("cirid") String circleId,
                          @Query("opid") String optId);
}
