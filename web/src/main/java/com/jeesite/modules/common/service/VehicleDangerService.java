/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.dao.VehicleDangerDao;
import com.jeesite.modules.common.dao.VehicleDangerTotalDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.VehicleDanger;
import com.jeesite.modules.common.entity.VehicleDangerDetail;
import com.jeesite.modules.common.entity.VehicleDangerTotal;
import com.jeesite.modules.common.vo.VehicleDangerInfoVO;
import com.jeesite.modules.common.vo.VehicleDangerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆出险记录表Service
 *
 * @author liangtao
 * @version 2019-07-12
 */
@Service
@Transactional(readOnly = true)
public class VehicleDangerService extends CrudService<VehicleDangerDao, VehicleDanger> {

    @Autowired
    private VehicleDangerTotalService vehicleDangerTotalService;

    @Autowired
    private VehicleDangerDetailService vehicleDangerDetailService;

    @Autowired
    private VehicleDangerTotalDao vehicleDangerTotalDao;

    /**
     * 获取单条数据
     *
     * @param vehicleDanger
     * @return
     */
    @Override
    public VehicleDanger get(VehicleDanger vehicleDanger) {
        return super.get(vehicleDanger);
    }

    /**
     * 查询分页数据
     *
     * @param vehicleDanger 查询条件
     * @return
     */
    @Override
    public Page<VehicleDanger> findPage(VehicleDanger vehicleDanger) {
        return super.findPage(vehicleDanger);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param vehicleDanger
     */
    @Override
    @Transactional(readOnly = false)
    public void save(VehicleDanger vehicleDanger) {
        super.save(vehicleDanger);
    }

    /**
     * 更新状态
     *
     * @param vehicleDanger
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(VehicleDanger vehicleDanger) {
        super.updateStatus(vehicleDanger);
    }

    /**
     * 删除数据
     *
     * @param vehicleDanger
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(VehicleDanger vehicleDanger) {
        super.delete(vehicleDanger);
    }

    /**
     * @description: 加载出险记录
     * @param: [vehicleDangerDetail]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/4
     * @time: 14:50
     */
    public CommonResult findVehicleDanger(VehicleDangerTotal vehicleDangerTotal) {
        CommonResult commonResult = new CommonResult();
        VehicleDanger vehicleDanger = new VehicleDanger();
        String vinCode = null;
        if (StringUtils.isBlank(vehicleDangerTotal.getId()) && StringUtils.isNotBlank(vehicleDangerTotal.getVinCode())) {
            vinCode = vehicleDangerTotal.getVinCode();
            // 学生端调用查询出险记录 根据VIN码 获取总表id
            VehicleDangerTotal totalStu = vehicleDangerTotalDao.getByEntity(vehicleDangerTotal);
            if (null == totalStu) {
                return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "空对象");
            }
            vehicleDanger.setCommonVehicleDangerTotalId(totalStu.getId());
        } else {
            VehicleDangerTotal totalTea = vehicleDangerTotalService.get(vehicleDangerTotal.getId());
            if (null == totalTea) {
                return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "空对象");
            }
            vinCode = totalTea.getVinCode();
            vehicleDanger.setCommonVehicleDangerTotalId(vehicleDangerTotal.getId());
        }
        List<VehicleDanger> vehicleDangers = dao.findList(vehicleDanger);
        if (vehicleDangers.size() <= 0) {
            return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "空列表");
        }
        for (VehicleDanger danger : vehicleDangers) {
            if (StringUtils.isBlank(danger.getVin())) {
                danger.setVin(vinCode);
            }
        }
        commonResult.setData(vehicleDangers);
        return commonResult;
    }

    /**
     * @description: 保存出险记录
     * @param: [vehicleDangerInfoVO]
     * @return: void
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 9:39
     */
    @Transactional(readOnly = false)
    public void saveVehicleDanger(VehicleDangerTotal total, VehicleDangerInfoVO vehicleDangerInfoVO) {
        String totalId = null;
        // 获取vehicleDangerInfoVO中的出险记录列表
        List<VehicleDangerRecord> vehicleDangerRecords = vehicleDangerInfoVO.getVehicleDangerRecords();
        if (null == total) {
            // 出险记录总表对象 先存入VIN码和车辆类型 获取出险记录表总对象id
            VehicleDangerTotal vehicleDangerTotal = new VehicleDangerTotal();
            vehicleDangerTotal.setVinCode(vehicleDangerInfoVO.getVin());
            vehicleDangerTotal.setVehicleType(vehicleDangerInfoVO.getCarModel());
            vehicleDangerTotal.setServiceSumCount((long) vehicleDangerRecords.size());
            vehicleDangerTotalService.save(vehicleDangerTotal);
            totalId = vehicleDangerTotalService.get(vehicleDangerTotal).getId();
        } else {
            total.setVinCode(vehicleDangerInfoVO.getVin());
            total.setVehicleType(vehicleDangerInfoVO.getCarModel());
            total.setServiceSumCount((long) vehicleDangerRecords.size());
            vehicleDangerTotalService.update(total);
            totalId = total.getId();
            // 删除关联记录
            vehicleDangerTotalService.deleteVehicleDanger(vehicleDangerTotalService.get(total.getId()), false);
        }
        // 根据上述处理 获取总表最终Total对象
        VehicleDangerTotal vehicleDangerTotal = vehicleDangerTotalService.get(totalId);
        // 出险总金额计数器初始化
        Integer serviceSumMountCount = 0;
        for (VehicleDangerRecord vehicleDangerRecord : vehicleDangerRecords) {
            // 出险记录对象 属性填充
            VehicleDanger vehicleDanger = new VehicleDanger();
            vehicleDanger.setCommonVehicleDangerTotalId(totalId);
            vehicleDanger.setDangerDate(vehicleDangerRecord.getDangerDate());
            this.save(vehicleDanger);
            // 获取出险记录id
            String id = this.get(vehicleDanger).getId();
            // 维修金额计数器初始化
            Integer dangerSingleMoneyCount = 0;
            for (VehicleDangerDetail vehicleDangerDetail : vehicleDangerRecord.getVehicleDangerDetails()) {
                // 理赔记录对象 属性填充
                VehicleDangerDetail detail = new VehicleDangerDetail();
                detail.setCommonVehicleDangerId(id);
                detail.setDangerSingleType(vehicleDangerDetail.getDangerSingleType());
                detail.setDangerSingleName(vehicleDangerDetail.getDangerSingleName());
                detail.setDangerSingleMoney(vehicleDangerDetail.getDangerSingleMoney());
                dangerSingleMoneyCount += Integer.parseInt(vehicleDangerDetail.getDangerSingleMoney());
                vehicleDangerDetailService.save(detail);
            }
            // 添加维修金额 更新出险记录
            vehicleDanger.setServiceMoney(dangerSingleMoneyCount.toString());
            this.update(vehicleDanger);
            // 计算出险总金额
            serviceSumMountCount += dangerSingleMoneyCount;
        }
        // 添加出险总金额 更新出险总记录
        vehicleDangerTotal.setServiceSumMoney(serviceSumMountCount.toString());
        vehicleDangerTotalService.update(vehicleDangerTotal);
    }
}