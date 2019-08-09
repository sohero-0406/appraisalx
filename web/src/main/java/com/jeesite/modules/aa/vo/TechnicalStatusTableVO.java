package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.IdentifyTec;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;

import java.util.List;

/**
 * @author by Jiangyf
 * @classname TechnicalStatusTableVO
 * @description 二手车技术状况表VO
 * @date 2019/8/8 11:21
 */
public class TechnicalStatusTableVO {

    /**
     * 车辆基本信息
     */
    private VehicleBasicInfo vehicleBasicInfo;

    /**
     * 重要配置
     */
    private ImportantConfig importantConfig;

    /**
     * 是否为事故车
     */
    private IsAccidentInfo isAccidentInfo;

    /**
     * 鉴定结果
     */
    private VehicleGradeAssess vehicleGradeAssess;

    /**
     * 车辆技术状况鉴定缺陷描述
     */
    private List<IdentifyTec> identifyTecList;

    /**
     * 评估师 - 来自VehicleGradeAssess
     */
    private String evaluator;

    /**
     *  鉴定单位 - 来自DelegateLetter
     */
    private String organizationName;

    /**
     * 鉴定日期 - 来自VehicleGradeAssess
     */
    private String identifyDate;

    public TechnicalStatusTableVO() {

    }

    public VehicleBasicInfo getVehicleBasicInfo() {
        return vehicleBasicInfo;
    }

    public void setVehicleBasicInfo(VehicleBasicInfo vehicleBasicInfo) {
        this.vehicleBasicInfo = vehicleBasicInfo;
    }

    public ImportantConfig getImportantConfig() {
        return importantConfig;
    }

    public void setImportantConfig(ImportantConfig importantConfig) {
        this.importantConfig = importantConfig;
    }

    public IsAccidentInfo getIsAccidentInfo() {
        return isAccidentInfo;
    }

    public void setIsAccidentInfo(IsAccidentInfo isAccidentInfo) {
        this.isAccidentInfo = isAccidentInfo;
    }

    public VehicleGradeAssess getVehicleGradeAssess() {
        return vehicleGradeAssess;
    }

    public void setVehicleGradeAssess(VehicleGradeAssess vehicleGradeAssess) {
        this.vehicleGradeAssess = vehicleGradeAssess;
    }

    public List<IdentifyTec> getIdentifyTecList() {
        return identifyTecList;
    }

    public void setIdentifyTecList(List<IdentifyTec> identifyTecList) {
        this.identifyTecList = identifyTecList;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getIdentifyDate() {
        return identifyDate;
    }

    public void setIdentifyDate(String identifyDate) {
        this.identifyDate = identifyDate;
    }

    @Override
    public String toString() {
        return "TechnicalStatusTableVO{" +
                "vehicleBasicInfo=" + vehicleBasicInfo +
                ", importantConfig=" + importantConfig +
                ", isAccidentInfo=" + isAccidentInfo +
                ", vehicleGradeAssess=" + vehicleGradeAssess +
                ", identifyTecList=" + identifyTecList +
                ", evaluator='" + evaluator + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", identifyDate='" + identifyDate + '\'' +
                '}';
    }
}

