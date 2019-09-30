package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.sys.entity.DictData;

import java.util.List;

public class VehicleGradeAssessVO {

    /**
     * 车辆等级评定
     */
    private VehicleGradeAssess vehicleGradeAssess;

    /**
     * 综合分数
     */
    private String score;

    /**
     * 技术状况
     */
    private List<DictData> technicalStatusList;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public VehicleGradeAssess getVehicleGradeAssess() {
        return vehicleGradeAssess;
    }

    public void setVehicleGradeAssess(VehicleGradeAssess vehicleGradeAssess) {
        this.vehicleGradeAssess = vehicleGradeAssess;
    }

    public List<DictData> getTechnicalStatusList() {
        return technicalStatusList;
    }

    public void setTechnicalStatusList(List<DictData> technicalStatusList) {
        this.technicalStatusList = technicalStatusList;
    }
}
