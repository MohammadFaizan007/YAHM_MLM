package com.yehm.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.constants.UrlConstants;
import com.yehm.model.ResponseUpdateMemberPersonalDetails;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditBankDetails extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.account_holder_et)
    EditText accountHolderEt;
    @BindView(R.id.input_account_holder)
    TextInputLayout inputAccountHolder;
    @BindView(R.id.account_no_et)
    EditText accountNoEt;
    @BindView(R.id.input_account_no)
    TextInputLayout inputAccountNo;
    @BindView(R.id.ifsc_et)
    EditText ifscEt;
    @BindView(R.id.input_ifsc)
    TextInputLayout inputIfsc;
    @BindView(R.id.et_bank)
    EditText etBank;
    @BindView(R.id.input_et_bank)
    TextInputLayout inputEtBank;
    @BindView(R.id.branch_et)
    EditText branchEt;
    @BindView(R.id.input_branch)
    TextInputLayout inputBranch;
    @BindView(R.id.pan_et)
    EditText panEt;
    @BindView(R.id.input_pan)
    TextInputLayout inputPan;
    @BindView(R.id.aadhar_et)
    EditText aadharEt;
    @BindView(R.id.input_aadhar)
    TextInputLayout inputAadhar;
    @BindView(R.id.submit)
    Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_bank_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        accountHolderEt.setText(UrlConstants.profile.getApiUserProfile().getAccountHolderName());
        accountNoEt.setText(UrlConstants.profile.getApiUserProfile().getAccountNo());
        ifscEt.setText(UrlConstants.profile.getApiUserProfile().getIfscCode());
        etBank.setText(UrlConstants.profile.getApiUserProfile().getBankName());
        branchEt.setText(UrlConstants.profile.getApiUserProfile().getBranchName());
        panEt.setText(UrlConstants.profile.getApiUserProfile().getPanCard());
        aadharEt.setText(UrlConstants.profile.getApiUserProfile().getAadhaarNo());

//        Lock edit details as Anand sir said
//        lockEditText(accountHolderEt);
//        lockEditText(accountNoEt);
//        lockEditText(ifscEt);
//        lockEditText(etBank);
//        lockEditText(branchEt);
//        lockEditText(panEt);
//        lockEditText(aadharEt);
    }

    private void lockEditText(EditText et) {
        et.setClickable(false);
        et.setCursorVisible(false);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
    }


    @OnClick(R.id.submit)
    public void onViewClicked() {
        if (Validation()) {
            if (NetworkUtils.getConnectivityStatus(context) != 0) {
                updateBankDetails();
//            showToastS( "Please contact Admin" );
            } else {
                showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
            }
        }
    }

    private void updateBankDetails() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
        object.addProperty("AccountNo", accountNoEt.getText().toString());
        object.addProperty("BankName", etBank.getText().toString());
        object.addProperty("BranchName", branchEt.getText().toString());
        object.addProperty("AccountHolderName", accountHolderEt.getText().toString());
        object.addProperty("IFSCCode", ifscEt.getText().toString());
        object.addProperty("PanCard", panEt.getText().toString());
        object.addProperty("AadharNo", aadharEt.getText().toString());
        LoggerUtil.logItem(object);
        Call<ResponseUpdateMemberPersonalDetails> updateProfileCall = apiServices.getUpdateMemberBankDetails(object);
        updateProfileCall.enqueue(new Callback<ResponseUpdateMemberPersonalDetails>() {
            @Override
            public void onResponse(Call<ResponseUpdateMemberPersonalDetails> call, Response<ResponseUpdateMemberPersonalDetails> response) {
                hideLoading();
                Log.e("res", response.body().toString());
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    showToastS("Updated Successfully");
                } else {
                    showToastS(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateMemberPersonalDetails> call, Throwable t) {
                hideLoading();

            }
        });

    }

    public boolean Validation() {
        if (accountHolderEt.getText().toString().length() == 0) {
            accountHolderEt.setError("Please Enter Account Holder Name");
            requestFocus(accountHolderEt);
            return false;
        } else if (accountNoEt.getText().toString().length() == 0) {
            accountNoEt.setError("Please Enter Account Number");
            requestFocus(accountNoEt);
            return false;
        } else if (ifscEt.getText().toString().length() == 0) {
            ifscEt.setError("Please Enter IFSC code");
            requestFocus(ifscEt);
            return false;
        } else if (etBank.getText().toString().equalsIgnoreCase("")) {
            etBank.setError("Please Enter Bank Name");
            requestFocus(etBank);
            return false;
        } else if (branchEt.getText().toString().length() == 0) {
            branchEt.setError("Please Enter Branch Name");
            requestFocus(branchEt);
            return false;
        } else if (panEt.getText().toString().length() == 0) {
            panEt.setError("Please Enter Pan No.");
            requestFocus(panEt);
            return false;
        } else if (aadharEt.getText().toString().length() == 0) {
            aadharEt.setError("Please Enter Aadhar No.");
            requestFocus(aadharEt);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}