package com.yehm.constants;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.retrofit.ApiServices;
import com.yehm.retrofit.MvpView;
import com.yehm.retrofit.ServiceGenerator;
import com.yehm.utils.DialogUtil;
import com.yehm.utils.Utils;

import java.util.Date;
import java.util.Random;

import static com.yehm.app.AppConfig.PAYLOAD_BUNDLE;


public abstract class BaseFragment extends Fragment implements MvpView, IPickResult {
    // Toolbar toolbar;
    protected static final int ASK_SEND_SMS_PERMISSION_REQUEST_CODE = 14;
    private static final String TAG = "BaseFragment";
    protected final Gson gson = new Gson();
    public Activity context;
    public ApiServices apiServices;
    public ServiceGenerator serviceGenerator;
    //protected Entity mEntity;
    protected String latitude = "0", longitude = "0", lastActiveTime;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        serviceGenerator = ServiceGenerator.getInstance();
        apiServices = ServiceGenerator.createService(ApiServices.class);
    }

    public void showToastS(String text) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) context.findViewById(R.id.toast_layout_root));
        TextView text1 = (TextView) layout.findViewById(R.id.text);
        text1.setText(text);
        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 170);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void getClickPosition(int position, String tag) {

    }

    @Override
    public void openActivityOnTokenExpire() {

    }

    @Override
    public void onError(int resId) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void getDateDetails(Date date) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PreferencesManager.initializeInstance(getContext());
        onViewCreatedStuff(view, savedInstanceState);
    }

    public void showAlert(String msg, int color, int icon) {
        Alerter.create(context)
                .setText(msg)
                .setTextAppearance(R.style.alertTextColor)
                .setBackgroundColorRes(color)
                .setIcon(icon)
                .show();
    }

    public abstract void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState);

    public String generatePin() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    public void showLoading() {
        //hideLoading();
        mProgressDialog = DialogUtil.showLoadingDialog(getActivity(), TAG);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void createInfoDialog(Context context, String title,
                                 String msg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "OK",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void logoutDialog(Context context, Class activity) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Logout");
        builder1.setMessage("Do you really want to logout?");
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Yes",
                (dialog, id) -> {
                    PreferencesManager.getInstance(context).clear();
                    goToActivityWithFinish(activity, null);
                    dialog.cancel();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void goToActivity(Class<?> classActivity, Bundle bundle) {
        Utils.hideSoftKeyboard(getActivity());
        Intent intent = new Intent(getContext(), classActivity);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        getActivity().startActivity(intent);
    }

    public void goToActivityWithFinish(Class<?> classActivity, Bundle bundle) {
        Intent intent = new Intent(getContext(), classActivity);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        Utils.hideSoftKeyboard(getActivity());
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    public void checkSMSPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            requestSMSPermission();
    }

    public void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_SMS)
                || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECEIVE_SMS)) {
            Utils.createSimpleDialog1(getActivity(), getString(R.string.alert_text), getString(R.string.permission_camera_rationale11), getString(R.string.reqst_permission), new Utils.Method() {
                @Override
                public void execute() {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.SEND_SMS,
                                    Manifest.permission.READ_SMS,
                                    Manifest.permission.RECEIVE_SMS},
                            ASK_SEND_SMS_PERMISSION_REQUEST_CODE);
                }
            });
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_SMS
                    },
                    ASK_SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPickResult(PickResult pickResult) {
    }
}
