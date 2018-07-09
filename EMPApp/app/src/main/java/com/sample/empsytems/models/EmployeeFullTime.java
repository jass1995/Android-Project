package com.sample.empsytems.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "employee_full_time")
public class EmployeeFullTime {

    @DatabaseField(columnName = "emp_full_time_id", generatedId = true)
    private int id;

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
