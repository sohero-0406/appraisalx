/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.CheckTradableVehicles;
import com.jeesite.modules.aa.dao.CheckTradableVehiclesDao;

/**
 * 检查可交易车辆Service
 *
 * @author lvchangwei
 * @version 2019-07-01
 */
@Service
@Transactional(readOnly = true)
public class CheckTradableVehiclesService extends CrudService<CheckTradableVehiclesDao, CheckTradableVehicles> {

    @Autowired
    private CheckTradableVehiclesDao checkTradableVehiclesDao;
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 获取单条数据
     *
     * @param checkTradableVehicles
     * @return
     */
    @Override
    public CheckTradableVehicles get(CheckTradableVehicles checkTradableVehicles) {
        return super.get(checkTradableVehicles);
    }

    /**
     * 查询分页数据
     *
     * @param checkTradableVehicles      查询条件
     * @return
     */
    @Override
    public Page<CheckTradableVehicles> findPage(CheckTradableVehicles checkTradableVehicles) {
        return super.findPage(checkTradableVehicles);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param checkTradableVehicles
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CheckTradableVehicles checkTradableVehicles) {
        super.save(checkTradableVehicles);
    }

    /**
     * 更新状态
     *
     * @param checkTradableVehicles
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CheckTradableVehicles checkTradableVehicles) {
        super.updateStatus(checkTradableVehicles);
    }

    /**
     * 删除数据
     *
     * @param checkTradableVehicles
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CheckTradableVehicles checkTradableVehicles) {
        super.delete(checkTradableVehicles);
    }

    public CheckTradableVehicles getByEntity(CheckTradableVehicles checkTradableVehicles) {
        return checkTradableVehiclesDao.getByEntity(checkTradableVehicles);
    }

    /**
     * 保存事故车判定信息
     * @param examUser
     * @param checkTradableVehicles
     */
    @Transactional
    public void saveIsAccident(ExamUser examUser, CheckTradableVehicles checkTradableVehicles) {
        checkTradableVehicles.setExamUserId(examUser.getId());
        checkTradableVehicles.setPaperId(examUser.getPaperId());
        checkTradableVehiclesDao.saveIsAccident(checkTradableVehicles);
        operationLogService.saveObj(examUser,"保存判定事故车数据成功");
    }

    /**
     * 保存检查可交易车辆
     *
     * @param examUser
     * @param checkTradableVehicles
     */
    @Transactional
    public void saveObj(ExamUser examUser, CheckTradableVehicles checkTradableVehicles) {
        checkTradableVehicles.setExamUserId(examUser.getId());
        checkTradableVehicles.setPaperId(examUser.getPaperId());
        this.save(checkTradableVehicles);
        operationLogService.saveObj(examUser, "保存可交易车辆判别信息成功");
    }
}