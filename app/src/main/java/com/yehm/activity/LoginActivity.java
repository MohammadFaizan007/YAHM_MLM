package com.yehm.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseActivity;
import com.yehm.model.ResponseLogin;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_reg_mob_no)
    TextInputEditText etRegMobNo;
    @BindView(R.id.input_layout_reg_no)
    TextInputLayout inputLayoutRegNo;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.register)
    TextView register;

    private String mobileNo_st = "", password_st = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_btn, R.id.forgot_password, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (Validation()) {
                    if (NetworkUtils.getConnectivityStatus(context) != 0) {
                        getLogin();
                    } else {
                        showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
                    }
                }
                break;
            case R.id.forgot_password:
                goToActivity(LoginActivity.this, ForgotPassword.class, null);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.register:
                goToActivity(LoginActivity.this, SignUp.class, null);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
    }

    private boolean Validation() {
        try {
            mobileNo_st = etRegMobNo.getText().toString().trim();
            password_st = etPassword.getText().toString().trim();
            if (mobileNo_st.length() == 0) {
                mobileNo_st = "";
                etRegMobNo.setError("Please Enter Login Id");
                requestFocus(etRegMobNo);
                return false;
            } else {
                inputLayoutRegNo.setErrorEnabled(false);
            }
            if (password_st.length() == 0) {
                password_st = "";
                etPassword.setError("Please Enter Password");
                requestFocus(etPassword);
                return false;
            } else {
                inputLayoutPassword.setErrorEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void getLogin() {
        showLoading();
        JsonObject param = new JsonObject();
        param.addProperty("LoginID", mobileNo_st);
        param.addProperty("Password", password_st);
        Call<ResponseLogin> loginCall = apiServices.getLoginMember(param);
        Log.e("Request", param.toString());
        loginCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(@NonNull Call<ResponseLogin> call, @NonNull Response<ResponseLogin> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    PreferencesManager.getInstance(context).setUSERID(response.body().getAuthenticate().getLoginId());
                    PreferencesManager.getInstance(context).setMEMBERID(response.body().getAuthenticate().getFkMemID());
                    PreferencesManager.getInstance(context).setMOBILE(response.body().getAuthenticate().getMobile());
                    PreferencesManager.getInstance(context).setPROFILEPIC(response.body().getAuthenticate().getProfilepic());
                    PreferencesManager.getInstance(context).setNAME(response.body().getAuthenticate().getFirstName());
                    PreferencesManager.getInstance(context).setDoj(response.body().getAuthenticate().getDoj());
                    PreferencesManager.getInstance(context).setDoa(response.body().getAuthenticate().getDoa());
                    goToActivityWithFinish(LoginActivity.this, MainContainer.class, null);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    showToastS(response.body().getResponse() + "\nInvalid Login Credential");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseLogin> call, @NonNull Throwable t) {
                Log.e("fail==", t.toString());
                hideLoading();
            }
        });
    }
}
