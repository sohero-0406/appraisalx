/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.modules.common.dao.VehicleDangerDao;
import com.jeesite.modules.common.dao.VehicleDangerDetailDao;
import com.jeesite.modules.common.entity.*;
import com.jeesite.modules.common.vo.VehicleDangerInfoVO;
import com.jeesite.modules.common.vo.VehicleDangerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.dao.VehicleDangerTotalDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆出险总表Service
 *
 * @author liangtao
 * @version 2019-07-12
 */
@Service
@Transactional(readOnly = true)
public class VehicleDangerTotalService extends CrudService<VehicleDangerTotalDao, VehicleDangerTotal> {

    @Autowired
    private VehicleDangerDetailService vehicleDangerDetailService;

    @Autowired
    private VehicleDangerService vehicleDangerService;

    @Autowired
    private VehicleDangerDao vehicleDangerDao;

    @Autowired
    private VehicleDangerDetailDao vehicleDangerDetailDao;

    /**
     * 获取单条数据
     *
     * @param vehicleDangerTotal
     * @return
     */
    @Override
    public VehicleDangerTotal get(VehicleDangerTotal vehicleDangerTotal) {
        return super.get(vehicleDangerTotal);
    }

    /**
     * 查询分页数据
     *
     * @param vehicleDangerTotal 查询条件
     * @return
     */
    @Override
    public Page<VehicleDangerTotal> findPage(VehicleDangerTotal vehicleDangerTotal) {
        return super.findPage(vehicleDangerTotal);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param vehicleDangerTotal
     */
    @Override
    @Transactional(readOnly = false)
    public void save(VehicleDangerTotal vehicleDangerTotal) {
        super.save(vehicleDangerTotal);
    }

    /**
     * 更新状态
     *
     * @param vehicleDangerTotal
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(VehicleDangerTotal vehicleDangerTotal) {
        super.updateStatus(vehicleDangerTotal);
    }

    /**
     * 删除数据
     *
     * @param vehicleDangerTotal
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(VehicleDangerTotal vehicleDangerTotal) {
        super.delete(vehicleDangerTotal);
    }

    /**
     * @description: 删除出险记录
     * @param: [vehicleDangerTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/6
     * @time: 17:32
     */
    @Transactional(readOnly = false)
    public CommonResult deleteVehicleDanger(VehicleDangerTotal total, boolean flag) {
        CommonResult commonResult = new CommonResult();
        try {
            // 获取出险总表以及其关联对象(列表)
            VehicleDangerTotal vehicleDangerTotal = this.get(total.getId());
            VehicleDanger vehicleDanger = new VehicleDanger();
            vehicleDanger.setCommonVehicleDangerTotalId(total.getId());
            List<VehicleDanger> vehicleDangers = vehicleDangerService.findList(vehicleDanger);
            List<VehicleDangerDetail> vehicleDangerDetails = vehicleDangerDetailService.findListByVehicleDangers(vehicleDangers);
            // 依次执行数据删除
            if (flag) {
                dao.phyDelete(vehicleDangerTotal);
            }
            vehicleDangers.forEach(x -> vehicleDangerDao.phyDelete(x));
            vehicleDangerDetails.forEach(x -> vehicleDangerDetailDao.phyDelete(x));
        } catch (Exception e) {
            commonResult.setMsg("空对象");
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setData(e.getMessage());
        }
        return commonResult;
    }

    /**
    * @description: 加载已保存出险记录(编辑时调用)
    * @param: [vehicleDangerTotal]
    * @return: com.jeesite.modules.common.entity.CommonResult
    * @author: Jiangyf
    * @date: 2019/8/7
    * @time: 14:10
    */
    public CommonResult findSavedVehicleDanger(VehicleDangerTotal total) {
        CommonResult commonResult = new CommonResult();
        VehicleDangerInfoVO vehicleDangerInfoVO = new VehicleDangerInfoVO();
        List<VehicleDangerRecord> vehicleDangerRecords = new ArrayList<>();
        // 填充传递参数id
        vehicleDangerInfoVO.setId(total.getId());
        // 获取出险总表数据
        VehicleDangerTotal vehicleDangerTotal = this.get(total.getId());
        if (null == vehicleDangerTotal) {
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setMsg("空对象");
            return commonResult;
        }
        // 获取出险记录数据
        VehicleDanger vehicleDanger = new VehicleDanger();
        vehicleDanger.setCommonVehicleDangerTotalId(total.getId());
        List<VehicleDanger> vehicleDangers = vehicleDangerService.findList(vehicleDanger);
        if (vehicleDangers.size() <= 0) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "空列表");
        }
        // 获取并填充理赔内容数据
        vehicleDangers.forEach(v -> {
            VehicleDangerRecord vehicleDangerRecord = new VehicleDangerRecord();
            vehicleDangerRecord.setDangerDate(v.getDangerDate());
            vehicleDangerRecord.setServiceMoney(v.getServiceMoney());
            vehicleDangerRecord.setVehicleDangerDetails(vehicleDangerDetailService.findVehicleDangerDetail(v));
            vehicleDangerRecords.add(vehicleDangerRecord);
        });
        // 填充VO对象数据
        vehicleDangerInfoVO.setVin(vehicleDangerTotal.getVinCode());
        vehicleDangerInfoVO.setCarModel(vehicleDangerTotal.getVehicleType());
        vehicleDangerInfoVO.setVehicleDangerRecords(vehicleDangerRecords);
        commonResult.setData(vehicleDangerInfoVO);
        return commonResult;
    }
}