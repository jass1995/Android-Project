package com.sample.empsytems.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sample.empsytems.models.Employee;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.models.Vehicle;
import com.sample.empsytems.models.signup.User;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "emp_payroll_system.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<EmployeePayroll, Integer> employeePayroll;
    private Dao<Vehicle, Integer> vehicleInfoDao;
    private Dao<Employee, Integer> employeeDao;
    private Dao<User, Integer> userDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, EmployeePayroll.class);
            TableUtils.createTable(connectionSource, Vehicle.class);
            TableUtils.createTable(connectionSource, Employee.class);
            TableUtils.createTable(connectionSource, User.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {



            TableUtils.dropTable(connectionSource, EmployeePayroll.class, true);
            TableUtils.dropTable(connectionSource, Vehicle.class, true);
            TableUtils.dropTable(connectionSource, Employee.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);

            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }



    public Dao<EmployeePayroll, Integer> getEmpPayrollDao() throws SQLException {
        if (employeePayroll == null) {
            employeePayroll = getDao(EmployeePayroll.class);
        }
        return employeePayroll;
    }

    public Dao<Vehicle, Integer> getVehicleInfoDao() throws SQLException {
        if (vehicleInfoDao == null) {
            vehicleInfoDao = getDao(Vehicle.class);
        }
        return vehicleInfoDao;
    }

    public Dao<Employee, Integer> getEmployeeDao() throws SQLException {
        if (employeeDao == null) {
            employeeDao = getDao(Employee.class);
        }
        return employeeDao;
    }

    public Dao<User, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    public void clearDatabase(){
        try {
            TableUtils.clearTable(getConnectionSource(), EmployeePayroll.class);
            TableUtils.clearTable(getConnectionSource(), Vehicle.class);
            TableUtils.clearTable(getConnectionSource(), Employee.class);
            TableUtils.clearTable(getConnectionSource(), User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
