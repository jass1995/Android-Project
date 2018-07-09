package com.sample.empsytems.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "emp_vehicle")
public class VehicleInfo {

    @DatabaseField(columnName = "vehicle_id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "vehicle_type")
    public int vehicleType; //Car or Bike

    @DatabaseField(columnName = "vehicle_name")
    public String vehicleName;

    @DatabaseField(columnName = "vehicle_image")
    public int vehicleImage;

    @DatabaseField(columnName = "vehicle_model")
    public String vehicleModel;

    @DatabaseField(columnName = "vehicle_plateno")
    public String vehiclePlateNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(int vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehiclePlateNo() {
        return vehiclePlateNo;
    }

    public void setVehiclePlateNo(String vehiclePlateNo) {
        this.vehiclePlateNo = vehiclePlateNo;
    }
}
