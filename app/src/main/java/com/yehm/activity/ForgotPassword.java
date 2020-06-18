package com.yehm.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.gson.JsonObject;
import com.yehm.BuildConfig;
import com.yehm.R;
import com.yehm.constants.BaseActivity;
import com.yehm.model.ResponseChangePassword;
import com.yehm.model.ResponseForgotPassword;
import com.yehm.retrofit.ApiServices;
import com.yehm.retrofit.ServiceGenerator;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title)
    TextView title;
    private ImageView side_menu;
    private EditText mobile_no, login_id, otp_et, new_password, cnf_new_password;
    private CardView otp_cv, validate_cv, password_cv;
    private String mobile_st;
    private String loginid_st;
    private String old_pswd;
    private String mem_id;
    private String randomPin = "";
    private String new_password_st = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        title.setText("Forgot Password");
        try {
            Button send_otp = findViewById(R.id.send_otp);
            Button reset = findViewById(R.id.reset);
            Button verify_otp = findViewById(R.id.verify_otp);
            Button reset_otp = findViewById(R.id.reset_otp);
            Button submit_pswd = findViewById(R.id.submit_pswd);
            Button reset_pswd = findViewById(R.id.reset_pswd);

            login_id = findViewById(R.id.login_id);
            mobile_no = findViewById(R.id.mobile_no);
            otp_et = findViewById(R.id.otp_et);
            new_password = findViewById(R.id.new_password);
            cnf_new_password = findViewById(R.id.cnf_new_password);
            otp_cv = findViewById(R.id.otp_cv);
            validate_cv = findViewById(R.id.validate_cv);
            password_cv = findViewById(R.id.password_cv);
            side_menu = findViewById(R.id.side_menu);

            send_otp.setOnClickListener(this);
            reset.setOnClickListener(this);
            verify_otp.setOnClickListener(this);
            reset_otp.setOnClickListener(this);
            submit_pswd.setOnClickListener(this);
            reset_pswd.setOnClickListener(this);
            side_menu.setOnClickListener(this);

        } catch (Error e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getForgetPswd() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("LoginID", login_id.getText().toString().trim());
        object.addProperty("Mobile", mobile_no.getText().toString().trim());
        Call<ResponseForgotPassword> forgotPasswordCall = apiServices.getForgotPassword(object);
        forgotPasswordCall.enqueue(new Callback<ResponseForgotPassword>() {
            @Override
            public void onResponse(Call<ResponseForgotPassword> call, Response<ResponseForgotPassword> response) {
                LoggerUtil.logItem(response.body());
                if (response.isSuccessful()) ;
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    old_pswd = response.body().getPassword();
                    mem_id = response.body().getLoginId();
                    String msg = "Dear " + response.body().getName() + ", One Time Password(OTP) for your request is " + randomPin + ", OTP is valid for 5 min. www.yehm.co.in";
                    msg = msg.replace(" ", "%20");
                    sendPassword(msg);
                } else {
                    hideLoading();
                    showToastS("Invalid Credentials! Please enter valid Mobile Number or Login Id.");
                }
            }

            @Override
            public void onFailure(Call<ResponseForgotPassword> call, Throwable t) {

            }
        });
    }

    public void sendPassword(String msg) {
        String url = BuildConfig.SMS_URL + mobile_st + "&msg=" + msg;
        ApiServices apiServices = ServiceGenerator.createService(ApiServices.class);
        Log.e("==Forgot msg====>> ", msg);
        Log.e("==URL====>> ", url);
        Call<String> call = apiServices.getOtp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                hideLoading();
                showToastS("OTP has been sent to your registered mobile number *******" + mobile_st.substring(6) + ".");
                validate_cv.setVisibility(View.GONE);
                otp_cv.setVisibility(View.VISIBLE);
                password_cv.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                hideLoading();
                showToastS("Something went wrong, Try again.");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_otp:
                if (NetworkUtils.getConnectivityStatus(context) != 0) {
                    if (Validation()) {
                        randomPin = generatePin();
                        getForgetPswd();
                    }
                } else {
                    createInfoDialog(context, "Alert", getString(R.string.alert_internet));
                }
                break;
            case R.id.reset:
                mobile_no.setText("");
                login_id.setText("");
                break;
            case R.id.verify_otp:
                matchOTP();
                break;
            case R.id.reset_otp:
                otp_et.setText("");
                break;
            case R.id.submit_pswd:
                if (ValidationPswd()) {
                    getChangePswd();
                }
                break;
            case R.id.reset_pswd:
                new_password.setText("");
                cnf_new_password.setText("");
                break;

            case R.id.side_menu:
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
        }
    }

    public void getChangePswd() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("LoginId", mem_id);
        object.addProperty("OldPassword", old_pswd);
        object.addProperty("NewPassword", new_password_st);
        object.addProperty("UpdatedBy", "");
        object.addProperty("Formname", "");
        Call<ResponseChangePassword> changePasswordCall = apiServices.getChangePassword(object);
        changePasswordCall.enqueue(new Callback<ResponseChangePassword>() {
            @Override
            public void onResponse(Call<ResponseChangePassword> call, Response<ResponseChangePassword> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        showToastS("Reset Your Password Successfully. Please Login with new password");
                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
                        showToastS(response.body().getResponse());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseChangePassword> call, Throwable t) {

            }
        });

    }


    private void matchOTP() {
        if (otp_et.getText().toString().trim().equals(randomPin)) {
            showToastS("Successfully verify OTP");
            otp_cv.setVisibility(View.GONE);
            password_cv.setVisibility(View.VISIBLE);
            validate_cv.setVisibility(View.GONE);
        } else {
            showToastS("Invalid OTP");
        }
    }

    private boolean Validation() {
        loginid_st = login_id.getText().toString().trim();
        mobile_st = mobile_no.getText().toString().trim();
        if (loginid_st.length() == 0) {
            showToastS("Login Id can't be empty");
            return false;
        }
        if (mobile_st.length() == 0) {
            showToastS("Mobile Number can't be empty");
            return false;
        }
        return true;
    }

    private boolean ValidationPswd() {
        new_password_st = new_password.getText().toString().trim();
        String conf_new_password_st = cnf_new_password.getText().toString().trim();
        if (new_password_st.length() == 0) {
            showToastS("New Password can't be empty");
            return false;
        } else if (conf_new_password_st.length() == 0) {
            showToastS("Confirm New Password can't be empty");
            return false;
        } else if (!new_password_st.equalsIgnoreCase(conf_new_password_st)) {
            showToastS("Confirm Password doesn't match with New Password.");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

