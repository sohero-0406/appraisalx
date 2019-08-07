package com.jeesite.modules.common.vo;


import java.util.List;

/**
 * @author by Jiangyf
 * @classname MaintenanceInfoVO
 * @description 维保记录VO
 * @date 2019/8/4 11:20
 */
public class MaintenanceInfoVO {

    /**
     * 维保记录总表id
     */
    private String id;

    /**
     * 车型名称
     */
    private String carModel;

    /**
     * VIN码
     */
    private String vin;

    /**
     * 维保记录
     */
    private List<MaintenanceRecord> maintenanceRecords;


    public MaintenanceInfoVO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<MaintenanceRecord> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void setMaintenanceRecords(List<MaintenanceRecord> maintenanceRecords) {
        this.maintenanceRecords = maintenanceRecords;
    }

    @Override
    public String toString() {
        return "MaintenanceInfoVO{" +
                "carModel='" + carModel + '\'' +
                ", vin='" + vin + '\'' +
                ", maintenanceRecords=" + maintenanceRecords +
                '}';
    }
}
