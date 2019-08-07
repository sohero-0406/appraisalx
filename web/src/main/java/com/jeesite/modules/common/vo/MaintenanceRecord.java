package com.jeesite.modules.common.vo;

import com.jeesite.modules.common.entity.Maintenance;
import com.jeesite.modules.common.entity.MaintenanceType;

import java.util.List;

/**
 * @author by Jiangyf
 * @classname MaintenanceRecord
 * @description 维保记录
 * @date 2019/8/6 8:42
 */
public class MaintenanceRecord {


    /**
     * 维保记录信息
     */
    private Maintenance maintenance;

    /**
     * 维保类别记录列表 - 外观覆盖件详细维修记录
     */
    private List<MaintenanceType> outsideAnalyzeRepairRecords;

    /**
     * 维保类别记录列表 - 重要组成部件详细维修记录
     */
    private List<MaintenanceType> componentAnalyzeRepairRecords;

    /**
     * 维保类别记录列表 - 该车所有的详细维修记录
     */
    private List<MaintenanceType> normalRepairRecords;

    public MaintenanceRecord() {

    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public List<MaintenanceType> getOutsideAnalyzeRepairRecords() {
        return outsideAnalyzeRepairRecords;
    }

    public void setOutsideAnalyzeRepairRecords(List<MaintenanceType> outsideAnalyzeRepairRecords) {
        this.outsideAnalyzeRepairRecords = outsideAnalyzeRepairRecords;
    }

    public List<MaintenanceType> getComponentAnalyzeRepairRecords() {
        return componentAnalyzeRepairRecords;
    }

    public void setComponentAnalyzeRepairRecords(List<MaintenanceType> componentAnalyzeRepairRecords) {
        this.componentAnalyzeRepairRecords = componentAnalyzeRepairRecords;
    }

    public List<MaintenanceType> getNormalRepairRecords() {
        return normalRepairRecords;
    }

    public void setNormalRepairRecords(List<MaintenanceType> normalRepairRecords) {
        this.normalRepairRecords = normalRepairRecords;
    }

    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "maintenance=" + maintenance +
                ", outsideAnalyzeRepairRecords=" + outsideAnalyzeRepairRecords +
                ", componentAnalyzeRepairRecords=" + componentAnalyzeRepairRecords +
                ", normalRepairRecords=" + normalRepairRecords +
                '}';
    }
}
