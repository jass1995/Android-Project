package com.sample.empsytems.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sample.empsytems.models.Employee;
import com.sample.empsytems.models.EmployeeFullTime;
import com.sample.empsytems.models.EmployeeIntern;
import com.sample.empsytems.models.EmployeePartTime;
import com.sample.empsytems.models.EmployeePayroll;
import com.sample.empsytems.models.VehicleInfo;
import com.sample.empsytems.models.signup.User;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/

    private static final String DATABASE_NAME = "emp_payroll_system.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<EmployeePayroll, Integer> employeePayroll;
    private Dao<VehicleInfo, Integer> vehicleInfoDao;
    private Dao<Employee, Integer> employeeDao;
    private Dao<User, Integer> userDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, EmployeePayroll.class);
            TableUtils.createTable(connectionSource, VehicleInfo.class);
            TableUtils.createTable(connectionSource, Employee.class);
            TableUtils.createTable(connectionSource, User.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, EmployeePayroll.class, true);
            TableUtils.dropTable(connectionSource, VehicleInfo.class, true);
            TableUtils.dropTable(connectionSource, Employee.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);

            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<EmployeePayroll, Integer> getEmpPayrollDao() throws SQLException {
        if (employeePayroll == null) {
            employeePayroll = getDao(EmployeePayroll.class);
        }
        return employeePayroll;
    }

    public Dao<VehicleInfo, Integer> getVehicleInfoDao() throws SQLException {
        if (vehicleInfoDao == null) {
            vehicleInfoDao = getDao(VehicleInfo.class);
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
            TableUtils.clearTable(getConnectionSource(), VehicleInfo.class);
            TableUtils.clearTable(getConnectionSource(), Employee.class);
            TableUtils.clearTable(getConnectionSource(), User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
