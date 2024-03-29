/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.dao.MaintenanceDao;
import com.jeesite.modules.common.dao.MaintenanceTotalDao;
import com.jeesite.modules.common.dao.MaintenanceTypeDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Maintenance;
import com.jeesite.modules.common.entity.MaintenanceTotal;
import com.jeesite.modules.common.entity.MaintenanceType;
import com.jeesite.modules.common.vo.MaintenanceInfoVO;
import com.jeesite.modules.common.vo.MaintenanceRecord;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆维保记录Service
 *
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Service
@Transactional(readOnly = true)
public class MaintenanceService extends CrudService<MaintenanceDao, Maintenance> {

    @Autowired
    private MaintenanceTypeDao maintenanceTypeDao;

    @Autowired
    private MaintenanceTotalService maintenanceTotalService;

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;

    @Autowired
    private MaintenanceTotalDao maintenanceTotalDao;

    /**
     * 获取单条数据
     *
     * @param maintenance
     * @return
     */
    @Override
    public Maintenance get(Maintenance maintenance) {
        return super.get(maintenance);
    }

    /**
     * 查询分页数据
     *
     * @param maintenance 查询条件
     * @return
     */
    @Override
    public Page<Maintenance> findPage(Maintenance maintenance) {
        return super.findPage(maintenance);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param maintenance
     */
    @Override
    @Transactional(readOnly = false)
    public void save(Maintenance maintenance) {
        super.save(maintenance);
    }

    /**
     * 更新状态
     *
     * @param maintenance
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Maintenance maintenance) {
        super.updateStatus(maintenance);
    }

    /**
     * 删除数据
     *
     * @param maintenance
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Maintenance maintenance) {
        super.delete(maintenance);
    }

    /**
     * @description: 加载维保记录
     * @param: [maintenanceTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 17:05
     */
    public CommonResult findMaintenance(MaintenanceTotal maintenanceTotal) {
        CommonResult commonResult = new CommonResult();
        Maintenance maintenance = new Maintenance();
        if (StringUtils.isBlank(maintenanceTotal.getId()) && StringUtils.isNotBlank(maintenanceTotal.getVinCode())) {
            // 学生端调用查询维保记录
            MaintenanceTotal total = maintenanceTotalDao.getByEntity(maintenanceTotal);
            if (null == total) {
                return new CommonResult();
            }
            maintenance.setMaintenanceTotalId(total.getId());
        } else {
            maintenance.setMaintenanceTotalId(maintenanceTotal.getId());
        }
        List<Maintenance> maintenances = dao.findMaintenance(maintenance);
        if (maintenances.size() <= 0) {
            return new CommonResult();
        }
        commonResult.setData(maintenances);
        return commonResult;
    }

    /**
     * @description: 加载维保详情
     * @param: [maintenance]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 17:15
     */
    public CommonResult findMaintenanceDetail(Maintenance maintenance) {
        CommonResult commonResult = new CommonResult();
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        // 设置维保详情
        maintenanceRecord.setMaintenance(dao.findMaintenanceDetail(maintenance));
        // 设置详细维修记录
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setMaintenanceId(maintenance.getId());
        List<MaintenanceType> outsideAnalyzeRepairRecords = maintenanceTypeDao.findRepairRecords(maintenanceType, "1");
        List<MaintenanceType> componentAnalyzeRepairRecords = maintenanceTypeDao.findRepairRecords(maintenanceType, "2");
        List<MaintenanceType> normalRepairRecords = maintenanceTypeDao.findRepairRecords(maintenanceType, "3");
        maintenanceRecord.setOutsideAnalyzeRepairRecords(outsideAnalyzeRepairRecords);
        maintenanceRecord.setComponentAnalyzeRepairRecords(componentAnalyzeRepairRecords);
        maintenanceRecord.setNormalRepairRecords(normalRepairRecords);
        commonResult.setData(maintenanceRecord);
        return commonResult;
    }

    /**
     * @description: 保存维保记录
     * @param: [maintenanceInfoVO]
     * @return: void
     * @author: Jiangyf
     * @date: 2019/8/6
     * @time: 8:32
     */
    @Transactional(readOnly = false)
    public void saveMaintenance(MaintenanceTotal total, MaintenanceInfoVO maintenanceInfoVO) {
        String totalId = null;
        // 获取维保记录列表
        List<MaintenanceRecord> maintenanceRecords = maintenanceInfoVO.getMaintenanceRecords();
        if (null == total) {
            // 维保记录总表对象 先存入车型名称和VIN码 获取维保记录总对象id
            MaintenanceTotal maintenanceTotal = new MaintenanceTotal();
            maintenanceTotal.setModelName(maintenanceInfoVO.getCarModel());
            maintenanceTotal.setVinCode(maintenanceInfoVO.getVin());
            maintenanceTotal.setMaintenanceCount((long) maintenanceRecords.size());
            maintenanceTotalService.save(maintenanceTotal);
            totalId = maintenanceTotalService.get(maintenanceTotal).getId();
        } else {
            total.setModelName(maintenanceInfoVO.getCarModel());
            total.setVinCode(maintenanceInfoVO.getVin());
            total.setMaintenanceCount((long) maintenanceRecords.size());
            totalId = total.getId();
            // 更新维保记录总表对象
            maintenanceTotalService.update(total);
            // 删除关联记录
            maintenanceTotalService.deleteMaintenance(maintenanceTotalService.get(totalId).getId(), false);
        }

        for (MaintenanceRecord maintenanceRecord : maintenanceRecords) {
            Maintenance maintenance = maintenanceRecord.getMaintenance();
            maintenance.setMaintenanceTotalId(totalId);
            this.save(maintenance);
            // 获取维保记录id
            String maintenanceId = this.get(maintenance).getId();
            // 维保类别记录列表 - 外观覆盖件详细维修记录、重要组成部件详细维修记录、该车所有的详细维修记录
            List<MaintenanceType> outsideAnalyzeRepairRecords = maintenanceRecord.getOutsideAnalyzeRepairRecords();
            List<MaintenanceType> componentAnalyzeRepairRecords = maintenanceRecord.getComponentAnalyzeRepairRecords();
            List<MaintenanceType> normalRepairRecords = maintenanceRecord.getNormalRepairRecords();
            // 存入维保类别记录
            if (CollectionUtils.isNotEmpty(outsideAnalyzeRepairRecords)) {
                outsideAnalyzeRepairRecords.forEach(outsideAnalyzeRepairRecord -> {
                    outsideAnalyzeRepairRecord.setMaintenanceId(maintenanceId);
                    maintenanceTypeService.save(outsideAnalyzeRepairRecord);
                });
            }
            if (CollectionUtils.isNotEmpty(componentAnalyzeRepairRecords)) {
                componentAnalyzeRepairRecords.forEach(componentAnalyzeRepairRecord -> {
                    componentAnalyzeRepairRecord.setMaintenanceId(maintenanceId);
                    maintenanceTypeService.save(componentAnalyzeRepairRecord);
                });
            }
            if (CollectionUtils.isNotEmpty(normalRepairRecords)) {
                normalRepairRecords.forEach(normalRepairRecord -> {
                    normalRepairRecord.setMaintenanceId(maintenanceId);
                    maintenanceTypeService.save(normalRepairRecord);
                });
            }
        }
    }
}