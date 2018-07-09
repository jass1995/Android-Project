package com.sample.empsytems.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "employee_part_time")
public class EmployeePartTime {

    @DatabaseField(columnName = "emp_part_time_id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "emp_hour_rate")
    public String empHourRate;

    @DatabaseField(columnName = "emp_worked_hours")
    public String empWorkedHours;

    @DatabaseField(columnName = "emp_work_type")
    public int empWorkType; //1. Commission based, 2. Fixed

    @DatabaseField(columnName = "emp_extra_amt")
    public String empExtraAmt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpHourRate() {
        return empHourRate;
    }

    public void setEmpHourRate(String empHourRate) {
        this.empHourRate = empHourRate;
    }

    public String getEmpWorkedHours() {
        return empWorkedHours;
    }

    public void setEmpWorkedHours(String empWorkedHours) {
        this.empWorkedHours = empWorkedHours;
    }

    public int getEmpWorkType() {
        return empWorkType;
    }

    public void setEmpWorkType(int empWorkType) {
        this.empWorkType = empWorkType;
    }

    public String getEmpExtraAmt() {
        return empExtraAmt;
    }

    public void setEmpExtraAmt(String empExtraAmt) {
        this.empExtraAmt = empExtraAmt;
    }
}
