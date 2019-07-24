/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.MathUtils;
import com.jeesite.modules.aa.dao.CalculateReplaceCostDao;
import com.jeesite.modules.aa.entity.Calculate;
import com.jeesite.modules.aa.entity.CalculateReplaceCost;
import com.jeesite.modules.aa.entity.Tax;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 重置成本法Service
 *
 * @author chenlitao
 * @version 2019-07-05
 */
@Service
@Transactional(readOnly = true)
public class CalculateReplaceCostService extends CrudService<CalculateReplaceCostDao, CalculateReplaceCost> {

    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;

    @Autowired
    private ExamService examService;

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private TaxService taxService;

    /**
     * 获取单条数据
     *
     * @param calculateReplaceCost
     * @return
     */
    @Override
    public CalculateReplaceCost get(CalculateReplaceCost calculateReplaceCost) {
        return super.get(calculateReplaceCost);
    }

    /**
     * 查询分页数据
     *
     * @param calculateReplaceCost      查询条件
     * @return
     */
    @Override
    public Page<CalculateReplaceCost> findPage(CalculateReplaceCost calculateReplaceCost) {
        return super.findPage(calculateReplaceCost);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param calculateReplaceCost
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CalculateReplaceCost calculateReplaceCost) {
        super.save(calculateReplaceCost);
    }

    /**
     * 更新状态
     *
     * @param calculateReplaceCost
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CalculateReplaceCost calculateReplaceCost) {
        super.updateStatus(calculateReplaceCost);
    }

    /**
     * 删除数据
     *
     * @param calculateReplaceCost
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CalculateReplaceCost calculateReplaceCost) {
        super.delete(calculateReplaceCost);
    }

    public CalculateReplaceCost getByEntity(CalculateReplaceCost calculateReplaceCost) {
        return dao.getByEntity(calculateReplaceCost);
    }

    /**
     * 计算
     *
     * @param cost
     * @param examUser
     * @return
     */
    public CalculateReplaceCost calculate(CalculateReplaceCost cost, ExamUser examUser) {
        //新车销售价
        BigDecimal salePrice = new BigDecimal(cost.getSalePrice());
        // 牌照费
        BigDecimal licenseFee = new BigDecimal(0);
        //规定使用年限（月）
        BigDecimal provideUseMonth = new BigDecimal(0);
        //技术鉴定成新率系数
        BigDecimal tecNewRateCoefficient = new BigDecimal(0);

        //查询车辆技术状况分值
        VehicleGradeAssess assess = new VehicleGradeAssess();
        assess.setExamUserId(examUser.getId());
        assess.setPaperId(examUser.getPaperId());
        assess = vehicleGradeAssessService.getByEntity(assess);
        if (null == assess) {
            return cost;
        }
        BigDecimal score = new BigDecimal(assess.getScore());
        //考生
        if (StringUtils.isNotBlank(examUser.getExamId())) {
            //查询教师所用算法
            Calculate calculate = new Calculate();
            calculate.setType("3");
            Exam exam = examService.get(examUser.getExamId());
            if (null != exam) {
                calculate.setPaperId(exam.getPaperId());
            }
            calculate = calculateService.getByEntity(calculate);
            //教师用的不是该算法
            if (null == calculate) {
                return cost;
            }
            //查询教师答案
            CalculateReplaceCost teaCost = new CalculateReplaceCost();
            teaCost.setCalculateId(calculate.getId());
            teaCost = this.getByEntity(teaCost);
            if (null == teaCost) {
                return cost;
            }
            cost.setLicenseFee(teaCost.getLicenseFee());
            cost.setProvideUseYear(teaCost.getProvideUseYear());
            cost.setTecNewRateCoefficient(teaCost.getTecNewRateCoefficient());
            // 牌照费
            licenseFee = new BigDecimal(teaCost.getLicenseFee());
            //规定使用年限（月）
            provideUseMonth = new BigDecimal(teaCost.getProvideUseYear()).multiply(new BigDecimal(12));
            //技术鉴定成新率系数
            tecNewRateCoefficient = MathUtils.removePercent(teaCost.getTecNewRateCoefficient());
        } else {
            //教师
            licenseFee = new BigDecimal(cost.getLicenseFee());
            provideUseMonth = new BigDecimal(cost.getProvideUseYear()).multiply(new BigDecimal(12));
            tecNewRateCoefficient = MathUtils.removePercent(cost.getTecNewRateCoefficient());
        }
        //查询系统税率
        Tax tax = taxService.getByEntity(new Tax());
        if (tax == null) {
            return cost;
        }
        //车辆购置税税率
        BigDecimal purchase = MathUtils.removePercent(tax.getPurchase());
        //车辆增值税税率
        BigDecimal vat = MathUtils.removePercent(tax.getVat());

        //开始计算
        //车辆购置税
        BigDecimal purchaseTax = salePrice.divide(new BigDecimal(1).add(vat), 6, BigDecimal.ROUND_HALF_UP)
                .multiply(purchase).setScale(2, BigDecimal.ROUND_HALF_UP);
        return cost;
    }
}