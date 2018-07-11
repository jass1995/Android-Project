package com.sample.empsytems.ui.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.sample.empsytems.R;
import com.sample.empsytems.database.DatabaseHelper;
import com.sample.empsytems.models.Employee;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.models.Vehicle;
import com.sample.empsytems.ui.interfaces.onAlertCallbackListener;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.Constants;

import java.sql.SQLException;
import java.util.List;

public class ViewEmpDetailActivity extends AppCompatActivity {

    Toolbar tbEmpDetails;

    TextView tvEmpName;
    TextView tvEmpDOB;

    TextView tvNoVehicle;
    LinearLayout llVehicleInfo;
    TextView tvVehicleType, tvVehicleName, tvVehicleModel, tvVehiclePNo;

    TextView tvEmpCat;

    LinearLayout llPartTime;
    TextView tvHourRate, tvTotalHours, tvWorkType, tvWorkCatLabel, tvExtraAmt, tvPartTimeTEarning;

    LinearLayout llIntern;
    TextView tvSchoolName;

    LinearLayout llFullTime;
    TextView tvEmpSalary, tvEmpBonus, tvTotalEarnings;

    private DatabaseHelper databaseHelper = null;
    private Dao<EmployeePayroll, Integer> employeePayrollDao;

    private List<EmployeePayroll> employeePayrollList;

    String strName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_emp_detail);

        if (getIntent().getExtras() != null) {
            strName = getIntent().getExtras().getString("key_name");
            init();
            loadFromDB();
        } else {
            CommonMethods.showAlertMessageCallback(ViewEmpDetailActivity.this,
                    "Unable to load data. Please try again later.",
                    new onAlertCallbackListener() {
                        @Override
                        public void onClickOkay() {
                            onBackPressed();
                        }
                    });
        }
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
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(
                    ViewEmpDetailActivity.this
                    , DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void loadFromDB() {
        try {
            employeePayrollDao = getHelper().getEmpPayrollDao();
            employeePayrollList = employeePayrollDao.queryBuilder()
                    .where()
                    .eq("emp_name", strName)
                    .query();

            refreshData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshData() {
        if (employeePayrollList.size() >= 1) {
            EmployeePayroll employeePayroll = employeePayrollList.get(0);
            tvEmpName.setText(employeePayroll.empName);
            tvEmpDOB.setText(employeePayroll.empDOB);

            updateVehicleInfo(employeePayroll);
            updateEmployeeInfo(employeePayroll);
        }
    }

    private void updateVehicleInfo(EmployeePayroll employeePayroll) {
        if (employeePayroll.havingVehicle) {
            llVehicleInfo.setVisibility(View.VISIBLE);
            tvNoVehicle.setVisibility(View.GONE);

            Vehicle vehicle = employeePayroll.getVehicle();
            int vType = vehicle.getVehicleType();
            if (vType == Constants.TAG_VEHICLE_CAR) {
                tvVehicleType.setText(getString(R.string.rb_label_car));
            } else {
                tvVehicleType.setText(getString(R.string.rb_label_bike));
            }

            tvVehicleName.setText(vehicle.vehicleName);
            tvVehicleModel.setText(vehicle.vehicleModel);
            tvVehiclePNo.setText(vehicle.vehiclePlateNo);
        } else {
            tvNoVehicle.setVisibility(View.VISIBLE);
            llVehicleInfo.setVisibility(View.GONE);
        }
    }

    private void updateEmployeeInfo(EmployeePayroll employeePayroll) {
        Employee employee = employeePayroll.getEmployee();
        int eType = employee.getEmployeeType();

        switch (eType) {
            case Constants.EMP_TYPE_PART_TIME:
                llPartTime.setVisibility(View.VISIBLE);
                tvEmpCat.setText(getString(R.string.rb_label_part_time));
                float hourRate = Float.parseFloat(employee.empHourRate);
                int totalHours = Integer.parseInt(employee.empWorkedHours);
                int iWorkType = employee.empWorkType;
                float extraAmt = Float.parseFloat(employee.empExtraAmt);
                float fTotalEarnings = ((hourRate * totalHours) + extraAmt);

                tvHourRate.setText((Constants.CURRENCY_SYMBOL + hourRate));
                tvTotalHours.setText(employee.empWorkedHours);

                if (iWorkType == Constants.EMP_TYPE_COMMISSION) {
                    tvWorkType.setText(getString(R.string.rb_label_commission_base));
                    tvWorkCatLabel.setText(getString(R.string.text_label_comm_rate));
                } else {
                    tvWorkType.setText(getString(R.string.rb_label_fixed_base));
                    tvWorkCatLabel.setText(getString(R.string.text_label_fixed_rate));
                }

                tvExtraAmt.setText((Constants.CURRENCY_SYMBOL + extraAmt));
                tvPartTimeTEarning.setText((Constants.CURRENCY_SYMBOL + fTotalEarnings));
                break;

            case Constants.EMP_TYPE_INTERN:
                llIntern.setVisibility(View.VISIBLE);
                tvEmpCat.setText(getString(R.string.rb_label_intern));
                tvSchoolName.setText(employee.empSchoolName);
                break;

            case Constants.EMP_TYPE_FULL_TIME:
                llFullTime.setVisibility(View.VISIBLE);
                float salary = Float.parseFloat(employee.empSalary);
                float bonus = Float.parseFloat(employee.empBonus);
                tvEmpCat.setText(getString(R.string.rb_label_full_time));
                tvEmpSalary.setText((Constants.CURRENCY_SYMBOL + salary));
                tvEmpBonus.setText((Constants.CURRENCY_SYMBOL + bonus));
                tvTotalEarnings.setText((Constants.CURRENCY_SYMBOL + (salary + bonus)));
                break;
        }
    }

    private void init() {
        tbEmpDetails = findViewById(R.id.tbEmpDetails);
        tvEmpName = findViewById(R.id.tvEmpName);
        tvEmpDOB = findViewById(R.id.tvEmpDOB);

        tvNoVehicle = findViewById(R.id.tvNoVehicle);

        llVehicleInfo = findViewById(R.id.llVehicleInfo);
        tvVehicleType = findViewById(R.id.tvVehicleType);
        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvVehicleModel = findViewById(R.id.tvVehicleModel);
        tvVehiclePNo = findViewById(R.id.tvVehiclePNo);

        tvEmpCat = findViewById(R.id.tvEmpCat);

        llPartTime = findViewById(R.id.llPartTime);
        tvHourRate = findViewById(R.id.tvHourRate);
        tvTotalHours = findViewById(R.id.tvTotalHours);
        tvWorkType = findViewById(R.id.tvWorkType);
        tvWorkCatLabel = findViewById(R.id.tvWorkCatLabel);
        tvExtraAmt = findViewById(R.id.tvExtraAmt);
        tvPartTimeTEarning = findViewById(R.id.tvPartTimeTEarning);

        llIntern = findViewById(R.id.llIntern);
        tvSchoolName = findViewById(R.id.tvSchoolName);

        llFullTime = findViewById(R.id.llFullTime);
        tvEmpSalary = findViewById(R.id.tvEmpSalary);
        tvEmpBonus = findViewById(R.id.tvEmpBonus);
        tvTotalEarnings = findViewById(R.id.tvTotalEarnings);

        setSupportActionBar(tbEmpDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}