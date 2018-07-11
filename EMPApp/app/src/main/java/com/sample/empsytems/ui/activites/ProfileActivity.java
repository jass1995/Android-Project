package com.sample.empsytems.ui.activites;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.sample.empsytems.R;
import com.sample.empsytems.database.DatabaseHelper;
import com.sample.empsytems.models.signup.User;
import com.sample.empsytems.ui.interfaces.onAlertCallbackListener;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.PrefsManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText etUsername, etEmail, etPassword, etCPassword;
    private Button btUpdateProfile;

    String strErrorMessae = "";
    String strUsername, strEmail, strPassword, strCPassword;
    int loginID = -1;

    private GoogleMap mMap;
    private String strlocAddress = "Ottawa";
    private double llat = 45.4215;
    private double llong = 75.6972;

    SupportMapFragment mapFragment;
    LocationManager locationManager;
    private DatabaseHelper databaseHelper = null;
    private Dao<User, Integer> userDao;
    private User userInstance = null;

    PrefsManager prefsManager;

    private View.OnClickListener mUpdateProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isValidate()) {
                updateProfile();
            } else {
                CommonMethods.showAlertMessage(ProfileActivity.this, strErrorMessae);
            }
        }
    };

    public ProfileActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        prefsManager = new PrefsManager(ProfileActivity.this);
        init();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        enableGPS();

        btUpdateProfile.setOnClickListener(mUpdateProfileListener);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(ProfileActivity.this
                    , DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void updateProfile() {

        try {
            userDao = getHelper().getUserDao();
            User user = new User();
            user.setUserName(strUsername);
            user.setEmail(strEmail);
            user.setPassword(strPassword);


            UpdateBuilder<User, Integer> updateBuilder =
                    userDao.updateBuilder();

            updateBuilder.updateColumnValue("username", strUsername);
            updateBuilder.updateColumnValue("email", strEmail);
            updateBuilder.updateColumnValue("password", strPassword);

            updateBuilder.where().eq("user_id", loginID);
            updateBuilder.update();

            prefsManager.savePreferenceStringValue(PrefsManager.KEY_USERNAME, strUsername);
            prefsManager.savePreferenceStringValue(PrefsManager.KEY_EMAIL, strEmail);
            prefsManager.savePreferenceStringValue(PrefsManager.KEY_PASSWORD, strPassword);

            CommonMethods.showAlertMessage(ProfileActivity.this,
                    getString(R.string.alert_update_success));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        btUpdateProfile = findViewById(R.id.btUpdateProfile);

        getProfileInfo();
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

    private void enableGPS() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    llat = location.getLatitude();
                    llong = location.getLongitude();

                    try {
                        Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        strlocAddress = addresses.get(0).getAddressLine(0) + ", " +
                                addresses.get(0).getAddressLine(1) + ", "
                                + addresses.get(0).getAddressLine(2);
                    } catch (Exception e) {
                        Log.e("Exp", e.toString());
                    }

                    loadMap();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void loadMap() {
        if (mMap != null) {
            if (llat == 0 && llong == 0) {
                llat = 45.4215;
                llong = 75.6972;
                strlocAddress = "Ottawa";
            }
            LatLng location = new LatLng(llat, llong);
            mMap.addMarker(new MarkerOptions().position(location).title(strlocAddress));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        }
    }

    public void getProfileInfo() {
        try {
            strEmail = prefsManager.loadPreferenceStringValue(PrefsManager.KEY_EMAIL, "");
            userDao = getHelper().getUserDao();
            userInstance = userDao.queryBuilder()
                    .where()
                    .eq("email", strEmail)
                    .queryForFirst();

            if (userInstance != null) {
                loginID = userInstance.getId(); //Unique user ID.
                etUsername.setText(userInstance.getUserName());
                etEmail.setText(userInstance.getEmail());
                etPassword.setText(userInstance.getPassword());
                etCPassword.setText(userInstance.getPassword());
            } else {
                CommonMethods.showAlertMessageCallback(ProfileActivity.this
                        , getString(R.string.error_unable_to_load)
                        , new onAlertCallbackListener() {
                            @Override
                            public void onClickOkay() {
                                onBackPressed();
                            }
                        });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}