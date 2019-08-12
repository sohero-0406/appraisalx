/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;


import com.jeesite.common.constant.CodeConstant;
import com.jeesite.modules.common.dao.MaintenanceDao;
import com.jeesite.modules.common.dao.MaintenanceTypeDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Maintenance;
import com.jeesite.modules.common.entity.MaintenanceType;
import com.jeesite.modules.common.vo.MaintenanceInfoVO;
import com.jeesite.modules.common.vo.MaintenanceRecord;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.entity.MaintenanceTotal;
import com.jeesite.modules.common.dao.MaintenanceTotalDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆维保总表Service
 *
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Service
@Transactional(readOnly = true)
public class MaintenanceTotalService extends CrudService<MaintenanceTotalDao, MaintenanceTotal> {

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;

    @Autowired
    private MaintenanceDao maintenanceDao;

    @Autowired
    private MaintenanceTypeDao maintenanceTypeDao;

    /**
     * 获取单条数据
     *
     * @param maintenanceTotal
     * @return
     */
    @Override
    public MaintenanceTotal get(MaintenanceTotal maintenanceTotal) {
        return super.get(maintenanceTotal);
    }

    /**
     * 查询分页数据
     *
     * @param maintenanceTotal 查询条件
     * @return
     */
    @Override
    public Page<MaintenanceTotal> findPage(MaintenanceTotal maintenanceTotal) {
        return super.findPage(maintenanceTotal);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param maintenanceTotal
     */
    @Override
    @Transactional(readOnly = false)
    public void save(MaintenanceTotal maintenanceTotal) {
        super.save(maintenanceTotal);
    }

    /**
     * 更新状态
     *
     * @param maintenanceTotal
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(MaintenanceTotal maintenanceTotal) {
        super.updateStatus(maintenanceTotal);
    }

    /**
     * 删除数据
     *
     * @param maintenanceTotal
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(MaintenanceTotal maintenanceTotal) {
        super.delete(maintenanceTotal);
    }

    /**
     * @description: 删除维保记录
     * @param: [maintenanceTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/6
     * @time: 16:56
     */
    @Transactional(readOnly = false)
    public CommonResult deleteMaintenance(MaintenanceTotal total, boolean flag) {
        CommonResult commonResult = new CommonResult();
        try {
            // 根据维保记录总表获取关联的实体对象(列表)
            MaintenanceTotal maintenanceTotal = this.get(total.getId());
            Maintenance maintenance = new Maintenance();
            maintenance.setMaintenanceTotalId(total.getId());
            List<Maintenance> maintenances = maintenanceService.findList(maintenance);
            List<MaintenanceType> maintenanceTypes = maintenanceTypeService.findListByMaintenances(maintenances);
            // 依次删除数据记录 物理删除
            if (flag) {
                dao.phyDelete(maintenanceTotal);
            }
            maintenances.forEach(x -> maintenanceDao.phyDelete(x));
            maintenanceTypes.forEach(x -> maintenanceTypeDao.phyDelete(x));
        } catch (Exception e) {
            commonResult.setMsg("空对象");
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setData(e.getMessage());
        }
        return commonResult;
    }

    /**
     * @description: 加载已保存维保记录(编辑时调用)
     * @param: [maintenanceTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/7
     * @time: 10:41
     */
    public CommonResult findSavedMaintenance(MaintenanceTotal total) {
        CommonResult commonResult = new CommonResult();
        MaintenanceInfoVO maintenanceInfoVO = new MaintenanceInfoVO();
        List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
        // 填充传递参数到VO对象中
        maintenanceInfoVO.setId(total.getId());
        // 获取维保总表数据
        MaintenanceTotal maintenanceTotal = this.get(total.getId());
        if (null == maintenanceTotal) {
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setMsg("空对象");
            return commonResult;
        }
        // 获取维保记录数据
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceTotalId(total.getId());
        List<Maintenance> maintenances = maintenanceService.findList(maintenance);
        if (maintenances.size() <= 0) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "空列表");
        }
        // 获取并填充维修内容数据
        maintenances.forEach(m -> {
            MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
            MaintenanceType maintenanceType = new MaintenanceType();
            maintenanceType.setMaintenanceId(m.getId());
            maintenanceRecord.setMaintenance(maintenanceDao.findMaintenanceDetail(m));
            maintenanceRecord.setOutsideAnalyzeRepairRecords(maintenanceTypeDao.findRepairRecords(maintenanceType, "1"));
            maintenanceRecord.setComponentAnalyzeRepairRecords(maintenanceTypeDao.findRepairRecords(maintenanceType, "2"));
            maintenanceRecord.setNormalRepairRecords(maintenanceTypeDao.findRepairRecords(maintenanceType, "3"));
            maintenanceRecords.add(maintenanceRecord);
        });
        // 填充VO对象数据
        maintenanceInfoVO.setCarModel(maintenanceTotal.getModelName());
        maintenanceInfoVO.setVin(maintenanceTotal.getVinCode());
        maintenanceInfoVO.setMaintenanceRecords(maintenanceRecords);
        commonResult.setData(maintenanceInfoVO);
        return commonResult;
    }
}