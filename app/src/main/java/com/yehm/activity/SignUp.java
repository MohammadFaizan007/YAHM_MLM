package com.yehm.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.yehm.BuildConfig;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseActivity;
import com.yehm.constants.VerhoeffAlgorithm;
import com.yehm.model.ResponseSignUp;
import com.yehm.model.ResponseSponsorName;
import com.yehm.model.ResponseValidateParent;
import com.yehm.retrofit.ApiServices;
import com.yehm.retrofit.ServiceGenerator;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;
import com.yehm.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends BaseActivity {
    @BindView(R.id.side_menu)
    ImageView sideMenu;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.refference_id_et)
    EditText refferenceIdEt;
    @BindView(R.id.input_refference_id)
    TextInputLayout inputRefferenceId;
    @BindView(R.id.under_ref_name_et)
    EditText underRefNameEt;
    @BindView(R.id.input_ref_name_et)
    TextInputLayout inputRefNameEt;
    @BindView(R.id.left)
    RadioButton left;
    @BindView(R.id.right)
    RadioButton right;
    @BindView(R.id.leg_grp)
    RadioGroup leg_grp;
    @BindView(R.id.under_place_id_et)
    EditText underPlaceIdEt;
    @BindView(R.id.input_under_place_id)
    TextInputLayout inputUnderPlaceId;
    @BindView(R.id.under_place_name_et)
    EditText underPlaceNameEt;
    @BindView(R.id.input_under_place_name)
    TextInputLayout inputUnderPlaceName;
    @BindView(R.id.first_name_et)
    EditText firstNameEt;
    @BindView(R.id.input_first_name)
    TextInputLayout inputFirstName;
    @BindView(R.id.last_name_et)
    EditText lastNameEt;
    @BindView(R.id.input_last_name)
    TextInputLayout inputLastName;
    @BindView(R.id.father_name_et)
    EditText fatherNameEt;
    @BindView(R.id.input_father_name)
    TextInputLayout inputFatherName;
    @BindView(R.id.gender_et)
    EditText genderEt;
    @BindView(R.id.input_gender)
    TextInputLayout inputGender;
    @BindView(R.id.dob_et)
    EditText dobEt;
    @BindView(R.id.input_dob)
    TextInputLayout inputDob;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.input_et_email)
    TextInputLayout inputEtEmail;
    @BindView(R.id.mobile_et)
    EditText mobileEt;
    @BindView(R.id.input_mobile_et)
    TextInputLayout inputMobileEt;
    @BindView(R.id.psn_no_et)
    EditText psnNoEt;
    @BindView(R.id.input_panno)
    TextInputLayout inputPanno;
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
    @BindView(R.id.adhar_card_et)
    EditText adharCardEt;
    @BindView(R.id.input_adhaarcode)
    TextInputLayout inputAdhaarcode;

    String loginId = "";
    String joinLeg = "";
    boolean openfromLogin = false;
    String sponsor_mem_id = "";
    String refferenceId_st = "", referrence_name_st = "", underPlaceId_st = "", firstName_st = "", lastName_st = "", fatherName_st = "", gender_st = "", dob_st = "", email_st = "", mobile_st = "", psnNo_st = "",
            address_st = "", pinCode_st = "", city_st = "", state_st = "", addhaarcard_st;
    NodeList nodelist;
    String state_id, c_state_id;
    Calendar cal = Calendar.getInstance();

    private int mYear, mMonth, mDay;

    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ButterKnife.bind(this);
        title.setText("Create Account");
        Bundle bundle = getIntent().getBundleExtra("android.intent.extra.INTENT");
        if (bundle != null) {
            openfromLogin = false;
            loginId = bundle.getString("referrence_id");
            refferenceIdEt.setText(loginId);
            underPlaceIdEt.setText(bundle.getString("place_under_id"));
            underPlaceNameEt.setText(bundle.getString("place_under_name"));
            refferenceIdEt.setClickable(false);
            refferenceIdEt.setCursorVisible(false);
            refferenceIdEt.setFocusable(false);
            underRefNameEt.setFocusable(false);
            underRefNameEt.setClickable(false);
            underRefNameEt.setCursorVisible(false);
            if (bundle.getString("selected_leg").equalsIgnoreCase("R")) {
                joinLeg = "R";
                right.setChecked(true);
                left.setChecked(false);
                right.setClickable(false);
                left.setClickable(false);
            } else {
                joinLeg = "L";
                right.setChecked(true);
                left.setChecked(false);
                right.setClickable(false);
                left.setClickable(false);
            }
            getLoginNameByID(refferenceIdEt.getText().toString().trim());
//            getUnderPlaceId( refferenceIdEt.getText().toString(), joinLeg );
        } else {
            openfromLogin = true;
            leg_grp.setOnCheckedChangeListener((radioGroup, i) -> {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (rb.getText().toString().equalsIgnoreCase("Left")) {
                    joinLeg = "L";
                } else {
                    joinLeg = "R";
                }
            });
        }

        leg_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.left) {
                    joinLeg = "L";
                } else if (checkedId == R.id.right) {
                    joinLeg = "R";
                }
                if (!refferenceIdEt.getText().toString().equalsIgnoreCase(""))//under_place_id_et
                    getUnderPlaceId(refferenceIdEt.getText().toString(), joinLeg);
                else {
                    ((RadioButton) findViewById(checkedId)).setChecked(false);
                    showToastS("Please Enter Reference id and then select joining leg.");
                }
            }

        });

        refferenceIdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getLoginNameByID(refferenceIdEt.getText().toString().trim());
                } else {
                    underRefNameEt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    }

    private void getLoginNameByID(final String etSponsorId) {
        JsonObject object = new JsonObject();
        object.addProperty("LoginID", etSponsorId);
        Call<ResponseSponsorName> sponsorNameCall = apiServices.getGetSponsorName(object);
        sponsorNameCall.enqueue(new Callback<ResponseSponsorName>() {
            @Override
            public void onResponse(Call<ResponseSponsorName> call, Response<ResponseSponsorName> response) {
                LoggerUtil.logItem(response.body());
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    underRefNameEt.setText(response.body().getMemberName());
//                    sponsor_mem_id = response.body().getFkMemId();
                } else {
                    underRefNameEt.setText("Invalid Id");
                }
            }

            @Override
            public void onFailure(Call<ResponseSponsorName> call, Throwable t) {

            }
        });
    }

    private void getUnderPlaceId(String reference_id, String leg) {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("SponsorCode", reference_id);
        object.addProperty("Leg", leg);
        Call<ResponseValidateParent> sponsorNameCall = apiServices.getValidateParent(object);
        sponsorNameCall.enqueue(new Callback<ResponseValidateParent>() {
            @Override
            public void onResponse(@Nullable Call<ResponseValidateParent> call, @Nullable Response<ResponseValidateParent> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    underPlaceIdEt.setText(response.body().getLoginId());
                    underPlaceNameEt.setText(response.body().getDisplayName());
                } else {
//                    underRefNameEt.setText( "Invalid Id" );
                }
            }

            @Override
            public void onFailure(@Nullable Call<ResponseValidateParent> call, @Nullable Throwable t) {

            }
        });
    }

    @OnClick({R.id.side_menu, R.id.submit, R.id.gender_et, R.id.dob_et})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.side_menu:
                hideKeyboard();
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
            case R.id.gender_et:
                getPopUpForGender();
                break;
            case R.id.dob_et:
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                datePicker(dobEt);
                break;
            case R.id.submit:
                if (Validation()) {
                    if (NetworkUtils.getConnectivityStatus(context) != 0) {
                        registerNewMember();
                    } else {
                        showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
                    }
                }
                break;
        }
    }

    private void registerNewMember() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("SponsorCode", refferenceId_st);
        object.addProperty("ParentCode", underPlaceId_st);
        object.addProperty("JoiningLeg", joinLeg);
        object.addProperty("FirstName", firstName_st);
        object.addProperty("LastName", lastName_st);
        object.addProperty("FatherName", fatherName_st);
        object.addProperty("DOB", dob_st);
        object.addProperty("Gender", gender_st);
        object.addProperty("AddressLine1", address_st);
        object.addProperty("State", state_st);
        object.addProperty("City", city_st);
        object.addProperty("PinCode", pinCode_st);
        object.addProperty("Mobile", mobile_st);
        object.addProperty("CreatedBy", "");
        object.addProperty("AadharNo", addhaarcard_st);

        LoggerUtil.logItem(object);
        Call<ResponseSignUp> signUpCall = apiServices.getRegistration(object);
        signUpCall.enqueue(new Callback<ResponseSignUp>() {
            @Override
            public void onResponse(Call<ResponseSignUp> call, Response<ResponseSignUp> response) {
                Log.e("res", response.body().toString());
                if (response.body().getResponse().equalsIgnoreCase("Success")) {
                    createInfoDialog_SignUp(context, "Congratulations",
                            "Dear " + response.body().getFirstName() +
                                    " , your Login-ID :" + response.body().getLoginId()
                                    + " and Password :" + response.body().getPassword());

                    String msg = "Congratulations Dear " + response.body().getFirstName() + " you have successfully registered with YOUTH ENERGY HEALTH MARKETING PVT.LTD, your welcome in healthy family your Login Id- "
                            + response.body().getLoginId() + " and Password-" + response.body().getPassword() + " . Login www.yehm.co.in";
                    msg.replace(" ", "%20");
                    getOTP(msg, response.body().getMobile().toString());
                } else {
                    showToastS(response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<ResponseSignUp> call, Throwable t) {
                hideLoading();

            }
        });

    }

    public void createInfoDialog_SignUp(Context context, String title, String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK", (dialog, id) -> {
            dialog.cancel();
            PreferencesManager.getInstance(context).clear();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void getOTP(String msg, String mobile) {
        String url = BuildConfig.SMS_URL + mobile + "&msg=" + msg.replace(" ", "%20");
        ApiServices apiServices = ServiceGenerator.createService(ApiServices.class);
        Log.e("==msg====>> ", msg);
        Log.e("==URL====>> ", url);
        Call<String> call = apiServices.getOtp(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                hideLoading();
                showToastS("LoginId and Password sent on your registered mobile no ******" + mobile.substring(6) + ".");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                hideLoading();
                showToastS("Something went wrong, Try again.");
            }
        });
    }

    public boolean Validation() {
        refferenceId_st = refferenceIdEt.getText().toString();
        referrence_name_st = underRefNameEt.getText().toString();
        underPlaceId_st = underPlaceIdEt.getText().toString();
        firstName_st = firstNameEt.getText().toString();
        lastName_st = lastNameEt.getText().toString();
        fatherName_st = fatherNameEt.getText().toString();
        gender_st = genderEt.getText().toString();
        dob_st = dobEt.getText().toString();
        email_st = etEmail.getText().toString();
        mobile_st = mobileEt.getText().toString();
        psnNo_st = psnNoEt.getText().toString();
        address_st = addressEt.getText().toString();
        pinCode_st = pinCodeEt.getText().toString();
        city_st = cityEt.getText().toString();
        state_st = stateEt.getText().toString();
        addhaarcard_st = adharCardEt.getText().toString().trim();

        if (refferenceId_st.length() == 0) {
            sponsor_mem_id = "";
            refferenceIdEt.setError("Please Enter Reference Id");
            requestFocus(refferenceIdEt);
            return false;
        } else if (referrence_name_st.length() == 0) {
            sponsor_mem_id = "";
            refferenceIdEt.setError("Please Enter valid Reference Id");
            requestFocus(refferenceIdEt);
            return false;
        } else if (underPlaceId_st.length() == 0) {
            showToastS("Please select Leg.");
            return false;
        } else if (joinLeg.equalsIgnoreCase("")) {
            showToastS("Please select Leg.");
            return false;
        } else if (firstName_st.length() == 0) {
            firstNameEt.setError("Please Enter First Name");
            requestFocus(firstNameEt);
            return false;
        } else if (gender_st.equalsIgnoreCase("")) {
            showToastS("Please select Gender.");
            return false;
        } else if (dob_st.length() == 0) {
            showToastS("Please select Date of Birth.");
            return false;
        } else if (mobile_st.length() == 0 || mobile_st.length() != 10) {
            mobileEt.setError("Please Enter Mobile Number");
            requestFocus(mobileEt);
            return false;
        } else if (adharCardEt.getText().toString().trim().length() < 12) {
            adharCardEt.setError("Enter valid Aadhar number");
            return false;
        } else if (!validateAadharNumber(adharCardEt.getText().toString().trim())) {
            adharCardEt.setError("Invalid aadhar number");
            adharCardEt.requestFocus();
            return false;
        } else if (pinCode_st.length() == 0) {
            pinCodeEt.setError("Please Enter Pin Code");
            requestFocus(pinCodeEt);
            return false;
        } else if (city_st.length() == 0) {
            pinCodeEt.setError("Please Enter valid Pin Code");
            requestFocus(pinCodeEt);
            return false;
        } else if (state_st.length() == 0) {
            pinCodeEt.setError("Please Enter valid Pin Code");
            requestFocus(pinCodeEt);
            return false;
        }

        return true;
    }

    private void getPopUpForGender() {
        PopupMenu popupg = new PopupMenu(this, genderEt);
        popupg.getMenuInflater().inflate(R.menu.menu_gender_type, popupg.getMenu());
        popupg.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    genderEt.setText(item.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        popupg.show();
    }

    private void datePicker(final EditText et) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
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


    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
}
