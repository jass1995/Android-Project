package com.sample.empsytems.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "employee")
public class Employee {

    @DatabaseField(columnName = "employee_id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "employee_cat")
    public int employeeType; //Part Time, Intern or Full Time.

    ////////////////   PART TIME  /////////////////////////

    @DatabaseField(columnName = "emp_hour_rate")
    public String empHourRate;

    @DatabaseField(columnName = "emp_worked_hours")
    public String empWorkedHours;

    @DatabaseField(columnName = "emp_work_type")
    public int empWorkType; //1. Commission based, 2. Fixed

    @DatabaseField(columnName = "emp_extra_amt")
    public String empExtraAmt;


    ////////////////   INTERN  /////////////////////////

    @DatabaseField(columnName = "emp_school_name")
    public String empSchoolName;

    ////////////////   FULL TIME  /////////////////////////

    @DatabaseField(columnName = "emp_salary")
    public String empSalary;

    @DatabaseField(columnName = "emp_bonus")
    public String empBonus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
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

    public String getEmpSchoolName() {
        return empSchoolName;
    }

    public void setEmpSchoolName(String empSchoolName) {
        this.empSchoolName = empSchoolName;
    }

    public String getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(String empSalary) {
        this.empSalary = empSalary;
    }

    public String getEmpBonus() {
        return empBonus;
    }

    public void setEmpBonus(String empBonus) {
        this.empBonus = empBonus;
    }
}
