/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.ReferenceDao;
import com.jeesite.modules.aa.entity.Reference;
import com.jeesite.modules.aa.vo.ReferenceVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 参照物表Service
 *
 * @author lvchangwei
 * @version 2019-07-16
 */
@Service
@Transactional(readOnly = true)
public class ReferenceService extends CrudService<ReferenceDao, Reference> {

    /**
     * 获取单条数据
     *
     * @param reference
     * @return
     */
    @Override
    public Reference get(Reference reference) {
        return super.get(reference);
    }

    /**
     * 查询分页数据
     *
     * @param reference 查询条件
     * @return
     */
    @Override
    public Page<Reference> findPage(Reference reference) {
        return super.findPage(reference);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param reference
     */
    @Override
    @Transactional(readOnly = false)
    public void save(Reference reference) {
        super.save(reference);
    }

    /**
     * 更新状态
     *
     * @param reference
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Reference reference) {
        super.updateStatus(reference);
    }

    /**
     * 删除数据
     *
     * @param reference
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Reference reference) {
        super.delete(reference);
    }

    /**
     * 初始化参照物
     * flag 1-修改 其他-查看
     */
    public CommonResult initReference(Reference reference, String flag) {

        if("1".equals(flag)){
            //判断是否被占用
            String[] referenceId = {reference.getId()};
            if (CollectionUtils.isNotEmpty(dao.selectReferenceExist(referenceId))) {
                return new CommonResult(CodeConstant.REQUEST_FAILED, "当前参照物已经被占用，不可进行编辑!");
            }
        }

        ReferenceVO referenceVO = new ReferenceVO();
        //加载车辆配置类型
        List<DictData> vehicleConfigurationTypeList = DictUtils.getDictList("aa_vehicle_configuration_type");
        referenceVO.setVehicleConfigurationTypeList(vehicleConfigurationTypeList);

        //加载发动机类型
        List<DictData> engineTypeList = DictUtils.getDictList("aa_engine_type");
        referenceVO.setEngineTypeList(engineTypeList);

        //加载变速箱类型
        List<DictData> gearboxTypeList = DictUtils.getDictList("aa_gearbox_type");
        referenceVO.setGearboxTypeList(gearboxTypeList);

        //加载环保标准
        List<DictData> environmentalStandardList = DictUtils.getDictList("aa_environmental_standard");
        referenceVO.setEnvironmentalStandardList(environmentalStandardList);

        //加载付款方式
        List<DictData> paymentMethodList = DictUtils.getDictList("aa_payment_method");
        referenceVO.setPaymentMethodList(paymentMethodList);

        referenceVO.setReference(this.get(reference));
        return new CommonResult(referenceVO);
    }

    /**
     * 删除 真删
     */
    @Transactional(readOnly = false)
    public CommonResult deleteReference(String referenceIdList) {
        String[] idList = referenceIdList.split(",");
        List<Reference> referenceList = dao.selectReferenceExist(idList);
        if (CollectionUtils.isNotEmpty(referenceList)) {
            StringBuilder builder = new StringBuilder();
            int len = referenceList.size();
            for (int i = 0; i < len; i++) {
                if (i < len - 1) {
                    builder.append(referenceList.get(i).getModel() + "已被试卷占用,");
                } else if (i == len - 1 && i < 4) {
                    builder.append(referenceList.get(i).getModel() + "已被试卷占用");
                } else {
                    builder.append("...");
                    break;
                }
            }
            return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER, builder.toString());
        }
        dao.deleteReference(idList);
        return new CommonResult();
    }


    /**
     * @description: 查询参照物列表
     * @param: [keyword]
     * @return: java.lang.Object
     * @author: Jiangyf
     * @date: 2019/8/12
     * @time: 10:41
     */
    public List<Reference> findReferenceList(Reference reference) {
        return dao.findReferenceList(reference);
    }
}