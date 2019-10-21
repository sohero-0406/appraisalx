package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.VehicleInstallInfo;

import java.util.List;

/**
 * @author by Jiangyf
 * @classname ImportantConfig
 * @description 重要配置 - 技术状况表使用
 * @date 2019/8/8 17:50
 */
public class ImportantConfig {

    /**
     * 燃油标号 - ranyoubiaohao
     */
    private String fuelLabel;

    /**
     * 排量
     */
    private String displacement;

    /**
     * 缸数 - qigangshuGe
     */
    private String cylindersNum;

    /**
     * 发动机功率 - 来自登记证
     */
    private String enginePower;

    /**
     * 排放标准 - huanbaobiaozhun
     */
    private String emissionStandards;

    /**
     * 变速器形式 - biansuxiangleixing
     */
    private String transmissionForm;

    /**
     * 安全气囊 zhuFujiashizuoanquanqinang + houpaizhongyanganquanqinang
     */
    private String airbag;

    /**
     * 驱动方式 - qudongfangshi
     */
    private String driveMode;

    /**
     * ABS - ABSfangbaosi(有/无)
     */
    private String abs;

    /**
     * 其他重要配置 - 加装项目
     */
    private List<String> vehicleInstallInfos;

    public ImportantConfig() {
    }

    public String getFuelLabel() {
        return fuelLabel;
    }

    public void setFuelLabel(String fuelLabel) {
        this.fuelLabel = fuelLabel;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getCylindersNum() {
        return cylindersNum;
    }

    public void setCylindersNum(String cylindersNum) {
        this.cylindersNum = cylindersNum;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getEmissionStandards() {
        return emissionStandards;
    }

    public void setEmissionStandards(String emissionStandards) {
        this.emissionStandards = emissionStandards;
    }

    public String getTransmissionForm() {
        return transmissionForm;
    }

    public void setTransmissionForm(String transmissionForm) {
        this.transmissionForm = transmissionForm;
    }

    public String getAirbag() {
        return airbag;
    }

    public void setAirbag(String airbag) {
        this.airbag = airbag;
    }

    public String getDriveMode() {
        return driveMode;
    }

    public void setDriveMode(String driveMode) {
        this.driveMode = driveMode;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public List<String> getVehicleInstallInfos() {
        return vehicleInstallInfos;
    }

    public void setVehicleInstallInfos(List<String> vehicleInstallInfos) {
        this.vehicleInstallInfos = vehicleInstallInfos;
    }

    @Override
    public String toString() {
        return "ImportantConfig{" +
                "fuelLabel='" + fuelLabel + '\'' +
                ", displacement='" + displacement + '\'' +
                ", cylindersNum='" + cylindersNum + '\'' +
                ", enginePower='" + enginePower + '\'' +
                ", emissionStandards='" + emissionStandards + '\'' +
                ", transmissionForm='" + transmissionForm + '\'' +
                ", airbag='" + airbag + '\'' +
                ", driveMode='" + driveMode + '\'' +
                ", abs='" + abs + '\'' +
                ", vehicleInstallInfos=" + vehicleInstallInfos +
                '}';
    }
}
