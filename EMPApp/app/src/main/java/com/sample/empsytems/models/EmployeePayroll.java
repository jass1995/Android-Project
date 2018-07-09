package com.sample.empsytems.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "emp_payroll")
public class EmployeePayroll {

    @DatabaseField(columnName = "emp_id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "emp_name")
    public String empName;

    @DatabaseField(columnName = "emp_dob")
    public String empDOB;

    @DatabaseField(columnName = "emp_vehicle")
    public boolean havingVehicle;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public VehicleInfo vehicleInfo;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public Employee employee;

    public EmployeePayroll(int id, String empName, String empDOB, boolean havingVehicle, VehicleInfo vehicleInfo) {
        this.id = id;
        this.empName = empName;
        this.empDOB = empDOB;
        this.havingVehicle = havingVehicle;
        this.vehicleInfo = vehicleInfo;
    }

    public EmployeePayroll() {
        this.vehicleInfo = new VehicleInfo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrEmpName() {
        return empName;
    }

    public void setStrEmpName(String empName) {
        this.empName = empName;
    }

    public String getStrEmpDOB() {
        return empDOB;
    }

    public void setStrEmpDOB(String empDOB) {
        this.empDOB = empDOB;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDOB() {
        return empDOB;
    }

    public void setEmpDOB(String empDOB) {
        this.empDOB = empDOB;
    }

    public boolean isHavingVehicle() {
        return havingVehicle;
    }

    public void setHavingVehicle(boolean havingVehicle) {
        this.havingVehicle = havingVehicle;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
