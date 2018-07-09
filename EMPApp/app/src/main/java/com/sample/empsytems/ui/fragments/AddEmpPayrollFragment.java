package com.sample.empsytems.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.sample.empsytems.R;
import com.sample.empsytems.database.DatabaseHelper;
import com.sample.empsytems.models.Employee;
import com.sample.empsytems.models.EmployeeFullTime;
import com.sample.empsytems.models.EmployeeIntern;
import com.sample.empsytems.models.EmployeePartTime;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.models.VehicleInfo;
import com.sample.empsytems.ui.activites.HomeActivity;
import com.sample.empsytems.ui.interfaces.onAlertCallbackListener;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.Utility;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEmpPayrollFragment extends Fragment {

    private View mRoot;
    private EditText etEmpName;
    private EditText etEmpDOB;
    private EditText /*etVehicleName,*/ etVehicleModel, etVehiclePlateNo;
    private EditText etEmpHourRate, etEmpWorkedHours;
    private EditText etEmpISchoolName;
    private EditText etEmpFTimeSalary, etEmpFTimeBonus;
    private Spinner spVehicleName;
    private EditText etExtraAmt;
    private CircleImageView ivVehicleImg;
    private CheckBox cbIsVehicle;
    private LinearLayout llVehicleInfo;
    private LinearLayout llPartTime, llIntern, llFullTime;
    private RadioGroup rdVehicleGroup;
    private RadioGroup rdEmpGroup, rdCommissionGroup;
    private Button btSavePayroll;
    private TextInputLayout textInputLayoutExtraAmt;

    private String strEmpName, strEmpDOB;
    private String strVehicleName, strVehicleModel, strVehiclePlateNo;
    private String strEmpHourRate, strEmpWorkedHours, strExtraAmt;
    private String strSchoolName;
    private String strEmpFTimeSalary, strEmpFTimeBonus;
    private int iVehicleType = Utility.TAG_VEHICLE_CAR; //1 for car and 2 for MotorBike
    private int iVehicleImage = 0; //Vehicle Image position
    private int iEmployeeType = Utility.EMP_TYPE_PART_TIME; //By Default Part Time
    //By Default Commission type in part time.
    private int iCommissionOrFixed = Utility.EMP_TYPE_COMMISSION;
    private boolean isVehicleAvail;

    List<String> mVehicleCategoriesList = new ArrayList<String>();

    Calendar mCalendarInstance;
    private DatabaseHelper databaseHelper = null;

    public AddEmpPayrollFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_add_emp_payroll, container, false);
        init();

        //Handle click events.
        etEmpDOB.setOnClickListener(mDobListener);
        cbIsVehicle.setOnCheckedChangeListener(mVehicleChangeListener);
        rdVehicleGroup.setOnCheckedChangeListener(mVehicleTypeChangeListener);//Car or MotorBike
        rdEmpGroup.setOnCheckedChangeListener(mEmpInfoChangeListener);
        rdCommissionGroup.setOnCheckedChangeListener(mEmpPartTimeExtraRateListener);
        spVehicleName.setOnItemSelectedListener(mVehicleNameSelectedListener);
        btSavePayroll.setOnClickListener(mAddNewPayrollListener);
        return mRoot;
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private View.OnClickListener mDobListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            new DatePickerDialog(getActivity(), date, /*mCalendarInstance
                    .get(Calendar.YEAR)*/2002, mCalendarInstance.get(Calendar.MONTH),
                    mCalendarInstance.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendarInstance.set(Calendar.YEAR, year);
            mCalendarInstance.set(Calendar.MONTH, monthOfYear);
            mCalendarInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private CompoundButton.OnCheckedChangeListener mVehicleChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                updateVehicleIcon(Utility.TAG_VEHICLE_CAR, 0);
                llVehicleInfo.setVisibility(View.VISIBLE);
            } else {
                updateVehicleIcon(Utility.TAG_VEHICLE_MOTORBIKE, 0);
                llVehicleInfo.setVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener mAddNewPayrollListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addNewPayRoll();
        }
    };

    private RadioGroup.OnCheckedChangeListener mEmpInfoChangeListener = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rbPartTime:
                    llPartTime.setVisibility(View.VISIBLE);
                    llIntern.setVisibility(View.GONE);
                    llFullTime.setVisibility(View.GONE);
                    iEmployeeType = Utility.EMP_TYPE_PART_TIME;
                    break;

                case R.id.rbIntern:
                    llPartTime.setVisibility(View.GONE);
                    llIntern.setVisibility(View.VISIBLE);
                    llFullTime.setVisibility(View.GONE);
                    iEmployeeType = Utility.EMP_TYPE_INTERN;
                    break;

                case R.id.rbFullTime:
                    llPartTime.setVisibility(View.GONE);
                    llIntern.setVisibility(View.GONE);
                    llFullTime.setVisibility(View.VISIBLE);
                    iEmployeeType = Utility.EMP_TYPE_FULL_TIME;
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener mEmpPartTimeExtraRateListener = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rbCommissionBased:
                    iCommissionOrFixed = Utility.EMP_TYPE_COMMISSION;
                    textInputLayoutExtraAmt.setHint(getString(R.string.edit_hint_commission_amt));
                    break;

                case R.id.rbFixedBased:
                    iCommissionOrFixed = Utility.EMP_TYPE_FIXED;
                    textInputLayoutExtraAmt.setHint(getString(R.string.edit_hint_fixed_amt));
                    break;
            }
        }
    };

    private AdapterView.OnItemSelectedListener mVehicleNameSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            strVehicleName = parent.getItemAtPosition(position).toString();
            updateVehicleIcon(iVehicleType, position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void updateVehicleIcon(int iVehicleType, int position) {
        iVehicleImage = position;
        this.iVehicleType = iVehicleType;
        if (iVehicleType == Utility.TAG_VEHICLE_CAR) {
            switch (position) {
                case 0:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_audi);
                    break;

                case 1:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_bmw);
                    break;

                case 2:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_bently);
                    break;

                case 3:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_ferrari);
                    break;

                case 4:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_ford);
                    break;

                case 5:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_ktm);
                    break;

                case 6:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_lambo);
                    break;

                case 7:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_mercedes);
                    break;

                case 8:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_mini);
                    break;

                default:
                    ivVehicleImg.setImageResource(R.drawable.ic_car_audi);
                    break;
            }
        } else if (iVehicleType == Utility.TAG_VEHICLE_MOTORBIKE) {
            switch (position) {
                case 0:
                    ivVehicleImg.setImageResource(R.drawable.ic_bike_honda);
                    break;
                case 1:
                    ivVehicleImg.setImageResource(R.drawable.ic_bike_harley);
                    break;
                default:
                    ivVehicleImg.setImageResource(R.drawable.ic_bike_honda);
                    break;
            }
        }
    }

    private RadioGroup.OnCheckedChangeListener mVehicleTypeChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int selectedId = rdVehicleGroup.getCheckedRadioButtonId();
            switch (selectedId) {
                case R.id.rbCar:
                    iVehicleType = Utility.TAG_VEHICLE_CAR;
                    loadCarBrands();
                    break;
                case R.id.rbMotorCycle:
                    iVehicleType = Utility.TAG_VEHICLE_MOTORBIKE;
                    loadMotorBikeBrands();
                    break;
            }
        }
    };

    private void addNewPayRoll() {
        strEmpName = etEmpName.getText().toString().trim();
        strEmpDOB = etEmpDOB.getText().toString();

        Log.e("Name", strEmpName);
        Log.e("DOB", strEmpDOB);
        Log.e("isVehicleAvail", ": " + isVehicleAvail);

        isVehicleAvail = cbIsVehicle.isChecked();
        // Check if vehicle avail, then collect values.
        if (isVehicleAvail) {
            strVehicleModel = etVehicleModel.getText().toString();
            strVehiclePlateNo = etVehiclePlateNo.getText().toString();

            Log.e("iVehicleType", "(1 for Car and 2 for Motor Bike): " + iVehicleType);
            Log.e("VehicleName", strVehicleName);
            Log.e("MODEL", strVehicleModel);
            Log.e("Plate No", strVehiclePlateNo);
        }

        Log.e("Emp Type", "1. Part Time 2. Intern 3. Full Time " + iEmployeeType);
        //Store EMPLOYEE TYPE i.e. iEmployeeType
        switch (iEmployeeType) {
            case Utility.EMP_TYPE_PART_TIME:
                strEmpHourRate = etEmpHourRate.getText().toString();
                strEmpWorkedHours = etEmpWorkedHours.getText().toString();
                strExtraAmt = etExtraAmt.getText().toString();
                Log.e("Hour Rate", strEmpHourRate);
                Log.e("Worked Hours", strEmpWorkedHours);
                Log.e("Comm Or Fixed", "1. Commission 2 . Fixed :" + iCommissionOrFixed);
                Log.e("Extra Amt.", strExtraAmt);
                break;

            case Utility.EMP_TYPE_INTERN:
                strSchoolName = etEmpISchoolName.getText().toString().trim();
                Log.e("School Name", strSchoolName);
                break;

            case Utility.EMP_TYPE_FULL_TIME:
                strEmpFTimeSalary = etEmpFTimeSalary.getText().toString().trim();
                strEmpFTimeBonus = etEmpFTimeBonus.getText().toString().trim();
                Log.e("Salary", strEmpFTimeSalary);
                Log.e("Bonus", strEmpFTimeBonus);
                break;
        }

        String strErrorMessage = "";
        //Add Payroll Validations.
        if (strEmpName.length() == 0) {
            strErrorMessage = getString(R.string.error_empty_ename);
        } else if (strEmpDOB.length() == 0) {
            strErrorMessage = getString(R.string.error_empty_edob);
        } else if (isVehicleAvail) {
            if (strVehicleModel.length() == 0) {
                strErrorMessage = getString(R.string.error_empty_vmodel);
            } else if (strVehiclePlateNo.length() == 0) {
                strErrorMessage = getString(R.string.error_empty_vplateno);
            }
        }

        if (strErrorMessage.length() > 1) {
            CommonMethods.showAlertMessage(getActivity(), strErrorMessage);
        } else {
            savePayrollInDB();
        }
    }

    private void savePayrollInDB() {
        final EmployeePayroll employeePayrollInstance = new EmployeePayroll();
        VehicleInfo vehicleInfo = new VehicleInfo();
        Employee employee = new Employee();

        try {
            vehicleInfo.vehicleType = iVehicleType;
            vehicleInfo.vehicleImage = iVehicleImage;
            vehicleInfo.vehicleName = strVehicleName;
            vehicleInfo.vehicleModel = strVehicleModel;
            vehicleInfo.vehiclePlateNo = strVehiclePlateNo;
            final Dao<VehicleInfo, Integer> vehicleInfos = getHelper().getVehicleInfoDao();
            vehicleInfos.create(vehicleInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (iEmployeeType) {
            case Utility.EMP_TYPE_PART_TIME:
                strEmpHourRate = etEmpHourRate.getText().toString();
                strEmpWorkedHours = etEmpWorkedHours.getText().toString();
                strExtraAmt = etExtraAmt.getText().toString();
                Log.e("Hour Rate", strEmpHourRate);
                Log.e("Worked Hours", strEmpWorkedHours);
                Log.e("Comm Or Fixed", "1. Commission 2 . Fixed :" + iCommissionOrFixed);
                Log.e("Extra Amt.", strExtraAmt);
                break;

            case Utility.EMP_TYPE_INTERN:
                strSchoolName = etEmpISchoolName.getText().toString().trim();
                Log.e("School Name", strSchoolName);
                break;

            case Utility.EMP_TYPE_FULL_TIME:
                strEmpFTimeSalary = etEmpFTimeSalary.getText().toString().trim();
                strEmpFTimeBonus = etEmpFTimeBonus.getText().toString().trim();
                Log.e("Salary", strEmpFTimeSalary);
                Log.e("Bonus", strEmpFTimeBonus);
                break;
        }

        try {
           employee.employeeType = iEmployeeType;
            employee.empHourRate = strEmpHourRate;
            employee.empWorkedHours = strEmpWorkedHours;
            employee.empWorkType = iCommissionOrFixed;
            employee.empExtraAmt = strExtraAmt;
            employee.empSchoolName = strSchoolName;
            employee.empSalary = strEmpFTimeSalary;
            employee.empBonus = strEmpFTimeBonus;

            final Dao<Employee, Integer> employeeDao = getHelper().getEmployeeDao();
            employeeDao.create(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Then, set all the values from user input
        employeePayrollInstance.empName = strEmpName;
        employeePayrollInstance.empDOB = strEmpDOB;
        employeePayrollInstance.havingVehicle = isVehicleAvail;
        if (isVehicleAvail) {
            employeePayrollInstance.vehicleInfo = vehicleInfo;
        }
        employeePayrollInstance.employee = employee;

        try {
            final Dao<EmployeePayroll, Integer> addPayrollDao = getHelper().getEmpPayrollDao();
            addPayrollDao.create(employeePayrollInstance);
            CommonMethods.showAlertMessageCallback(getActivity()
                    , getString(R.string.success_save_payroll)
                    , new onAlertCallbackListener() {
                        @Override
                        public void onClickOkay() {
                            getActivity().onBackPressed();
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        etEmpName = mRoot.findViewById(R.id.etEmpName);
        etEmpDOB = mRoot.findViewById(R.id.etEmpDOB);

        cbIsVehicle = mRoot.findViewById(R.id.cbIsVehicle);
        llVehicleInfo = mRoot.findViewById(R.id.llVehicleInfo);
        rdVehicleGroup = mRoot.findViewById(R.id.rdVehicleGroup);
        spVehicleName = mRoot.findViewById(R.id.spVehicleName);
        //etVehicleName = mRoot.findViewById(R.id.etVehicleName);
        etVehicleModel = mRoot.findViewById(R.id.etVehicleModel);
        etVehiclePlateNo = mRoot.findViewById(R.id.etVehiclePlateNo);
        ivVehicleImg = mRoot.findViewById(R.id.ivVehicleImg);
        etEmpISchoolName = mRoot.findViewById(R.id.etEmpISchoolName);

        rdEmpGroup = mRoot.findViewById(R.id.rdEmpGroup);
        llPartTime = mRoot.findViewById(R.id.llPartTime);
        llIntern = mRoot.findViewById(R.id.llIntern);
        llFullTime = mRoot.findViewById(R.id.llFullTime);

        etEmpHourRate = mRoot.findViewById(R.id.etEmpHourRate);
        etEmpWorkedHours = mRoot.findViewById(R.id.etEmpWorkedHours);
        rdCommissionGroup = mRoot.findViewById(R.id.rdCommissionGroup);
        etExtraAmt = mRoot.findViewById(R.id.etExtraAmt);
        textInputLayoutExtraAmt = mRoot.findViewById(R.id.textInputLayoutExtraAmt);

        etEmpFTimeSalary = mRoot.findViewById(R.id.etEmpFTimeSalary);
        etEmpFTimeBonus = mRoot.findViewById(R.id.etEmpFTimeBonus);

        btSavePayroll = mRoot.findViewById(R.id.btSavePayroll);

        mCalendarInstance = Calendar.getInstance();

        loadCarBrands();
    }

    private void loadMotorBikeBrands() {
        mVehicleCategoriesList = new ArrayList<String>();
        mVehicleCategoriesList.add("Honda");
        mVehicleCategoriesList.add("Harley");

        refreshVehiclesSpinner();
    }

    private void loadCarBrands() {
        mVehicleCategoriesList = new ArrayList<String>();
        mVehicleCategoriesList.add("Audi");
        mVehicleCategoriesList.add("BMW");
        mVehicleCategoriesList.add("Bently");
        mVehicleCategoriesList.add("Ferrari");
        mVehicleCategoriesList.add("Ford");
        mVehicleCategoriesList.add("KTM");
        mVehicleCategoriesList.add("Lamborghini");
        mVehicleCategoriesList.add("Mercedes");
        mVehicleCategoriesList.add("Mini");

        refreshVehiclesSpinner();
    }

    private void refreshVehiclesSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mVehicleCategoriesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVehicleName.setAdapter(dataAdapter);
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etEmpDOB.setText(sdf.format(mCalendarInstance.getTime()));
    }
}