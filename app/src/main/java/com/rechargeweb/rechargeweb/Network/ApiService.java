package com.rechargeweb.rechargeweb.Network;

import com.rechargeweb.rechargeweb.AepsReport;
import com.rechargeweb.rechargeweb.Model.Coupon;
import com.rechargeweb.rechargeweb.Model.Credential;
import com.rechargeweb.rechargeweb.Model.Details;
import com.rechargeweb.rechargeweb.Model.FundResponse;
import com.rechargeweb.rechargeweb.Model.Password;
import com.rechargeweb.rechargeweb.Model.Post;
import com.rechargeweb.rechargeweb.Profile;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;



public interface ApiService {


    @POST("login")
    @FormUrlEncoded
    Observable<Post> savePost(@Field("login_id") String login_id,
                              @Field("password") String password);

    @POST("profile?")
    @FormUrlEncoded
    Observable<Details> setDetailsPost(@Field("session_id") String session_id,
                                       @Field("auth_key") String auth_key);

    @POST("forgot_password?")
    @FormUrlEncoded
    Call<Password> resetPassword(@Field("user_id") String user_id);

    @POST("profile?")
    @FormUrlEncoded
    Observable<Profile> getProfileDetails(@Field("session_id") String session_id,
                                          @Field("auth_key") String auth_key);

    @GET("fund_request?")
    Call<FundResponse> getFundResponse(@Query("session_id") String id,
                                       @Query("auth_key") String auth,
                                       @Query("amount") String amount,
                                       @Query("bank") String bank,
                                       @Query("payment_mode") String paymentMode,
                                       @Query("payment_date") String paymentDate,
                                       @Query("transaction_id") String transaction_id,
                                       @Query("wallet_type") String walletType);

    @GET("psa_registration?")
    Call<FundResponse> getPsaResgistration(@Query("session_id") String id,
                                           @Query("auth_key") String auth,
                                           @Query("name") String name,
                                           @Query("shop_name") String shop,
                                           @Query("location") String location,
                                           @Query("pincode") String pincode,
                                           @Query("state") String state,
                                           @Query("mobile") String mobile,
                                           @Query("email") String email,
                                           @Query("pan_no") String pan);

    @POST("view_credentials")
    @FormUrlEncoded
    Call<Credential> viewCredential(@Field("session_id") String session_id,
                                    @Field("auth_key") String auth);

    @POST("buy_coupon")
    @FormUrlEncoded
    Call<Coupon>buyCoupon(@Field("auth_key") String auth,
                          @Field("session_id") String id,
                          @Field("vle_name") String name,
                          @Field("quantity") String quantity);
    @GET("change_password?")
    Observable<Password>changePassword(@Query("session_id")String session_id,
                                 @Query("auth_key")String auth_key,
                                 @Query("current_password")String cPassword,
                                 @Query("new_password")String nPassword,
                                 @Query("confrim_new_password")String conNewPassword);
    @POST("aeps_report")
    @FormUrlEncoded
    Call<List<AepsReport>>getAepsReport(@Field("user_id")String session_id,
                                        @Field("auth_key")String auth_key);
    @POST("aeps_report")
    @FormUrlEncoded
    Call<List<AepsReport>>getAepsReportByDate(@Field("user_id")String session_id,
                                              @Field("auth_key")String auth_key,
                                              @Field("from_date")String from_date,
                                              @Field("to_date")String to_date);
}
