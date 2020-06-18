package com.yehm.retrofit;


import com.google.gson.JsonObject;
import com.yehm.model.CheckDownlineResponse;
import com.yehm.model.DownlineResponse;
import com.yehm.model.EWalletLedgerResponse;
import com.yehm.model.EwalletRequestResponse;
import com.yehm.model.EwalletRequestSubmitResponse;
import com.yehm.model.MyIncomeResponse;
import com.yehm.model.PayoutLedgerResponse;
import com.yehm.model.ResponseChangePassword;
import com.yehm.model.ResponseDashboard;
import com.yehm.model.ResponseDirectMember;
import com.yehm.model.ResponseForgotPassword;
import com.yehm.model.ResponseLogin;
import com.yehm.model.ResponseManageOrder;
import com.yehm.model.ResponseProductList;
import com.yehm.model.ResponseSignUp;
import com.yehm.model.ResponseSponsorName;
import com.yehm.model.ResponseSupportAddRequest;
import com.yehm.model.ResponseSupportList;
import com.yehm.model.ResponseTreeView;
import com.yehm.model.ResponseUpdateMemberPersonalDetails;
import com.yehm.model.ResponseValidateParent;
import com.yehm.model.ResponseViewOrderItem;
import com.yehm.model.ResponseViewProfile;
import com.yehm.model.ViewIncomeResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiServices {

    @POST("LoginMember")
    Call<ResponseLogin> getLoginMember(@Body JsonObject object);

    @POST("Dashboard")
    Call<ResponseDashboard> getDashboard(@Body JsonObject object);

    @POST("UserProfile")
    Call<ResponseViewProfile> getUserProfile(@Body JsonObject object);

    @POST("ManageOrder")
    Call<ResponseManageOrder> getManageOrder(@Body JsonObject object);

    @POST("ViewOrderItem")
    Call<ResponseViewOrderItem> getViewOrderItem(@Body JsonObject object);

    @POST("Directs")
    Call<ResponseDirectMember> getDirects(@Body JsonObject object);

    @POST("TicketsSupport")
    Call<ResponseSupportList> getTicketsSupport(@Body JsonObject object);

    @POST("SupportSend")
    Call<ResponseSupportAddRequest> getSupportSend(@Body JsonObject object);

    @POST("ForgotPassword")
    Call<ResponseForgotPassword> getForgotPassword(@Body JsonObject object);

    @POST("ChangePassword")
    Call<ResponseChangePassword> getChangePassword(@Body JsonObject object);


    ////////////////////////////////////////////////////////////////////
    @POST("MyGenealogy")
    Call<ResponseTreeView> getTreeView(@Body JsonObject object);

    @POST("CheckDownlineForTree")
    Call<CheckDownlineResponse> getCheckDownline(@Body JsonObject object);

    @POST("Downline")
    Call<DownlineResponse> getDownline(@Body JsonObject object);

    @POST("EWalletRequest")
    Call<EwalletRequestResponse> getEwalletReqstLst(@Body JsonObject object);

    @POST("NewWalletRequest")
    Call<EwalletRequestSubmitResponse> getEwalletRequestSubmit(@Body JsonObject object);

    @POST("PayoutRequest")
    Call<PayoutLedgerResponse> getPayoutLedger(@Body JsonObject object);

    @POST("EWalletLedger")
    Call<EWalletLedgerResponse> getEwalletLedger(@Body JsonObject object);

    @POST("GetSponserName")
    Call<ResponseSponsorName> getGetSponsorName(@Body JsonObject object);

    @POST("Register")
    Call<ResponseSignUp> getRegistration(@Body JsonObject object);

    @POST("ValidateParent")
    Call<ResponseValidateParent> getValidateParent(@Body JsonObject object);

    @GET
    Call<String> getOtp(@Url String url);

    @POST("UpdateMemberPersonalDetails")
    Call<ResponseUpdateMemberPersonalDetails> getUpdateMemberPersonalDetails(@Body JsonObject object);

    @POST("BankDetailsUpdate")
    Call<ResponseUpdateMemberPersonalDetails> getUpdateMemberBankDetails(@Body JsonObject object);

    @POST("MyIncome")
    Call<MyIncomeResponse> getIncome(@Body JsonObject object);

    @POST("PayoutDetails")
    Call<ViewIncomeResponse> getViewIncomeDetails(@Body JsonObject object);

    @POST("ProductList")
    Call<ResponseProductList> getProductList(@Body JsonObject object);

    @Multipart
    @POST("MediaUpload")
    Call<JsonObject> uploadImage(@Part("Fk_MemId") RequestBody customer_id,
                                 @Part("imageType") RequestBody imgType,
                                 @Part() MultipartBody.Part file);

}
