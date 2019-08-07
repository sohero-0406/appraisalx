/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import java.util.List;

import com.jeesite.modules.common.entity.VehicleDanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.entity.VehicleDangerDetail;
import com.jeesite.modules.common.dao.VehicleDangerDetailDao;

/**
 * 出险记录详情表Service
 *
 * @author liangtao
 * @version 2019-07-12
 */
@Service
@Transactional(readOnly = true)
public class VehicleDangerDetailService extends CrudService<VehicleDangerDetailDao, VehicleDangerDetail> {

    @Autowired
    private VehicleDangerDetailDao vehicleDangerDetailDao;

    /**
     * 获取单条数据
     *
     * @param vehicleDangerDetail
     * @return
     */
    @Override
    public VehicleDangerDetail get(VehicleDangerDetail vehicleDangerDetail) {
        return super.get(vehicleDangerDetail);
    }

    /**
     * 查询分页数据
     *
     * @param vehicleDangerDetail 查询条件
     * @return
     */
    @Override
    public Page<VehicleDangerDetail> findPage(VehicleDangerDetail vehicleDangerDetail) {
        return super.findPage(vehicleDangerDetail);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param vehicleDangerDetail
     */
    @Override
    @Transactional(readOnly = false)
    public void save(VehicleDangerDetail vehicleDangerDetail) {
        super.save(vehicleDangerDetail);
    }

    /**
     * 更新状态
     *
     * @param vehicleDangerDetail
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(VehicleDangerDetail vehicleDangerDetail) {
        super.updateStatus(vehicleDangerDetail);
    }

    /**
     * 删除数据
     *
     * @param vehicleDangerDetail
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(VehicleDangerDetail vehicleDangerDetail) {
        super.delete(vehicleDangerDetail);
    }

    /**
     * @description: 加载理赔记录
     * @param: [vehicleDanger]
     * @return: java.util.List<com.jeesite.modules.common.entity.VehicleDangerDetail>
     * @author: Jiangyf
     * @date: 2019/8/4
     * @time: 15:30
     */
    @Transactional(readOnly = true)
    public List<VehicleDangerDetail> findVehicleDangerDetail(VehicleDanger vehicleDanger) {
        String vehicleDangerId = vehicleDanger.getId();
        VehicleDangerDetail vehicleDangerDetail = new VehicleDangerDetail();
        vehicleDangerDetail.setCommonVehicleDangerId(vehicleDangerId);
        return vehicleDangerDetailDao.findVehicleDangerDetail(vehicleDangerDetail);
    }

    /** 
    * @description: 根据出险记录查询其关联理赔详情
    * @param: [vehicleDangers]
    * @return: java.util.List<com.jeesite.modules.common.entity.VehicleDangerDetail>
    * @author: Jiangyf
    * @date: 2019/8/6 
    * @time: 17:55
    */
    @Transactional(readOnly = true)
    public List<VehicleDangerDetail> findListByVehicleDangers(List<VehicleDanger> vehicleDangers) {
        List<VehicleDangerDetail> vehicleDangerDetailList = null;
        try {
           vehicleDangerDetailList = vehicleDangerDetailDao.findListByVehicleDangers(vehicleDangers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicleDangerDetailList;
    }
}