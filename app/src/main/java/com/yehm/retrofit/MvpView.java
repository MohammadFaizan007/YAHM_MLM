package com.yehm.retrofit;

import androidx.annotation.StringRes;

import java.util.Date;

/**
 * Created by Vivek on 1/8/18.
 */

public interface MvpView {
    void showLoading();

    void hideLoading();

    void openActivityOnTokenExpire();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

    void getDateDetails(Date date);

    void getClickPosition(int position, String tag);

}
