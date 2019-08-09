package com.jeesite.modules.aa.vo;

import java.util.List;

/**
 * @author by Jiangyf
 * @classname VehicleBasicInfo
 * @description 车辆基本信息 - 技术状况表使用
 * @date 2019/8/8 17:20
 */
public class VehicleBasicInfo {

    /**
     * 厂牌型号 - labelType
     */
    private String labelType;

    /**
     * 牌照号码
     */
    private String licensePlateNum;

    /**
     * 发动机号
     */
    private String engineNum;

    /**
     * VIN码
     */
    private String vinCode;

    /**
     * 注册登记日期
     */
    private String registerDate;

    /**
     * 表征里程
     */
    private String mileage;

    /**
     * 品牌名称 - 来自登记证
     */
    private String brandName;

    /**
     * 国产/进口 - 来自登记证
     */
    private String brandType;

    /**
     * 车身颜色
     */
    private String color;

    /**
     * 年检证明 - 有(年月日)/无
     * 来自车辆单证信息 - project = 6 -> state -> validity
     * dict_type = aa_vehicle_document_info
     */
    private String project6Validity;

    /**
     * 购置税证书
     * 来自车辆单证信息 - project = 8 -> state
     */
    private String project8;

    /**
     * 车船税证明
     * 来自车辆单证信息 - project = 3 -> state -> validity
     */
    private String project3Validity;

    /**
     * 交强险
     * 来自车辆单证信息 - project = 4 -> state -> validity
     */
    private String project4Validity;

    /**
     * 使用性质
     */
    private String usage;

    /**
     * 其他法定凭证、证明
     */
    private List<String> otherDocuments;

    /**
     * 车主名称/姓名 - 来自DelegateUser
     */
    private String name;

    /**
     * 企业法人证书代号/身份证号码 - 来自DelegateUser
     */
    private String idNum;

    public VehicleBasicInfo() {
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        this.brandType = brandType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProject6Validity() {
        return project6Validity;
    }

    public void setProject6Validity(String project6Validity) {
        this.project6Validity = project6Validity;
    }

    public String getProject8() {
        return project8;
    }

    public void setProject8(String project8) {
        this.project8 = project8;
    }

    public String getProject3Validity() {
        return project3Validity;
    }

    public void setProject3Validity(String project3Validity) {
        this.project3Validity = project3Validity;
    }

    public String getProject4Validity() {
        return project4Validity;
    }

    public void setProject4Validity(String project4Validity) {
        this.project4Validity = project4Validity;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public List<String> getOtherDocuments() {
        return otherDocuments;
    }

    public void setOtherDocuments(List<String> otherDocuments) {
        this.otherDocuments = otherDocuments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    @Override
    public String toString() {
        return "VehicleBasicInfo{" +
                "labelType='" + labelType + '\'' +
                ", licensePlateNum='" + licensePlateNum + '\'' +
                ", engineNum='" + engineNum + '\'' +
                ", vinCode='" + vinCode + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", mileage='" + mileage + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandType='" + brandType + '\'' +
                ", color='" + color + '\'' +
                ", project6Validity='" + project6Validity + '\'' +
                ", project8='" + project8 + '\'' +
                ", project3Validity='" + project3Validity + '\'' +
                ", project4Validity='" + project4Validity + '\'' +
                ", usage='" + usage + '\'' +
                ", otherDocuments=" + otherDocuments +
                ", name='" + name + '\'' +
                ", idNum='" + idNum + '\'' +
                '}';
    }
}
