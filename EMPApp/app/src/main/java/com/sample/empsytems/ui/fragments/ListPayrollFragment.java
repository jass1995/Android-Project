package com.sample.empsytems.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.sample.empsytems.R;
import com.sample.empsytems.database.DatabaseHelper;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.models.VehicleInfo;
import com.sample.empsytems.ui.adapters.AllPayrollListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPayrollFragment extends Fragment {

    private View mRoot;
    private LinearLayout llNoData;
    private RecyclerView rvAllPayrollList;
    private DatabaseHelper databaseHelper = null;
    private Dao<EmployeePayroll, Integer> employeePayrollDao;

    private List<EmployeePayroll> mAllPayrollList = new ArrayList<>();

    public ListPayrollFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_list_payroll, container, false);
        init();
        loadFromDB();

        return mRoot;
    }

    private void init() {
        rvAllPayrollList = mRoot.findViewById(R.id.rvAllPayrollList);
        llNoData = mRoot.findViewById(R.id.llNoData);
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
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void loadFromDB() {
        try {
            employeePayrollDao = getHelper().getEmpPayrollDao();
            mAllPayrollList.clear();

            // Query the database. We need all the records so, used queryForAll()
            mAllPayrollList = employeePayrollDao.queryForAll();
            initAdapter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        if (mAllPayrollList.size() == 0) {
            llNoData.setVisibility(View.VISIBLE);
            rvAllPayrollList.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.GONE);
            rvAllPayrollList.setVisibility(View.VISIBLE);

            rvAllPayrollList = mRoot.findViewById(R.id.rvAllPayrollList);
            Collections.reverse(mAllPayrollList);
            AllPayrollListAdapter mPayrollListAdapter = new AllPayrollListAdapter(getActivity(), mAllPayrollList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvAllPayrollList.setLayoutManager(mLayoutManager);
            rvAllPayrollList.setAdapter(mPayrollListAdapter);
        }
    }
}
