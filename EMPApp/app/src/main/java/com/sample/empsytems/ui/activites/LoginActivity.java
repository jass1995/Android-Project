package com.sample.empsytems.ui.activites;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.sample.empsytems.R;
import com.sample.empsytems.database.DatabaseHelper;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.models.signup.User;
import com.sample.empsytems.ui.interfaces.onAlertCallbackListener;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.PrefsManager;
import com.sample.empsytems.utils.Utility;

import java.sql.SQLException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    CheckBox cbRememberMe;
    TextView tvSignUp;
    Button btLogin;

    private PrefsManager prefsManager;
    private DatabaseHelper databaseHelper = null;
    private Dao<User, Integer> userDao;

    String strEmail, strPassword;
    String strErrorMessage;
    boolean isEnterDefUser;

    private View.OnClickListener mLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            attemptLogin();
        }
    };

    private View.OnClickListener mSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CommonMethods.startActivity(LoginActivity.this,
                    SignupActivity.class,
                    false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        //Handle click events
        btLogin.setOnClickListener(mLoginListener);
        tvSignUp.setOnClickListener(mSignUpListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(LoginActivity.this
                    , DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public boolean isValid() {
        if (strEmail.length() == 0 || strPassword.length() == 0) {
            strErrorMessage = getResources().getString(R.string.error_email_pass_required);
            return false;
        } else if (!CommonMethods.isEmailValid(strEmail)) {
            strErrorMessage = getResources().getString(R.string.error_invalid_email);
            return false;
        }
        return true;
    }

    private void attemptLogin() {
        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();

        if (isValid()) {
            checkUserIfExist();
        } else {
            CommonMethods.showAlertMessage(LoginActivity.this, strErrorMessage);
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btLogin = findViewById(R.id.btLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        //Pre-filled data
        etEmail.setText("user@employee.com");
        etPassword.setText("s3cr3t");
        etEmail.setSelection(etEmail.length());

        prefsManager = new PrefsManager(LoginActivity.this);
        isEnterDefUser = prefsManager.loadPrefBoolValue(
                PrefsManager.KEY_ENTER_DEFAULT_USER,
                false);

        if (!isEnterDefUser) {
            //Saving a default static type user in DB.
            saveUserInDatabase();
        }
    }

    private void checkUserIfExist() {
        try {
            userDao = getHelper().getUserDao();
            User mUser = userDao.queryBuilder()
                    .where()
                    .eq("email", strEmail)
                    .and()
                    .eq("password", strPassword)
                    .queryForFirst();

            if (mUser != null) {

                if (cbRememberMe.isChecked()) {
                    PrefsManager prefsManager = new PrefsManager(LoginActivity.this);
                    prefsManager.savePreferenceBoolValue(PrefsManager.KEY_LOGIN_SESSION, true);
                }

                prefsManager.savePreferenceStringValue(PrefsManager.KEY_EMAIL, mUser.getEmail());
                prefsManager.savePreferenceStringValue(PrefsManager.KEY_USERNAME, mUser.getUserName());

                CommonMethods.showAlertMessageCallback(LoginActivity.this
                        , "Congratulations!!! Login successfully."
                        , new onAlertCallbackListener() {
                            @Override
                            public void onClickOkay() {
                                CommonMethods.startActivity(LoginActivity.this,
                                        HomeActivity.class,
                                        false);
                            }
                        });
            }else{
                CommonMethods.showAlertMessageCallback(LoginActivity.this
                        , getString(R.string.error_invalid_credentials)
                        , new onAlertCallbackListener() {
                            @Override
                            public void onClickOkay() {
                            }
                        });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveUserInDatabase() {
        try {
            User userInstance = new User("TestUser",
                    etEmail.getText().toString().trim(),
                    etPassword.getText().toString().trim());
            final Dao<User, Integer> userDao = getHelper().getUserDao();
            int resultCode = userDao.create(userInstance);

            if(resultCode == 1) {
                prefsManager.savePreferenceBoolValue(
                        PrefsManager.KEY_ENTER_DEFAULT_USER,
                        true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
