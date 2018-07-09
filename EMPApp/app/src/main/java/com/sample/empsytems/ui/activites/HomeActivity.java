package com.sample.empsytems.ui.activites;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sample.empsytems.R;
import com.sample.empsytems.database.DatabaseHelper;
import com.sample.empsytems.ui.fragments.AddEmpPayrollFragment;
import com.sample.empsytems.ui.fragments.HomeFragment;
import com.sample.empsytems.ui.fragments.ListPayrollFragment;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.PrefsManager;

public class HomeActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView tvUserName, tvUserEmail;
    private Toolbar toolbar;

    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    private static final String TAG_ADD_EMP = "add_emp";
    private static final String TAG_VIEW_ALL_EMP = "view_all_emp";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_HELP = "help";
    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private PrefsManager prefsManager;
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefsManager = new PrefsManager(HomeActivity.this);
        init();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNavHeader();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
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
            databaseHelper = OpenHelperManager.getHelper(HomeActivity.this
                    , DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @SuppressLint("SetTextI18n")
    private void loadNavHeader() {
        tvUserName.setText(prefsManager.loadPreferenceStringValue(PrefsManager.KEY_USERNAME
                , "UserXXXXX"));
        tvUserEmail.setText(prefsManager.loadPreferenceStringValue(PrefsManager.KEY_EMAIL
                , "user@employee.com"));
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View navHeader = navigationView.getHeaderView(0);
        tvUserName = navHeader.findViewById(R.id.tvUserName);
        tvUserEmail = navHeader.findViewById(R.id.tvUserEmail);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        setUpNavigationView();
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new AddEmpPayrollFragment();
            case 2:
                return new ListPayrollFragment();
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_add_emp:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ADD_EMP;
                        break;
                    case R.id.nav_list_payroll:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_VIEW_ALL_EMP;
                        break;
                    case R.id.nav_profile:
                        /*navItemIndex = 3;
                        CURRENT_TAG = TAG_PROFILE;*/
                        CommonMethods.startActivity(HomeActivity.this,
                                ProfileActivity.class,
                                false);
                        break;
                    case R.id.nav_help:
                        //navItemIndex = 4;
                        //CURRENT_TAG = TAG_HELP;
                        showHelpAlertDialog();
                        break;
                    case R.id.nav_logout:
                        performLogout();
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void showHelpAlertDialog() {
        final Dialog openDialog = new Dialog(HomeActivity.this);
        openDialog.setContentView(R.layout.alert_help_dialog);

        TextView tvThanksBtn = openDialog.findViewById(R.id.tvThanksBtn);

        tvThanksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHomeFragment();
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }

    public void loadNavItemIndexPos(int navItemPos) {
        switch (navItemPos) {
            case 1:
                navItemIndex = 1;
                CURRENT_TAG = TAG_ADD_EMP;
                break;

            case 2:
                navItemIndex = 2;
                CURRENT_TAG = TAG_VIEW_ALL_EMP;
                break;

            case 3:
                CommonMethods.startActivity(HomeActivity.this,
                        ProfileActivity.class,
                        false);
                break;
        }
        loadHomeFragment();
    }

    public void performLogout() {
        getHelper().clearDatabase();
        new PrefsManager(HomeActivity.this).clearAutoPreference();
        CommonMethods.startActivity(HomeActivity.this,
                LoginActivity.class,
                true);
    }
}