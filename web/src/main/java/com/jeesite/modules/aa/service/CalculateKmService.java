/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.math.BigDecimal;
import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.common.entity.ExamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.CalculateKm;
import com.jeesite.modules.aa.dao.CalculateKmDao;

/**
 * 公里数估值法Service
 *
 * @author chenlitao
 * @version 2019-07-05
 */
@Service
@Transactional(readOnly = true)
public class CalculateKmService extends CrudService<CalculateKmDao, CalculateKm> {

    @Autowired
    private CarInfoService carInfoService;

    /**
     * 获取单条数据
     *
     * @param calculateKm
     * @return
     */
    @Override
    public CalculateKm get(CalculateKm calculateKm) {
        return super.get(calculateKm);
    }

    /**
     * 查询分页数据
     *
     * @param calculateKm      查询条件
     * @param calculateKm.page 分页对象
     * @return
     */
    @Override
    public Page<CalculateKm> findPage(CalculateKm calculateKm) {
        return super.findPage(calculateKm);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param calculateKm
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CalculateKm calculateKm) {
        super.save(calculateKm);
    }

    /**
     * 更新状态
     *
     * @param calculateKm
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CalculateKm calculateKm) {
        super.updateStatus(calculateKm);
    }

    /**
     * 删除数据
     *
     * @param calculateKm
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CalculateKm calculateKm) {
        super.delete(calculateKm);
    }

    public CalculateKm getByEntity(CalculateKm calculateKm) {
        return dao.getByEntity(calculateKm);
    }

    /**
     * 计算
     *
     * @param calculateKm
     * @param examUser
     * @return
     */
    public CalculateKm calculate(CalculateKm calculateKm, ExamUser examUser) {
        int[] valueRatios = {5, 4, 3, 2, 1};
        //折旧率之和
        BigDecimal valueRatio = new BigDecimal(0);
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        if (null != carInfo) {
            String mileage = carInfo.getMileage();
            if (StringUtils.isBlank(mileage)) {
                mileage = "0";
            }
            BigDecimal mileageBig = new BigDecimal(mileage);
            if (mileageBig.compareTo(new BigDecimal("300000")) >= 0) {
                calculateKm.setPrice((double) 0);
            } else {
                //计算已用全部里程的几段，取上整
                int para = mileageBig.divide(new BigDecimal("60000"), BigDecimal.ROUND_UP).intValue();
                for (int i = 0; i < para; i++) {
                    valueRatio = valueRatio.add(new BigDecimal(valueRatios[i]));
                }
                //计算剩余里程分别消耗车辆价值率之和
                valueRatio = new BigDecimal(15).subtract(valueRatio);
                BigDecimal salePrice = new BigDecimal(calculateKm.getSalePrice());
                BigDecimal price = salePrice.multiply(valueRatio)
                        .divide(new BigDecimal(15),2,BigDecimal.ROUND_HALF_UP)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                calculateKm.setPrice(price.doubleValue());
            }
        }
        return calculateKm;
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("300001");

//		BigDecimal b = new BigDecimal("60000");
        System.out.println(a.compareTo(new BigDecimal("300000")));
    }
}