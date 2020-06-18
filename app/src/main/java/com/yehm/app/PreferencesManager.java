package com.yehm.app;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesManager {

    //app login variables
    private static final String PREF_NAME = "com.yehm";
    private static final String USERID = "user_id";
    private static final String USERTYPE = "user_type";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MOBILE = "mobile";
    private static final String PROFILEPIC = "pic";
    private static final String MEMEBERID = "mem_id";
    private static final String INCOMEPLANID = "income_planid";
    private static final String DOJ = "doj";
    private static final String DOA = "doa";

    private static final String SPONSORID = "sponsor_id";
    private static final String SPONSORNAME = "sponsor_name";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //for fragment
    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    //for getting instance
    public static synchronized PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public boolean clear() {
        return mPref.edit().clear().commit();
    }

    public String getDoa() {
        return mPref.getString(DOA, "");
    }

    public void setDoa(String value) {
        mPref.edit().putString(DOA, value).apply();
    }

    public String getDoj() {
        return mPref.getString(DOJ, "");
    }

    public void setDoj(String value) {
        mPref.edit().putString(DOJ, value).apply();
    }

    public String getUSERID() {
        return mPref.getString(USERID, "");
    }

    public void setUSERID(String value) {
        mPref.edit().putString(USERID, value).apply();
    }

    public String getMEMBERID() {
        return mPref.getString(MEMEBERID, "");
    }

    public void setMEMBERID(String value) {
        mPref.edit().putString(MEMEBERID, value).apply();
    }

    public String getUSERTYPE() {
        return mPref.getString(USERTYPE, "");
    }

    public void setUSERTYPE(String value) {
        mPref.edit().putString(USERTYPE, value).apply();
    }

    public String getNAME() {
        return mPref.getString(NAME, "");
    }

    public void setNAME(String value) {
        mPref.edit().putString(NAME, value).apply();
    }

    public String getEMAIL() {
        return mPref.getString(EMAIL, "");
    }

    public void setEMAIL(String value) {
        mPref.edit().putString(EMAIL, value).apply();
    }

    public String getMOBILE() {
        return mPref.getString(MOBILE, "");
    }

    public void setMOBILE(String value) {
        mPref.edit().putString(MOBILE, value).apply();
    }

    public String getPROFILEPIC() {
        return mPref.getString(PROFILEPIC, "");
    }

    public void setPROFILEPIC(String value) {
        mPref.edit().putString(PROFILEPIC, value).apply();
    }

    public String getIncomePlanId() {
        return mPref.getString(INCOMEPLANID, "");
    }

    public void setIncomePlanId(String value) {
        mPref.edit().putString(INCOMEPLANID, value).apply();
    }

    public String getSponsorid() {
        return mPref.getString(SPONSORID, "");
    }

    public void setSponsorid(String value) {
        mPref.edit().putString(SPONSORID, value).apply();
    }

    public String getSponsorname() {
        return mPref.getString(SPONSORNAME, "");
    }

    public void setSponsorname(String value) {
        mPref.edit().putString(SPONSORNAME, value).apply();
    }

}
