package com.rechargeweb.rechargeweb.Network;

import com.rechargeweb.rechargeweb.Model.ElectricStatus;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Operator {

    String BASE_URL = "https://rechargewebs.com/web/api/";

    String RECH_URL = "https://api.rechapi.com/";

    @GET("mob_details.php?")
    Call<String> getNumberDetails(@Query("format") String format,
                                  @Query("token") String token,
                                  @Query("mobile") int mobile);

    @GET("recharge_operator?")
    Call<String> getOperatorList(@Query("operator_type") String operator);

    @GET("recharge_operator?")
    Call<String> getOperatorListByState(@Query("operator_type") String operator, @Query("state") String state);

    @GET("do_recharge?")
    Call<String> getRechargeStatus(@Query("user_id") String user_id,
                                   @Query("auth_key") String auth,
                                   @Query("number") String number,
                                   @Query("operator_id") String operator_id,
                                   @Query("amount") String amount);

    @GET("recharge_report?")
    Call<String> getRechargeList(@Query("user_id") String user_id, @Query("auth_key") String auth);

    @GET("recharge_report?")
    Call<String> getReportByDate(@Query("user_id") String id, @Query("auth_key") String auth, @Query("from_date") String fromDate, @Query("to_date") String toDate);

    @GET("support?")
    Call<String> getSupport(@Query("auth_key") String auth);

    @GET("ebill_fetch?")
    Call<ElectricStatus> getElectricBillStatus(@Query("auth_key") String auth,
                                               @Query("customer_id") String consumer_id,
                                               @Query("operator_id") String operator_id);

    @GET("ebill_fetch?")
    Call<ElectricStatus> getElectricBillStatus(@Query("auth_key") String auth,
                                               @Query("customer_id") String consumer_id,
                                               @Query("operator_id") String operator_id,
                                               @Query("parameter2") String parameter2);

    @GET("bill_pay?")
    Call<String> getElectricBillPayDetails(@Query("auth_key") String key,
                                           @Query("user_id") String session_id,
                                           @Query("number") String consumer_id,
                                           @Query("operator_id") String code,
                                           @Query("amount") int amount,
                                           @Query("ref_id") String ref_id);

    @GET("accounts_statement?")
    Call<String> getPassbookDetails(@Query("user_id") String user_id,
                                    @Query("auth_key") String auth);

    @GET("accounts_statement?")
    Call<String> getPassbookDetails(@Query("user_id") String user_id,
                                    @Query("auth_key") String auth,
                                    @Query("from_date") String from,
                                    @Query("to_date") String todate);

    @GET("bank_details")
    Call<String> getBankDetails();

    @GET("credit_summary?")
    Call<String> getCreditSummary(@Query("user_id") String id,
                                  @Query("auth_key") String auth);

    @GET("credit_summary?")
    Call<String> getCreditSummaryByDate(@Query("user_id") String user_id,
                                        @Query("auth_key") String auth,
                                        @Query("from_date") String from,
                                        @Query("to_date") String todate);

    @GET("debit_summary?")
    Call<String> getDebititSummary(@Query("user_id") String id,
                                   @Query("auth_key") String auth);

    @GET("debit_summary?")
    Call<String> getDebitSummaryByDate(@Query("user_id") String user_id,
                                       @Query("auth_key") String auth,
                                       @Query("from_date") String from,
                                       @Query("to_date") String todate);

    @GET("coupon_report?")
    Call<String> getCouponPurchaseReport(@Query("user_id") String user_id,
                                         @Query("auth_key") String key);


    @GET("coupon_report?")
    Call<String> getCouponPurchaseReportByDate(@Query("user_id") String user_id,
                                               @Query("auth_key") String key,
                                               @Query("from_date") String from,
                                               @Query("to_date") String toDate);

    @POST("remitter_details")
    @FormUrlEncoded
    Call<String> getRemitterDetails(@Field("auth_key") String auth_key, @Field("mobile") String number);

    @POST("remitter_validate")
    @FormUrlEncoded
    Call<String> validateRemitter(@Field("auth_key") String auth,
                                  @Field("mobile") String mobile,
                                  @Field("remitter_id") String remitter_id,
                                  @Field("otp") String otp);

    @POST("remitter_register")
    @FormUrlEncoded
    Call<String> registerRemitter(@Field("auth_key") String auth,
                                  @Field("mobile") String mobile,
                                  @Field("first_name") String firstName,
                                  @Field("last_name") String last_name,
                                  @Field("pincode") String pincode);

    @POST("beneficiary_register")
    @FormUrlEncoded
    Call<String> addBeneficiary(@Field("auth_key") String auth,
                                @Field("mobile") String mobile,
                                @Field("remitter_id") String remitter_id,
                                @Field("name") String name,
                                @Field("ifsc") String ifscCode,
                                @Field("account") String accountNumber);

    @POST("account_validate")
    @FormUrlEncoded
    Call<String> validateAccount(@Field("user_id") String session_id,
                                 @Field("auth_key") String auth,
                                 @Field("account") String account,
                                 @Field("ifsc") String ifscCode,
                                 @Field("mobile") String mobile);

    @POST("beneficiary_delete")
    @FormUrlEncoded
    Call<String> deleteBeneficiary(@Field("auth_key") String auth,
                                   @Field("beneficiary_id") String beneficiary_id,
                                   @Field("remitter_id") String remitter_id);

    @POST("beneficiary_delete_validate")
    @FormUrlEncoded
    Call<String> deleteBenValidate(@Field("auth_key") String auth,
                                   @Field("beneficiary_id") String ben_id,
                                   @Field("remitter_id") String remitter_id,
                                   @Field("otp") String otp);

    @POST("send_money")
    @FormUrlEncoded
    Call<String> transferMoney(@Field("user_id") String session_id,
                               @Field("auth_key") String auth_key,
                               @Field("mobile") String mobile,
                               @Field("remitter_id") String remitter_id,
                               @Field("name") String name,
                               @Field("ifsc") String ifsc,
                               @Field("account") String account,
                               @Field("beneficiary_id") String bennificiaqry_id,
                               @Field("amount") String amount);

    @POST("aeps_login")
    @FormUrlEncoded
    Call<String> aepsLogIn(@Field("user_id") String session_id,
                           @Field("service_type") String service_type,
                           @Field("auth_key") String auth_key);

    @POST("aeps_kyc")
    @Multipart
    Call<String> submitKyc(@Part("user_id") String session_id,
                           @Part("auth_key") String auth,
                           @Part("name") String name,
                           @Part("shop_name") String shop_name,
                           @Part("dob") String dob,
                           @Part("email") String email,
                           @Part("address") String address,
                           @Part("pincode") String pincode,
                           @Part("state") String state,
                           @Part("mobile") String mobile,
                           @Part("city") String city,
                           @Part("aadhaar_no") String adhar,
                           @Part("pan_no") String panNo,
                           @Part("aadhaar_img\";filename=\"myaadhar.jpg\"") RequestBody aadharImageUrl,
                           @Part("pan_img\";filename=\"myPan.jpg\"") RequestBody panImageUrl,
                           @Part("service_type") String service);

    @POST("aeps_login")
    @FormUrlEncoded
    Call<String>paisanikalaepslogin(@Field("user_id") String session_id,
                                    @Field("service_type") String service_type,
                                    @Field("auth_key") String auth_key);


    @POST("aeps_kyc")
    @Multipart
    Call<String> submitPaisanikalKyc(@Part("user_id") String session_id,
                           @Part("auth_key") String auth,
                           @Part("name") String name,
                           @Part("shop_name") String shop_name,
                           @Part("dob") String dob,
                           @Part("email") String email,
                           @Part("address") String address,
                           @Part("pincode") String pincode,
                           @Part("state") String state,
                           @Part("mobile") String mobile,
                           @Part("city") String city,
                           @Part("aadhaar_no") String adhar,
                           @Part("pan_no") String panNo,
                           @Part("aadhaar_img\";filename=\"myaadhar.jpg\"") RequestBody aadharImageUrl,
                           @Part("pan_img\";filename=\"myPan.jpg\"") RequestBody panImageUrl,
                           @Part("service_type") String service);

    @POST("insert_aeps_log")
    @FormUrlEncoded
    Call<String>sendAepsDetails(@Field("user_id")String session_id,
                                @Field("auth_key")String auth,
                                @Field("service_type")String service,
                                @Field("amount")String amount,
                                @Field("order_id")String orderId,
                                @Field("mobile")String mobile);
}
