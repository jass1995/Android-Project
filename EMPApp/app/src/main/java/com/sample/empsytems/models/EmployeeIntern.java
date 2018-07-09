package com.sample.empsytems.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "employee_intern")
public class EmployeeIntern {

    @DatabaseField(columnName = "emp_intern_id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "emp_school_name")
    public String empSchoolName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpSchoolName() {
        return empSchoolName;
    }

    public void setEmpSchoolName(String empSchoolName) {
        this.empSchoolName = empSchoolName;
    }
}
