package com.jeesite.modules.common.vo;

import com.jeesite.modules.common.entity.VehicleDangerDetail;

import java.util.List;

/**
 * @author by Jiangyf
 * @classname VehicleDangerRecord
 * @description 出险记录
 * @date 2019/8/5 9:42
 */
public class VehicleDangerRecord {

    /**
     * 出险日期
     */
    private String dangerDate;

    /**
     * 维修金额（单位：分）
     */
    private String serviceMoney;

    /**
     * 理赔记录
     */
    private List<VehicleDangerDetail> vehicleDangerDetails;

    public VehicleDangerRecord() {

    }

    public String getDangerDate() {
        return dangerDate;
    }

    public void setDangerDate(String dangerDate) {
        this.dangerDate = dangerDate;
    }

    public String getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(String serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public List<VehicleDangerDetail> getVehicleDangerDetails() {
        return vehicleDangerDetails;
    }

    public void setVehicleDangerDetails(List<VehicleDangerDetail> vehicleDangerDetails) {
        this.vehicleDangerDetails = vehicleDangerDetails;
    }

    @Override
    public String toString() {
        return "VehicleDangerRecord{" +
                "dangerDate='" + dangerDate + '\'' +
                ", serviceMoney='" + serviceMoney + '\'' +
                ", vehicleDangerDetails=" + vehicleDangerDetails +
                '}';
    }
}
