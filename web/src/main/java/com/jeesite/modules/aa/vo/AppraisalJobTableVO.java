package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.common.entity.VehicleInfo;

import java.util.List;

public class AppraisalJobTableVO {

    private CarInfo carInfo;        //委托车辆信息
    private List<VehicleInstallInfo> vehicleInstallInfoList;      //车辆加装信息
    private List<VehicleDocumentInfo> vehicleDocumentInfoList;        //车辆单证信息
    private VehicleInfo vehicleInfo;        //车辆配置全表
    private CheckBodySkeleton checkBodySkeleton;        //检查车体骨架
    private IdentifyTecDetail identifyTecDetail;        //鉴定技术状况详情
    private VehicleGradeAssess vehicleGradeAssess;      //车辆等级评定
    private DelegateLetter delegateLetter;      //委托书信息

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public List<VehicleInstallInfo> getVehicleInstallInfoList() {
        return vehicleInstallInfoList;
    }

    public void setVehicleInstallInfoList(List<VehicleInstallInfo> vehicleInstallInfoList) {
        this.vehicleInstallInfoList = vehicleInstallInfoList;
    }

    public List<VehicleDocumentInfo> getVehicleDocumentInfoList() {
        return vehicleDocumentInfoList;
    }

    public void setVehicleDocumentInfoList(List<VehicleDocumentInfo> vehicleDocumentInfoList) {
        this.vehicleDocumentInfoList = vehicleDocumentInfoList;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public CheckBodySkeleton getCheckBodySkeleton() {
        return checkBodySkeleton;
    }

    public void setCheckBodySkeleton(CheckBodySkeleton checkBodySkeleton) {
        this.checkBodySkeleton = checkBodySkeleton;
    }

    public IdentifyTecDetail getIdentifyTecDetail() {
        return identifyTecDetail;
    }

    public void setIdentifyTecDetail(IdentifyTecDetail identifyTecDetail) {
        this.identifyTecDetail = identifyTecDetail;
    }

    public VehicleGradeAssess getVehicleGradeAssess() {
        return vehicleGradeAssess;
    }

    public void setVehicleGradeAssess(VehicleGradeAssess vehicleGradeAssess) {
        this.vehicleGradeAssess = vehicleGradeAssess;
    }

    public DelegateLetter getDelegateLetter() {
        return delegateLetter;
    }

    public void setDelegateLetter(DelegateLetter delegateLetter) {
        this.delegateLetter = delegateLetter;
    }
}
