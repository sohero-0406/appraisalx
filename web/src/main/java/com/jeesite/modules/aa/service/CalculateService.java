/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.CalculateDao;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.vo.CalculateVO;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.entity.VehicleInfo;
import com.jeesite.modules.common.service.VehicleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 计算车辆价值Service
 *
 * @author chenlitao
 * @version 2019-07-04
 */
@Service
@Transactional(readOnly = true)
public class CalculateService extends CrudService<CalculateDao, Calculate> {

    @Autowired
    private CalculateDao calculateDao;
    @Autowired
    private CalculateDepreciationService calculateDepreciationService;
    @Autowired
    private CalculateReplaceCostService calculateReplaceCostService;
    @Autowired
    private CalculateKmService calculateKmService;
    @Autowired
    private CalculateCurrentService calculateCurrentService;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private VehicleInfoService vehicleInfoService;

    /**
     * 获取单条数据
     *
     * @param calculate
     * @return
     */
    @Override
    public Calculate get(Calculate calculate) {
        return super.get(calculate);
    }

    /**
     * 查询分页数据
     *
     * @param calculate 查询条件
     * @return
     */
    @Override
    public Page<Calculate> findPage(Calculate calculate) {
        return super.findPage(calculate);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param calculate
     */
    @Override
    @Transactional(readOnly = false)
    public void save(Calculate calculate) {
        super.save(calculate);
    }

    /**
     * 更新状态
     *
     * @param calculate
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Calculate calculate) {
        super.updateStatus(calculate);
    }

    /**
     * 删除数据
     *
     * @param calculate
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Calculate calculate) {
        super.delete(calculate);
    }

    /**
     * 获取车辆评估价值算法
     *
     * @param examUser
     * @return
     */
    public CalculateVO getCalculate(ExamUser examUser) {
        CalculateVO vo = new CalculateVO();

        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        if (null != carInfo) {
            VehicleInfo vehicleInfo = vehicleInfoService.getCarModel(carInfo.getModel());
            if (null != vehicleInfo) {
                vo.setNewCarPrice(vehicleInfo.getChangshangzhidaojiaYuan());
            }
        }

        Calculate calculate = new Calculate();
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        calculate = this.getByEntity(calculate);
        vo.setCalculate(calculate);

        CalculateDepreciation calculateDepreciation = new CalculateDepreciation();
        CalculateKm calculateKm = new CalculateKm();
        CalculateReplaceCost calculateReplaceCost = new CalculateReplaceCost();
        CalculateCurrent calculateCurrent = new CalculateCurrent();
        switch (calculate.getType()) {
            case "1"://折旧率估值法
                calculateDepreciation.setCalculateId(calculate.getId());
                vo.setCalculateDepreciation(calculateDepreciationService.getByEntity(calculateDepreciation));
                break;
            case "2"://公里数估值法
                calculateKm.setCalculateId(calculate.getId());
                vo.setCalculateKm(calculateKmService.getByEntity(calculateKm));
                break;
            case "3"://重置成本法
                calculateReplaceCost.setCalculateId(calculate.getId());
                vo.setCalculateReplaceCost(calculateReplaceCostService.getByEntity(calculateReplaceCost));
                break;
            case "4"://现行市价法
                calculateCurrent.setCalculateId(calculate.getId());
                vo.setCalculateCurrent(calculateCurrentService.getByEntity(calculateCurrent));
                break;
        }
        return vo;
    }

    /**
     * 保存车辆评估价值算法
     *
     * @param vo
     * @param examUser
     */
    @Transactional
    public void saveCalculate(CalculateVO vo, ExamUser examUser) {
        Calculate calculate = vo.getCalculate();
        String type = calculate.getType();
        //现行市价法有被评估车辆，不能删除
        if (!"4".equals(type)) {
            this.deleteCalculate(examUser);
        }
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        this.save(calculate);
        String calculateId = calculate.getId();

        switch (calculate.getType()) {
            case "1"://折旧率估值法
                CalculateDepreciation calculateDepreciation = vo.getCalculateDepreciation();
                calculateDepreciation.setCalculateId(calculateId);
                calculateDepreciationService.save(calculateDepreciation);
                break;
            case "2"://公里数估值法
                CalculateKm calculateKm = vo.getCalculateKm();
                calculateKm.setCalculateId(calculateId);
                calculateKmService.save(calculateKm);
                break;
            case "3"://重置成本法
                CalculateReplaceCost calculateReplaceCost = vo.getCalculateReplaceCost();
                calculateReplaceCost.setCalculateId(calculateId);
                calculateReplaceCostService.save(calculateReplaceCost);
                break;
            case "4"://现行市价法
                CalculateCurrent calculateCurrent = vo.getCalculateCurrent();
                calculateCurrent.setCalculateId(calculateId);
                calculateCurrentService.save(calculateCurrent);
                break;
        }
    }

    public Calculate getByEntity(Calculate calculate) {
        return dao.getByEntity(calculate);
    }

    //依据学生的估算方式查找对应的估算值
    public Map<String, String> getEstimateByType(String examUserId,String paperId) {
        return dao.getEstimateByType(examUserId,paperId);
    }

    /**
     * 查询算法类型
     *
     * @param calculate
     */
    public String getType(Calculate calculate) {
        return calculateDao.getType(calculate);
    }

    /**
     * 删除之前算法存的数据
     *
     * @param examUser
     */
    @Transactional
    public Calculate deleteCalculate(ExamUser examUser) {
        Calculate calculate = new Calculate();
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        calculate = this.getByEntity(calculate);
        if (null != calculate) {
            String type = calculate.getType();
            String calculateId = calculate.getId();
            switch (type) {
                case "1"://折旧率估值法
                    CalculateDepreciation calculateDepreciation = new CalculateDepreciation();
                    calculateDepreciation.setCalculateId(calculateId);
                    calculateDepreciationService.phyDeleteByEntity(calculateDepreciation);
                    break;
                case "2"://公里数估值法
                    CalculateKm calculateKm = new CalculateKm();
                    calculateKm.setCalculateId(calculateId);
                    calculateKmService.phyDeleteByEntity(calculateKm);
                    break;
                case "3"://重置成本法
                    CalculateReplaceCost calculateReplaceCost = new CalculateReplaceCost();
                    calculateReplaceCost.setCalculateId(calculateId);
                    calculateReplaceCostService.phyDeleteByEntity(calculateReplaceCost);
                    break;
                case "4"://现行市价法
                    CalculateCurrent calculateCurrent = new CalculateCurrent();
                    calculateCurrent.setCalculateId(calculateId);
                    calculateCurrentService.phyDeleteByEntity(calculateCurrent);
                    break;
            }
            return calculate;
        }
        return new Calculate();
    }
}