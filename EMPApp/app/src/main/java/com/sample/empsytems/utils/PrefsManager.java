package com.sample.empsytems.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {

    public static final String KEY_LOGIN_SESSION = "isLogin";
    public static final String KEY_ENTER_DEFAULT_USER = "enter_default_user";

    public static final String KEY_EMAIL = "login_email";
    public static final String KEY_USERNAME = "login_username";
    public static final String KEY_PASSWORD = "login_password";

    String mPrefsName = "EmpPayrollSystem";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public PrefsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(mPrefsName, Context.MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
    }

    /**
     * Load Preference any Boolean value
     */
    public boolean loadPrefBoolValue(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Save boolean value in Preference for future use.
     */
    @SuppressWarnings("static-access")
    public void savePreferenceBoolValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    /**
     * Load Preference any String value
     */
    public String loadPreferenceStringValue(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * Save String value in Preference for future use.
     */
    @SuppressWarnings("static-access")
    public void savePreferenceStringValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    /**
     * Reset shared-preference state.
     */
    public void clearAutoPreference() {
        mEditor.clear();
        mEditor.apply();
    }
}
