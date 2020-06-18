package com.yehm.activity;

import android.os.Bundle;
import android.os.Handler;

import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            int SPLASH_TIME_OUT = 3000;

            new Handler().postDelayed(() -> {
                if (!PreferencesManager.getInstance(context).getUSERID().equals("")) {
                    goToActivityWithFinish(context, MainContainer.class, null);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    goToActivityWithFinish(SplashActivity.this, MainContainer.class, null);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }, SPLASH_TIME_OUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

