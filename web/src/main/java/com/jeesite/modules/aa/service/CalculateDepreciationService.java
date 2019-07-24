/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.math.BigDecimal;
import java.util.List;

import com.jeesite.common.idgen.IdWorker;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.common.entity.ExamUser;
import org.hyperic.sigar.cmd.MemWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.CalculateDepreciation;
import com.jeesite.modules.aa.dao.CalculateDepreciationDao;

/**
 * 折旧率估值法Service
 *
 * @author chenlitao
 * @version 2019-07-05
 */
@Service
@Transactional(readOnly = true)
public class CalculateDepreciationService extends CrudService<CalculateDepreciationDao, CalculateDepreciation> {

    @Autowired
    private CarInfoService carInfoService;


    /**
     * 获取单条数据
     *
     * @param calculateDepreciation
     * @return
     */
    @Override
    public CalculateDepreciation get(CalculateDepreciation calculateDepreciation) {
        return super.get(calculateDepreciation);
    }

    /**
     * 查询分页数据
     *
     * @param calculateDepreciation      查询条件
     * @param calculateDepreciation.page 分页对象
     * @return
     */
    @Override
    public Page<CalculateDepreciation> findPage(CalculateDepreciation calculateDepreciation) {
        return super.findPage(calculateDepreciation);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param calculateDepreciation
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CalculateDepreciation calculateDepreciation) {
        super.save(calculateDepreciation);
    }

    /**
     * 更新状态
     *
     * @param calculateDepreciation
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CalculateDepreciation calculateDepreciation) {
        super.updateStatus(calculateDepreciation);
    }

    /**
     * 删除数据
     *
     * @param calculateDepreciation
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CalculateDepreciation calculateDepreciation) {
        super.delete(calculateDepreciation);
    }

    public CalculateDepreciation getByEntity(CalculateDepreciation calculateDepreciation) {
        return dao.getByEntity(calculateDepreciation);
    }

    /**
     * 计算
     *
     * @param calculateDepreciation
     * @param examUser
     * @return
     */
    public CalculateDepreciation calculate(CalculateDepreciation calculateDepreciation, ExamUser examUser) {
        int[] depreciations = {15, 12, 10, 8, 7};
        //折旧率之和
        BigDecimal depreciation = new BigDecimal(0);
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        if (null != carInfo) {
            Integer useYear = carInfo.getUseYear();
            Integer useMonth = carInfo.getUseMonth();
            if (useYear == null) {
                useYear = 0;
            }
            if (useMonth == null) {
                useMonth = 0;
            }
            //取上整
            if (useMonth > 0) {
                useYear++;
            }
            //计算折旧率之和
            for (int i = 0; i < useYear; i++) {
                if (i < 5) {
                    depreciation = depreciation.add(new BigDecimal(depreciations[i]));
                } else {
                    depreciation = depreciation.add(new BigDecimal(5));
                }
            }
            calculateDepreciation.setUseYear(useYear);
            calculateDepreciation.setDepreciationRate(depreciation.toString());
            depreciation = depreciation.divide(new BigDecimal(100));
            BigDecimal salePrice = new BigDecimal(calculateDepreciation.getSalePrice())
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal price = salePrice.multiply(new BigDecimal(1).subtract(depreciation))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            calculateDepreciation.setPrice(price.doubleValue());
            String process = "评估价格=二手车价格=车辆销售价格×（1-年限折旧率之和）=" + salePrice.toString() + "*（1-"
                    + depreciation + ")=" + price.toString() + "元";
            calculateDepreciation.setProcess(process);
        }
        return calculateDepreciation;
    }
}