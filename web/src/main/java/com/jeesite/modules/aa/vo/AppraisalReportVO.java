package com.jeesite.modules.aa.vo;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.common.entity.VehicleInfo;

import java.util.List;

public class AppraisalReportVO {

    private DelegateLetter delegateLetter;      //委托书信息 --绪言
    private DelegateUser delegateUser;      //委托人
    private CarInfo carInfo;        //车辆信息
    private List<VehicleDocumentInfo> vehicleDocumentInfolist;        //车辆单证信息
    private CheckTradableVehicles checkTradableVehicles;        //检查可交易车辆
    private VehicleGradeAssess vehicleGradeAssess;      //车辆等级评定
    private Calculate calculate;        //计算车辆价值

    private JSONObject vehicleInfo;        //车辆配置全表
    private String carOwner;        //车主
    private String defectDescription;       //缺陷描述
    private String priceCapital;        //金额大写


    public DelegateUser getDelegateUser() {
        return delegateUser;
    }

    public void setDelegateUser(DelegateUser delegateUser) {
        this.delegateUser = delegateUser;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public List<VehicleDocumentInfo> getVehicleDocumentInfolist() {
        return vehicleDocumentInfolist;
    }

    public void setVehicleDocumentInfolist(List<VehicleDocumentInfo> vehicleDocumentInfolist) {
        this.vehicleDocumentInfolist = vehicleDocumentInfolist;
    }

    public CheckTradableVehicles getCheckTradableVehicles() {
        return checkTradableVehicles;
    }

    public void setCheckTradableVehicles(CheckTradableVehicles checkTradableVehicles) {
        this.checkTradableVehicles = checkTradableVehicles;
    }

    public VehicleGradeAssess getVehicleGradeAssess() {
        return vehicleGradeAssess;
    }

    public void setVehicleGradeAssess(VehicleGradeAssess vehicleGradeAssess) {
        this.vehicleGradeAssess = vehicleGradeAssess;
    }

    public Calculate getCalculate() {
        return calculate;
    }

    public void setCalculate(Calculate calculate) {
        this.calculate = calculate;
    }

    public DelegateLetter getDelegateLetter() {
        return delegateLetter;
    }

    public void setDelegateLetter(DelegateLetter delegateLetter) {
        this.delegateLetter = delegateLetter;
    }

    public JSONObject getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(JSONObject vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public String getPriceCapital() {
        return priceCapital;
    }

    public void setPriceCapital(String priceCapital) {
        this.priceCapital = priceCapital;
    }
}
