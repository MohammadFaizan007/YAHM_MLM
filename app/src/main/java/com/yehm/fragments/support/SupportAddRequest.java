package com.yehm.fragments.support;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseActivity;
import com.yehm.model.ResponseSupportAddRequest;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportAddRequest extends BaseActivity {
    @BindView(R.id.side_menu)
    ImageView sideMenu;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.support_title)
    TextView supporttitle;
    @BindView(R.id.et_subject)
    EditText etSubject;
    @BindView(R.id.request_details)
    EditText requestDetails;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.cancel)
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support);
        ButterKnife.bind(this);
        ((TextView) findViewById(R.id.support_title)).setText(Html.fromHtml("SEND US <font color=#19673D>YOUR QUERY</font>"), TextView.BufferType.SPANNABLE);
        title.setText("Support");
    }

    @OnClick({R.id.update, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update:
                if (NetworkUtils.getConnectivityStatus(context) != 0) {
                    if (Validation()) {
                        getSaveRequest();
                    }
                } else {
                    showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
                }
                break;
            case R.id.cancel:
                getReset();
                break;
        }
    }

    private void getSaveRequest() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
        object.addProperty("Subject", etSubject.getText().toString().trim());
        object.addProperty("Message", requestDetails.getText().toString().trim());
        Call<ResponseSupportAddRequest> addRequestCall = apiServices.getSupportSend(object);
        addRequestCall.enqueue(new Callback<ResponseSupportAddRequest>() {
            @Override
            public void onResponse(Call<ResponseSupportAddRequest> call, Response<ResponseSupportAddRequest> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        try {
                            LoggerUtil.logItem(response.body());
                            showMessage("Request Updated Sucessfully");
                            finish();
                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        } catch (Error | Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showMessage(response.body().getResponse());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSupportAddRequest> call, Throwable t) {
                hideLoading();
            }
        });
    }

    public void pressBack(View v) {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private boolean Validation() {
        if (etSubject.getText().toString().trim().length() == 0) {
            etSubject.setError("Please select Subject Type");
            return false;
        } else if (requestDetails.getText().toString().trim().length() == 0) {
            requestDetails.setError("Please enter your Enquiry Details");
            return false;
        }
        return true;
    }

    private void getReset() {
        etSubject.setText("");
        requestDetails.setText("");
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
