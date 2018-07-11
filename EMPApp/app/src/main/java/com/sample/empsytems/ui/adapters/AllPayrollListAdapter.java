package com.sample.empsytems.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sample.empsytems.R;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.ui.activites.ViewEmpDetailActivity;
import com.sample.empsytems.utils.CommonMethods;
import com.sample.empsytems.utils.Constants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllPayrollListAdapter extends RecyclerView.Adapter<AllPayrollListAdapter.MyViewHolder> {

    private Context context;
    private List<EmployeePayroll> mAllPayrollList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout llRowView;
        TextView tvEmpName, tvEmpDOB, tvHasVehicle, tvEmpType;
        CircleImageView ivVehicleImg;

        private MyViewHolder(View view) {
            super(view);
            llRowView = view.findViewById(R.id.llRowView);
            tvEmpName = view.findViewById(R.id.tvEmpName);
            tvEmpDOB = view.findViewById(R.id.tvEmpDOB);
            tvHasVehicle = view.findViewById(R.id.tvHasVehicle);
            tvEmpType = view.findViewById(R.id.tvEmpType);
            ivVehicleImg = view.findViewById(R.id.ivVehicleImg);

            llRowView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ViewEmpDetailActivity.class);
            intent.putExtra("key_name", mAllPayrollList.get(getAdapterPosition()).empName);
            context.startActivity(intent);
        }
    }

    public AllPayrollListAdapter(Context context, List<EmployeePayroll> mAllPayrollList) {
        this.context = context;
        this.mAllPayrollList = mAllPayrollList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_all_payroll_emp, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EmployeePayroll mEmpPayroll = mAllPayrollList.get(position);

        holder.tvEmpName.setText(mEmpPayroll.getStrEmpName());
        holder.tvEmpDOB.setText(mEmpPayroll.getStrEmpDOB());

        if (mEmpPayroll.havingVehicle) {
            holder.ivVehicleImg.setImageResource(CommonMethods.getVehicleIconByPosition(
                    mEmpPayroll.getVehicle().vehicleType,
                    mEmpPayroll.getVehicle().getVehicleImage()
            ));
            holder.tvHasVehicle.setText(context.getResources().getString(R.string.text_label_yes));
        } else {
            holder.ivVehicleImg.setImageResource(R.drawable.ic_no_vehicle);
            holder.tvHasVehicle.setText(context.getResources().getString(R.string.text_label_no));
        }

        int empCategory = mEmpPayroll.getEmployee().employeeType;
        switch (empCategory) {
            case Constants.EMP_TYPE_PART_TIME:
                holder.tvEmpType.setText(context.getResources().getString(R.string.rb_label_part_time));
                break;

            case Constants.EMP_TYPE_INTERN:
                holder.tvEmpType.setText(context.getResources().getString(R.string.rb_label_intern));
                break;

            case Constants.EMP_TYPE_FULL_TIME:
                holder.tvEmpType.setText(context.getResources().getString(R.string.rb_label_full_time));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mAllPayrollList.size();
    }
}
