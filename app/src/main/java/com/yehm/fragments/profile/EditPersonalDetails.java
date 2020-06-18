package com.yehm.fragments.profile;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.yehm.BuildConfig;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.constants.UrlConstants;
import com.yehm.model.ResponseUpdateMemberPersonalDetails;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditPersonalDetails extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.first_name_et)
    EditText firstNameEt;
    @BindView(R.id.input_first_name)
    TextInputLayout inputFirstName;
    @BindView(R.id.last_name_et)
    EditText lastNameEt;
    @BindView(R.id.input_last_name)
    TextInputLayout inputLastName;
    @BindView(R.id.dob_et)
    EditText dobEt;
    @BindView(R.id.input_dob)
    TextInputLayout inputDob;
    @BindView(R.id.gender_et)
    EditText genderEt;
    @BindView(R.id.input_gender)
    TextInputLayout inputGender;
    @BindView(R.id.father_name_et)
    EditText fatherNameEt;
    @BindView(R.id.input_father_name)
    TextInputLayout inputFatherName;
    @BindView(R.id.et_nominee_name)
    EditText etNomineeName;
    @BindView(R.id.input_et_nominee_name)
    TextInputLayout inputEtNomineeName;
    @BindView(R.id.et_nominee_relation)
    EditText etNomineeRelation;
    @BindView(R.id.input_et_nominee_relation)
    TextInputLayout inputEtNomineeRelation;
    @BindView(R.id.marital_status)
    EditText maritalStatus;
    @BindView(R.id.input_marital_status)
    TextInputLayout inputMaritalStatus;
    @BindView(R.id.address_et)
    EditText addressEt;
    @BindView(R.id.input_address)
    TextInputLayout inputAddress;
    @BindView(R.id.pin_code_et)
    EditText pinCodeEt;
    @BindView(R.id.input_pin_code)
    TextInputLayout inputPinCode;
    @BindView(R.id.city_et)
    EditText cityEt;
    @BindView(R.id.input_city)
    TextInputLayout inputCity;
    @BindView(R.id.state_et)
    EditText stateEt;
    @BindView(R.id.input_state)
    TextInputLayout inputState;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.email_et)
    EditText emailEt;
    @BindView(R.id.input_email)
    TextInputLayout inputEmail;

    NodeList nodelist;
    String state_id, c_state_id;
    Calendar cal = Calendar.getInstance();
    private int mYear, mMonth, mDay;

    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_personal_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        firstNameEt.setText(UrlConstants.profile.getApiUserProfile().getFirstName());
        lastNameEt.setText(UrlConstants.profile.getApiUserProfile().getLastName());
        dobEt.setText(UrlConstants.profile.getApiUserProfile().getDob());
        genderEt.setText(UrlConstants.profile.getApiUserProfile().getGender());
        fatherNameEt.setText(UrlConstants.profile.getApiUserProfile().getFathersName());
        etNomineeName.setText(UrlConstants.profile.getApiUserProfile().getNomineeName());
        etNomineeRelation.setText(UrlConstants.profile.getApiUserProfile().getRelationwithNominee());
        maritalStatus.setText(UrlConstants.profile.getApiUserProfile().getMarritalStatus());
        addressEt.setText(UrlConstants.profile.getApiUserProfile().getAddress1());
        pinCodeEt.setText(UrlConstants.profile.getApiUserProfile().getPinCode());
        cityEt.setText(UrlConstants.profile.getApiUserProfile().getCity());
        stateEt.setText(UrlConstants.profile.getApiUserProfile().getState());

        pinCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideLoading();
                if (s.length() != 0 && s.length() == 6)
                    new getPinCodeStateCity().execute(BuildConfig.PINCODEURL + pinCodeEt.getText().toString().trim(), "a");
            }
        });


//        Lock edit details as Anand sir said
        lockEditText(firstNameEt);
        lockEditText(lastNameEt);
        lockEditText(dobEt);
        lockEditText(emailEt);
        lockEditText(genderEt);
        lockEditText(fatherNameEt);
        lockEditText(etNomineeName);
        lockEditText(etNomineeRelation);
        lockEditText(maritalStatus);
        lockEditText(addressEt);
        lockEditText(pinCodeEt);
        lockEditText(cityEt);
        lockEditText(stateEt);

    }

    private void lockEditText(EditText et) {
        et.setClickable(false);
        et.setCursorVisible(false);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
    }

    @OnClick({R.id.dob_et, R.id.gender_et, R.id.marital_status, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dob_et:
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                datePicker(dobEt);
                break;
            case R.id.gender_et:
                getPopUpForGender();
                break;
            case R.id.marital_status:
                getPopUpMaritalStatus();
                break;
            case R.id.submit:
//                if (Validation()) {
                if (NetworkUtils.getConnectivityStatus(context) != 0) {
//                    updatePersonalDetails();
                    showToastS("Please contact Admin");
                } else {
                    showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
                }
//                }
                break;
        }
    }

    private void updatePersonalDetails() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
        object.addProperty("DOB", dobEt.getText().toString());
        object.addProperty("Gender", genderEt.getText().toString());
        object.addProperty("FathersName", fatherNameEt.getText().toString());
        object.addProperty("NomineeName", etNomineeName.getText().toString());
        object.addProperty("RelationwithNominee", etNomineeRelation.getText().toString());
        object.addProperty("Mobile", PreferencesManager.getInstance(context).getMOBILE());
        object.addProperty("FirstName", firstNameEt.getText().toString());
        object.addProperty("LastName", lastNameEt.getText().toString());
        object.addProperty("MarritalStatus", maritalStatus.getText().toString());
        object.addProperty("Email", emailEt.getText().toString());
        object.addProperty("Address1", addressEt.getText().toString());
        object.addProperty("Address2", "");
        object.addProperty("Address3", "");
        object.addProperty("PinCode", pinCodeEt.getText().toString());
        object.addProperty("City", cityEt.getText().toString());
        object.addProperty("State", stateEt.getText().toString());
        LoggerUtil.logItem(object);
        Call<ResponseUpdateMemberPersonalDetails> updateProfileCall = apiServices.getUpdateMemberPersonalDetails(object);
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

    private void getPopUpForGender() {
        PopupMenu popupg = new PopupMenu(context, genderEt);
        popupg.getMenuInflater().inflate(R.menu.menu_gender_type, popupg.getMenu());
        popupg.setOnMenuItemClickListener(item -> {
            try {
                genderEt.setText(item.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
        popupg.show();
    }

    private void getPopUpMaritalStatus() {
        PopupMenu popupg = new PopupMenu(context, maritalStatus);
        popupg.getMenuInflater().inflate(R.menu.menu_marital_status, popupg.getMenu());
        popupg.setOnMenuItemClickListener(item -> {
            try {
                maritalStatus.setText(item.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        });
        popupg.show();
    }

    private void datePicker(final EditText et) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }

    private class getPinCodeStateCity extends AsyncTask<Object, Void, Void> {
        String view = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected Void doInBackground(Object... param) {
            try {
                this.view = (String) param[1];
                URL url = new URL((String) param[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
// Locate the Tag Name
                nodelist = doc.getElementsByTagName("PostOffice");
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                Log.e("===========> ", "getPinCodeStateCity");
                LoggerUtil.logItem(args);
                hideLoading();
                for (int temp = 0; temp < nodelist.getLength(); temp++) {
                    Node nNode = nodelist.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (view.equalsIgnoreCase("a")) {
                            stateEt.setText(getNode("State", eElement));
                            cityEt.setText(getNode("District", eElement));
                            state_id = getNode("StateID", eElement);
                        } else {
                            stateEt.setText(getNode("District", eElement));
                            cityEt.setText(getNode("State", eElement));
                            c_state_id = getNode("StateID", eElement);
                        }
                    }
                }
            } catch (Error | Exception e) {
                e.printStackTrace();
//                showErrorToast("Invalid Pincode. Please Retry.");
                hideLoading();
            }
        }
    }

}