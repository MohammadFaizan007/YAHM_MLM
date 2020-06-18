package com.yehm.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.yehm.R;


public class DialogUtil {
    private static final String TAG = "DialogUtil";

    public static ProgressDialog progressDialog;

    public DialogUtil(Activity activity) {
        // This utility class is not publicly instantiable
//        progressDialog = new ProgressDialog(activity);
    }

    public static ProgressDialog showLoadingDialog(Activity activity, String callingPlace) {
        Log.d(TAG, "showLoadingDialog: " + callingPlace);
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(true);
        return progressDialog;
    }

    public static void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}