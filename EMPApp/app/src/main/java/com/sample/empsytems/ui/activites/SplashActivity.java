package com.sample.empsytems.ui.activites;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sample.empsytems.R;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.PrefsManager;

public class SplashActivity extends AppCompatActivity {

    boolean isLoginSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        isLoginSession = new PrefsManager(SplashActivity.this)
                .loadPrefBoolValue(PrefsManager.KEY_LOGIN_SESSION, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoginSession) {
                    CommonMethods.startActivity(SplashActivity.this,
                            HomeActivity.class,
                            true);
                } else {
                    CommonMethods.startActivity(SplashActivity.this,
                            LoginActivity.class,
                            true);
                }
            }
        }, 2000);
    }
}
