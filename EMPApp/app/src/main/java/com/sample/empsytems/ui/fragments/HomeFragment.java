package com.sample.empsytems.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sample.empsytems.R;
import com.sample.empsytems.ui.activites.HomeActivity;
import com.sample.empsytems.ui.activites.LoginActivity;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.PrefsManager;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View mRoot;
    private LinearLayout llLogout, llProfile, llListEmployee, llAddEmployee;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        llAddEmployee.setOnClickListener(this);
        llListEmployee.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        return mRoot;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llAddEmployee:
                ((HomeActivity)getActivity()).loadNavItemIndexPos(1);
                break;

            case R.id.llListEmployee:
                ((HomeActivity)getActivity()).loadNavItemIndexPos(2);
                break;

            case R.id.llProfile:
                ((HomeActivity)getActivity()).loadNavItemIndexPos(3);
                break;

            case R.id.llLogout:
                ((HomeActivity)getActivity()).performLogout();
                break;
        }
    }

    private void init() {
        llAddEmployee = mRoot.findViewById(R.id.llAddEmployee);
        llListEmployee = mRoot.findViewById(R.id.llListEmployee);
        llProfile = mRoot.findViewById(R.id.llProfile);
        llLogout = mRoot.findViewById(R.id.llLogout);
    }
}
