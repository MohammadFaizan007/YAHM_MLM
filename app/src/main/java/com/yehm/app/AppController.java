package com.yehm.app;

import android.app.Application;

import com.yehm.R;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@ReportsCrashes(formKey = "",
        mailTo = "sheeran@quaeretech.com",
        mode = ReportingInteractionMode.SILENT,
        resToastText = R.string.crash_toast_text)

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .disableCustomViewInflation()
                .setFontAttrId(R.attr.fontPath)
                .disableCustomViewInflation()
                .build());
    }
}
