/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.MathUtils;
import com.jeesite.modules.aa.dao.CalculateCurrentDao;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.vo.CalculateCurrentVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 现行市价法Service
 *
 * @author lvchangwei
 * @version 2019-07-22
 */
@Service
@Transactional(readOnly = true)
public class CalculateCurrentService extends CrudService<CalculateCurrentDao, CalculateCurrent> {

    @Autowired
    private CalculateService calculateService;
    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 获取单条数据
     *
     * @param calculateCurrent
     * @return
     */
    @Override
    public CalculateCurrent get(CalculateCurrent calculateCurrent) {
        return super.get(calculateCurrent);
    }

    /**
     * 查询分页数据
     *
     * @param calculateCurrent      查询条件
     * @return
     */
    @Override
    public Page<CalculateCurrent> findPage(CalculateCurrent calculateCurrent) {
        return super.findPage(calculateCurrent);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param calculateCurrent
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CalculateCurrent calculateCurrent) {
        super.save(calculateCurrent);
    }

    /**
     * 更新状态
     *
     * @param calculateCurrent
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CalculateCurrent calculateCurrent) {
        super.updateStatus(calculateCurrent);
    }

    /**
     * 删除数据
     *
     * @param calculateCurrent
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CalculateCurrent calculateCurrent) {
        super.delete(calculateCurrent);
    }

    public CalculateCurrent getByEntity(CalculateCurrent calculateCurrent) {
        return dao.getByEntity(calculateCurrent);
    }

    /**
     * 计算
     *
     * @param calculateCurrent
     * @param examUser
     * @return
     */
    public CommonResult calculate(CalculateCurrent calculateCurrent, ExamUser examUser) {
        CommonResult commonResult = new CommonResult();
        //价格更有益的车辆0：参照车辆，1是评估车辆
        String p1Type = calculateCurrent.getP1Type();
        String p2Type = calculateCurrent.getP2Type();
        //参照物的优异价格
        BigDecimal p1ExcellentPrice = calculateCurrent.getP1ExcellentPrice();
        BigDecimal p2ExcellentPrice = calculateCurrent.getP2ExcellentPrice();

        if (StringUtils.isBlank(p1Type) || StringUtils.isBlank(p2Type) ||
                p1ExcellentPrice == null || p2ExcellentPrice == null) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数不全！");
        }

        Calculate calculate = new Calculate();
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        calculateCurrent = this.getByCalculate(calculate);
        if (null == calculateCurrent) {
            return new CommonResult(CodeConstant.REFERENCES_EVALUATED_INCOMPLETE, "请完善被评估车辆及参照物数据表！");
        }
        //是否计算成新率，1：是，0：否
        String isComputeNewRate = calculateCurrent.getIsComputeNewRate();
        //成新率
        BigDecimal newRate;
        if ("1".equals(isComputeNewRate)) {
            newRate = MathUtils.removePercent(calculateCurrent.getNewRate());
        } else {
            newRate = new BigDecimal(1);
        }
        //物价指数
        BigDecimal priceIndex = new BigDecimal(calculateCurrent.getPriceIndex());
        Reference p1 = referenceService.get(calculateCurrent.getParam1Id());
        Reference p2 = referenceService.get(calculateCurrent.getParam2Id());
        //参照物不存在
        if (p1 == null || p2 == null) {
            return new CommonResult(CodeConstant.REFERENCE_NOT_EXIST, "参照物不存在！");
        }
        //查询车辆技术状况分值
        VehicleGradeAssess assess = new VehicleGradeAssess();
        assess.setExamUserId(examUser.getId());
        assess.setPaperId(examUser.getPaperId());
        assess = vehicleGradeAssessService.getByEntity(assess);
        if (null == assess) {
            return commonResult;
        }
        //鉴定技术分值
        BigDecimal score = new BigDecimal(assess.getScore());

        //开始计算
        //计算参照物1的价格
        BigDecimal p1Price = new BigDecimal(p1.getCarPrice());  //p1价格
        BigDecimal p1NewRate;    //p1成新率
        BigDecimal p1PriceIndex = new BigDecimal(p1.getPriceIndex());   //p1物价指数
        BigDecimal p1Score = new BigDecimal(p1.getAppraisalScore());    //p1技术鉴定分值
        if ("1".equals(isComputeNewRate)) {
            p1NewRate = MathUtils.removePercent(p1.getNewRate());
        } else {
            p1NewRate = new BigDecimal(1);
        }

        //计算 p1 k1
        BigDecimal p1k1 = new BigDecimal(0);
        StringBuilder p1Process = new StringBuilder();
        String p1k1Symbol = "";
        if ("0".equals(p1Type)) {
            p1k1 = p1Price.subtract(p1ExcellentPrice.multiply(newRate)).divide(p1Price, 2, BigDecimal.ROUND_HALF_UP);
            p1k1Symbol = "-";
        }
        if ("1".equals(p1Type)) {
            p1k1 = p1Price.add(p1ExcellentPrice.multiply(newRate)).divide(p1Price, 2, BigDecimal.ROUND_HALF_UP);
            p1k1Symbol = "+";
        }
        calculateCurrent.setP1K1(p1k1);
        p1Process.append("k1=(参照物的价格" + p1k1Symbol + "结构性能差异值×被评估车辆成新率)/参照物的价格=(" +
                p1Price + p1k1Symbol + p1ExcellentPrice + "*" + newRate + ")/" + p1Price + "=" + p1k1 + ";");
        //计算p1 k2
        BigDecimal p1k2 = priceIndex.divide(p1PriceIndex, 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP1K2(p1k2);
        p1Process.append("k2=被评估车辆评估时的物价指数/参照车辆评估时的物价指数=" + priceIndex + "/" + p1PriceIndex +
                "=" + p1k2 + ";");
        //计算p1 k3
        BigDecimal p1k3 = p1Price.subtract(p1Price.multiply(newRate.subtract(p1NewRate)))
                .divide(p1Price, 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP1K3(p1k3);
        p1Process.append("k3=[参照物的价格-参照物的价格×(被评估车辆成新率-参照物成新率)]/参照物的价格=(" +
                p1Price + "-(" + p1Price + "*(" + newRate + "-" + p1NewRate + ")))/" + p1Price + "=" + p1k3 + ";");
        //计算p1 k4
        BigDecimal p1k4 = new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP1K4(p1k4);
        p1Process.append("k4=" + p1k4 + ";");
        //计算p1 评估值
        BigDecimal p1FinalPrice = p1Price.multiply(p1k1).multiply(p1k2).multiply(p1k3).multiply(p1k4)
                .multiply(score).divide(p1Score, 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP1Price(p1FinalPrice);
        p1Process.append("参照物I评估值=P×K=P×k1×k2×k3×k4×被评估车辆的鉴定技术分值/参照车辆的鉴定技术分值)=" +
                p1Price + "*" + p1k1 + "*" + p1k2 + "*" + p1k3 + "*" + p1k4 + "*" + score + "/" + p1Score + "=" +
                p1FinalPrice + "元");
        calculateCurrent.setP1Process(p1Process.toString());

        //计算参照物2的价格
        BigDecimal p2Price = new BigDecimal(p2.getCarPrice());  //p2价格
        BigDecimal p2NewRate;    //p2成新率
        BigDecimal p2PriceIndex = new BigDecimal(p2.getPriceIndex());   //p2物价指数
        BigDecimal p2Score = new BigDecimal(p2.getAppraisalScore());    //p2技术鉴定分值
        if ("1".equals(isComputeNewRate)) {
            p2NewRate = MathUtils.removePercent(p2.getNewRate());
        } else {
            p2NewRate = new BigDecimal(1);
        }
        //计算 p2 k1
        BigDecimal p2k1 = new BigDecimal(0);
        StringBuilder p2Process = new StringBuilder();
        String p2k1Symbol = "";
        if ("0".equals(p2Type)) {
            p2k1 = p2Price.subtract(p2ExcellentPrice.multiply(newRate)).divide(p2Price, 2, BigDecimal.ROUND_HALF_UP);
            p2k1Symbol = "-";
        }
        if ("1".equals(p2Type)) {
            p2k1 = p2Price.add(p2ExcellentPrice.multiply(newRate)).divide(p2Price, 2, BigDecimal.ROUND_HALF_UP);
            p2k1Symbol = "+";
        }
        calculateCurrent.setP2K1(p2k1);
        p2Process.append("k1=(参照物的价格" + p2k1Symbol + "结构性能差异值×被评估车辆成新率)/参照物的价格=(" +
                p2Price + p2k1Symbol + p2ExcellentPrice + "*" + newRate + ")/" + p2Price + "=" + p2k1 + ";");
        //计算p2 k2
        BigDecimal p2k2 = priceIndex.divide(p2PriceIndex, 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP2K2(p2k2);
        p2Process.append("k2=被评估车辆评估时的物价指数/参照车辆评估时的物价指数=" + priceIndex + "/" + p2PriceIndex +
                "=" + p2k2 + ";");
        //计算p2 k3
        BigDecimal p2k3 = p2Price.subtract(p2Price.multiply(newRate.subtract(p2NewRate)))
                .divide(p2Price, 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP2K3(p2k3);
        p2Process.append("k3=[参照物的价格-参照物的价格×(被评估车辆成新率-参照物成新率)]/参照物的价格=(" +
                p2Price + "-(" + p2Price + "*(" + newRate + "-" + p2NewRate + ")))/" + p2Price + "=" + p2k3 + ";");
        //计算p2 k4
        BigDecimal p2k4 = new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP2K4(p2k4);
        p2Process.append("k4=" + p2k4 + ";");
        //计算p2 评估值
        BigDecimal p2FinalPrice = p2Price.multiply(p2k1).multiply(p2k2).multiply(p2k3).multiply(p2k4)
                .multiply(score).divide(p2Score, 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setP2Price(p2FinalPrice);
        p2Process.append("参照物II评估值=P×K=P×k1×k2×k3×k4×被评估车辆的鉴定技术分值/参照车辆的鉴定技术分值)=" +
                p2Price + "*" + p2k1 + "*" + p2k2 + "*" + p2k3 + "*" + p2k4 + "*" + score + "/" + p2Score + "=" +
                p2FinalPrice + "元");
        calculateCurrent.setP2Process(p2Process.toString());

        //计算车辆的评估价格
        BigDecimal price = p1FinalPrice.add(p2FinalPrice).divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
        calculateCurrent.setPrice(price);
        String process = "评估车辆的评估价格=(参照物I评估值+参照物II评估值)/2=(" + p1FinalPrice + "+" + p2FinalPrice +
                ")/2=" + price + "元";
        calculateCurrent.setProcess(process);

        commonResult.setData(calculateCurrent);
        return commonResult;
    }

    /**
     * 根据计算类型，查询被评估车辆相关参数
     *
     * @param calculate
     * @return
     */
    private CalculateCurrent getByCalculate(Calculate calculate) {
        return dao.getByCalculate(calculate);
    }

    /**
     * 保存被评估车辆信息
     *
     * @param calculateCurrent
     * @param examUser
     */
    @Transactional
    public void saveVehiclesAssess(CalculateCurrent calculateCurrent, ExamUser examUser) {
        //删除其他算法的记录
        Calculate calculate = calculateService.deleteCalculate(examUser);
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        calculate.setType("4");
        calculateService.save(calculate);

        calculateCurrent.setCalculateId(calculate.getId());
        this.save(calculateCurrent);
    }

    public void phyDeleteByEntity(CalculateCurrent calculateCurrent) {
        dao.phyDeleteByEntity(calculateCurrent);
    }

    /**
     * 加载被评估车辆信息及参照物
     *
     * @return
     */
    public CalculateCurrentVO findVehiclesAssess(ExamUser examUser) {
        CalculateCurrentVO vo = new CalculateCurrentVO();
        Calculate calculate = new Calculate();
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        CalculateCurrent calculateCurrent = this.getByCalculate(calculate);
        if (null == calculateCurrent) {
            calculateCurrent = new CalculateCurrent();
        }
        //查询不在该算法中的值
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        if (carInfo == null) {
            return vo;
        }
        Map<String, String> map = new HashMap<>();
        map.put("chexingId", carInfo.getModel());
        CommonResult result = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, map);
        if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
            if(null!=result.getData()){
                JSONObject vehicleInfo = JSONObject.parseObject(result.getData().toString());
                calculateCurrent.setModel(vehicleInfo.getString("chexingmingcheng"));
            }
        }

        calculateCurrent.setDisplacement(carInfo.getDisplacement());
        calculateCurrent.setEnvironmentalStandard(DictUtils.getDictLabel("aa_environmental_standard",
                carInfo.getEnvironmentalStandard(), ""));
        calculateCurrent.setRegisterDate(carInfo.getRegisterDate());
        Integer useYear = carInfo.getUseYear();
        Integer useMonth = carInfo.getUseMonth();
        if (useYear == null) {
            useYear = 0;
        }
        if (useMonth == null) {
            useMonth = 0;
        }
        calculateCurrent.setUsedYear(useYear + "年" + useMonth + "个月");

        //查询技术鉴定分值
        VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
        vehicleGradeAssess.setExamUserId(examUser.getId());
        vehicleGradeAssess.setPaperId(examUser.getPaperId());
        vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
        if (null != vehicleGradeAssess) {
            calculateCurrent.setScore(vehicleGradeAssess.getScore());
            //交易时间 == 鉴定日期
            calculateCurrent.setTradeTime(vehicleGradeAssess.getIdentifyDate());
        }

        vo.setCalculateCurrent(calculateCurrent);
        String p1Id = calculateCurrent.getParam1Id();
        String p2Id = calculateCurrent.getParam2Id();
        if (StringUtils.isNotBlank(p1Id)) {
            vo.setReference1(referenceService.get(p1Id));
        }
        if (StringUtils.isNotBlank(p2Id)) {
           vo.setReference2(referenceService.get(p2Id));
        }
        vo.setReferenceList(referenceService.findList(new Reference()));
        //车辆配置类型
        List<DictData> vehicleConfigTypeList = DictUtils.getDictList("aa_vehicle_configuration_type");
        vo.setVehicleConfigTypeList(vehicleConfigTypeList);
        //发动机类别
        List<DictData> engineTypeList = DictUtils.getDictList("aa_engine_type");
        vo.setEngineTypeList(engineTypeList);
        //变速箱类型
        List<DictData> gearboxTypeList = DictUtils.getDictList("aa_gearbox_type");
        vo.setGearboxTypeList(gearboxTypeList);
        //付款方式
        List<DictData> paymentMethodList = DictUtils.getDictList("aa_payment_method");
        vo.setPaymentMethodList(paymentMethodList);
        //尾气排放标准
        List<DictData> exhaustEmissionStandardList = DictUtils.getDictList("aa_environmental_standard");
        vo.setExhaustEmissionStandardList(exhaustEmissionStandardList);
        return vo;
    }
}