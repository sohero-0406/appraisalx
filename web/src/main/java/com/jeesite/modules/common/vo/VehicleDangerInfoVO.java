package com.jeesite.modules.common.vo;


import java.util.List;

/**
 * @author by Jiangyf
 * @classname VehicleDangerInfoVO
 * @description 出险记录VO
 * @date 2019/8/4 15:38
 */
public class VehicleDangerInfoVO {

    /**
     * 出险记录总表id 编辑时用
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
     * 出险记录
     */
    private List<VehicleDangerRecord> vehicleDangerRecords;


    public VehicleDangerInfoVO() {

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

    public List<VehicleDangerRecord> getVehicleDangerRecords() {
        return vehicleDangerRecords;
    }

    public void setVehicleDangerRecords(List<VehicleDangerRecord> vehicleDangerRecords) {
        this.vehicleDangerRecords = vehicleDangerRecords;
    }

    @Override
    public String toString() {
        return "VehicleDangerInfoVO{" +
                "carModel='" + carModel + '\'' +
                ", vin='" + vin + '\'' +
                ", vehicleDangerRecords=" + vehicleDangerRecords +
                '}';
    }
}
