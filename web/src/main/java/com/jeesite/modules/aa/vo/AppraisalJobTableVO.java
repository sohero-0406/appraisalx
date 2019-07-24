package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.common.entity.VehicleInfo;

import java.util.List;

public class AppraisalJobTableVO {

    private CarInfo carInfo;        //委托车辆信息
    private List<VehicleInstallInfo> vehicleInstallInfoList;      //车辆加装信息
    private List<VehicleDocumentInfo> vehicleDocumentInfoList;        //车辆单证信息
    private VehicleInfo vehicleInfo;        //车辆配置全表
    private List<CheckBodySkeleton> checkBodySkeletonList;        //检查车体骨架
    private List<IdentifyTec> identifyTecList;      //鉴定技术状况
    private List<IdentifyTecDetail> identifyTecDetailList;        //鉴定技术状况详情
    private VehicleGradeAssess vehicleGradeAssess;      //车辆等级评定
    private DelegateLetter delegateLetter;      //委托书信息
    private String niankuanxinghao;     //年款型号

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

    public List<CheckBodySkeleton> getCheckBodySkeletonList() {
        return checkBodySkeletonList;
    }

    public void setCheckBodySkeletonList(List<CheckBodySkeleton> checkBodySkeletonList) {
        this.checkBodySkeletonList = checkBodySkeletonList;
    }

    public List<IdentifyTec> getIdentifyTecList() {
        return identifyTecList;
    }

    public void setIdentifyTecList(List<IdentifyTec> identifyTecList) {
        this.identifyTecList = identifyTecList;
    }

    public List<IdentifyTecDetail> getIdentifyTecDetailList() {
        return identifyTecDetailList;
    }

    public void setIdentifyTecDetailList(List<IdentifyTecDetail> identifyTecDetailList) {
        this.identifyTecDetailList = identifyTecDetailList;
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

    public String getNiankuanxinghao() {
        return niankuanxinghao;
    }

    public void setNiankuanxinghao(String niankuanxinghao) {
        this.niankuanxinghao = niankuanxinghao;
    }
}
