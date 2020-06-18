package com.yehm.constants;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tapadoo.alerter.Alerter;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.retrofit.ApiServices;
import com.yehm.retrofit.MvpView;
import com.yehm.retrofit.ServiceGenerator;
import com.yehm.utils.DialogUtil;
import com.yehm.utils.NetworkConnectionChecker;
import com.yehm.utils.Utils;

import java.util.Date;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.yehm.app.AppConfig.PAYLOAD_BUNDLE;


public abstract class BaseActivity extends AppCompatActivity implements NetworkConnectionChecker.OnConnectivityChangedListener, View.OnClickListener, MvpView {
    protected static final int PHONE_STATE_PERMISSION_REQUEST_CODE = 12;
    protected static final int PERMISSION_REQUEST_AUDIO_VIDEO = 15;
    protected static final int STORAGE_PERMISSION_REQUEST_CODE = 16;
    private static final String TAG = "BaseActivity";
    public ApiServices apiServices;
    public ServiceGenerator serviceGenerator;
    public Activity context;
    public boolean isConnected;
    private ProgressDialog mProgressDialog;

    public static void goToActivity(Activity activity, Class<?> classActivity, Bundle bundle) {
        Utils.hideSoftKeyboard(activity);
        Intent intent = new Intent(activity, classActivity);
        if (bundle != null)
            intent.putExtra("android.intent.extra.INTENT", bundle);
        activity.startActivity(intent);
    }

    public static void finishActivity(Activity activity) {
        Utils.hideSoftKeyboard(activity);
        activity.finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        serviceGenerator = ServiceGenerator.getInstance();
        apiServices = ServiceGenerator.createService(ApiServices.class);

        new DialogUtil(BaseActivity.this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                getWindow().setStatusBarColor( Color.TRANSPARENT );
//                getWindow().getDecorView()
//                        .setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
//            } else {
//                getWindow().setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
//            }
//        }
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

    public void goToActivityWithFinish(Class<?> classActivity, Bundle bundle) {
        Intent intent = new Intent(context, classActivity);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        Utils.hideSoftKeyboard(context);
        context.startActivity(intent);
        context.finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));

        snackbar.show();
    }

    public void showToastS(String text) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text1 = (TextView) layout.findViewById(R.id.text);
        text1.setText(text);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 170);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading() {
        mProgressDialog = DialogUtil.showLoadingDialog(BaseActivity.this, "Base Activity");
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return isConnected;
    }

    public void showAlert(String msg, int color, int icon) {
        Alerter.create(context)
                .setText(msg)
                .setTextAppearance(R.style.alertTextColor)
                .setBackgroundColorRes(color)
                .setIcon(icon)
                .show();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void getDateDetails(Date date) {

    }

    @Override
    public void getClickPosition(int position, String tag) {

    }

    @Override
    public void connectivityChanged(boolean availableNow) {
        isConnected = availableNow;
    }

    public void createInfoDialog(Context context, String title, String msg) {

        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);


        builder1.setNegativeButton(
                "OK",
                (dialog, id) -> {
                    dialog.cancel();
                    finish();
                });

        androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public String generatePin() {
        Random random = new Random();
        @SuppressLint("DefaultLocale") String randomPIN = String.format("%04d", random.nextInt(10000));

        return randomPIN;
    }

    public void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            IBinder binder = activity.getCurrentFocus().getWindowToken();
            if (binder != null) {
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(binder, 0);
                }
            }
        }
    }

    public void goToActivityWithFinish(Activity activity, Class<?> classActivity, Bundle bundle) {
        Intent intent = new Intent(context, classActivity);
        if (bundle != null)
            intent.putExtra("android.intent.extra.INTENT", bundle);
        Utils.hideSoftKeyboard(activity);
        activity.startActivity(intent);
        activity.finish();
    }

    public void goToActivityOtherModuleWithFinish(Activity activity, String classActivity, String packageStr, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        if (bundle != null)
            intent.putExtra(PAYLOAD_BUNDLE, bundle);
        intent.setComponent(new ComponentName(packageStr, classActivity));
        Utils.hideSoftKeyboard(activity);
        activity.startActivity(intent);
        activity.finish();

    }

    public void hasAudioVideoStateContactPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            requestAudioVideoStateContactPermission();
    }

    public void requestAudioVideoStateContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Utils.createSimpleDialog1(context, getString(R.string.alert_text), getString(R.string.permission_camera_rationale11), getString(R.string.reqst_permission), new Utils.Method() {
                @Override
                public void execute() {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, PERMISSION_REQUEST_AUDIO_VIDEO);
                }
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_AUDIO_VIDEO);
        }
    }

    public void hasStoragePermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        )
            requestStoragePermission();
    }

    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utils.createSimpleDialog1(context, getString(R.string.alert_text), getString(R.string.permission_camera_rationale11), getString(R.string.reqst_permission), new Utils.Method() {
                @Override
                public void execute() {
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_REQUEST_CODE);
                }
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
    }


}