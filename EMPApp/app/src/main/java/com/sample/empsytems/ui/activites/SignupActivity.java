package com.sample.empsytems.ui.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sample.empsytems.R;
import com.sample.empsytems.utils.CommonMethods;

public class SignupActivity extends AppCompatActivity {

    Toolbar tbSignup;
    Button btSignup;
    private EditText etUsername, etEmail, etPassword, etCPassword;

    String strErrorMessae = "";
    String strUsername, strEmail, strPassword, strCPassword;

    private void registerUserInDB() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        btSignup.setOnClickListener(mSignuListener);
        etPassword.setOnEditorActionListener(mDoneActionListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener mSignuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            attempRegister();
        }
    };

    private TextView.OnEditorActionListener mDoneActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attempRegister();
                return true;
            }
            return false;
        }
    };

    private void attempRegister() {
        if (isValidate()) {
            registerUserInDB();
        } else {
            CommonMethods.showAlertMessage(SignupActivity.this
                    , strErrorMessae);
        }
    }

    public boolean isValidate() {
        strUsername = etUsername.getText().toString().trim();
        strEmail = etEmail.getText().toString().trim();
        strPassword = etPassword.getText().toString().trim();
        strCPassword = etCPassword.getText().toString().trim();

        if (strUsername.length() == 0 || strEmail.length() == 0
                || strPassword.length() == 0 || strCPassword.length() == 0) {
            strErrorMessae = getString(R.string.error_all_required);
            return false;
        } else if (!CommonMethods.isEmailValid(strEmail)) {
            strErrorMessae = getString(R.string.error_invalid_email);
            return false;
        } else if (!strPassword.equals(strCPassword)) {
            strErrorMessae = getString(R.string.error_invalid_password);
            return false;
        }
        return true;
    }

    private void init() {
        tbSignup = findViewById(R.id.tbSignup);
        btSignup = findViewById(R.id.btSignup);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);

        setSupportActionBar(tbSignup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
