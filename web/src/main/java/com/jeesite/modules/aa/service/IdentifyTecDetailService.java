/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.ExamDetail;
import com.jeesite.modules.aa.entity.IdentifyTec;
import com.jeesite.modules.aa.entity.TechnologyInfo;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.IdentifyTecDetail;
import com.jeesite.modules.aa.dao.IdentifyTecDetailDao;

/**
 * 鉴定技术状况详情Service
 *
 * @author lvchangwei
 * @version 2019-07-04
 */
@Service
@Transactional(readOnly = true)
public class IdentifyTecDetailService extends CrudService<IdentifyTecDetailDao, IdentifyTecDetail> {

    @Autowired
    private IdentifyTecDetailDao identifyTecDetailDao;

    @Autowired
    private ExamDetailService examDetailService;

    @Autowired
    private ExamService examService;

    @Autowired
    private TechnologyInfoService technologyInfoService;

    /**
     * 获取单条数据
     *
     * @param identifyTecDetail
     * @return
     */
    @Override
    public IdentifyTecDetail get(IdentifyTecDetail identifyTecDetail) {
        return super.get(identifyTecDetail);
    }

    /**
     * 查询分页数据
     *
     * @param identifyTecDetail      查询条件
     * @param identifyTecDetail.page 分页对象
     * @return
     */
    @Override
    public Page<IdentifyTecDetail> findPage(IdentifyTecDetail identifyTecDetail) {
        return super.findPage(identifyTecDetail);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param identifyTecDetail
     */
    @Override
    @Transactional(readOnly = false)
    public void save(IdentifyTecDetail identifyTecDetail) {
        super.save(identifyTecDetail);
    }

    /**
     * 更新状态
     *
     * @param identifyTecDetail
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(IdentifyTecDetail identifyTecDetail) {
        super.updateStatus(identifyTecDetail);
    }

    /**
     * 删除数据
     *
     * @param identifyTecDetail
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(IdentifyTecDetail identifyTecDetail) {
        super.delete(identifyTecDetail);
    }

    public IdentifyTec findData(IdentifyTec identifyTec, ExamUser examUser) {
        String type = identifyTec.getType();
        //查询启用项
        ExamDetail examDetail = new ExamDetail();
        //路试项
        boolean isDel = false;
        if ("7".equals(type)) {
            //考生
            if (StringUtils.isNotBlank(examUser.getExamId())) {
                examDetail.setExamId(examUser.getExamId());
                examDetail = examDetailService.getByEntity(examDetail);
                //未查到或者为启用，均查询学生答案，否则查询正确答案
                if (null == examDetail || "1".equals(examDetail.getEnableCheckBodySkeleton())) {
                    identifyTec.setExamUserId(examUser.getId());
                } else {
                    Exam exam = examService.get(examUser.getExamId());
                    if (null != exam) {
                        identifyTec.setPaperId(exam.getPaperId());
                    }
                    isDel = true;
                }
            } else {
                //教师
                identifyTec.setPaperId(examUser.getPaperId());
            }
        } else {
            identifyTec.setExamUserId(examUser.getId());
            identifyTec.setPaperId(examUser.getPaperId());
        }

        //查询考生或教师已作答过的题目
        identifyTec = identifyTecDetailDao.findData(identifyTec);
        if (isDel) {
            identifyTec.setId(null);
        }
        Map<String, IdentifyTecDetail> map = new HashMap<>();
        if (null != identifyTec) {
            List<IdentifyTecDetail> detailList = identifyTec.getItemList();
            for (IdentifyTecDetail detail : detailList) {
                if ("正常".equals(detail.getCode()) || "无".equals(detail.getCode()) || "是".equals(detail.getCode())) {
                    detail.setCheckRes("正常");
                } else {
                    detail.setCheckRes("有缺陷");
                }
                if (isDel) {
                    detail.setId(null);
                }
                map.put(detail.getTypeName(), detail);
            }
            //外包接口需要、有点傻逼
            identifyTec.setItemList(null);
        } else {
            identifyTec = new IdentifyTec();
        }
        identifyTec.setExamDetail(examDetail);

        //查询全部节点的id及对应名字
        TechnologyInfo technologyInfo = new TechnologyInfo();
        technologyInfo.setType(type);
        List<TechnologyInfo> infoList = technologyInfoService.findList(technologyInfo);
        for (TechnologyInfo info : infoList) {
            String key = info.getName();
            if (!map.containsKey(key)) {
                IdentifyTecDetail detail = new IdentifyTecDetail();
                detail.setTechnologyInfoId(info.getId());
                detail.setTypeName(info.getName());
                map.put(info.getName(), detail);
            }
        }
        identifyTec.setItemMap(map);
        return identifyTec;
    }

    /**
     * 鉴定技术状况 - 二手车鉴定评估作业表
     *
     * @param examUser
     * @return
     */
    public List<IdentifyTec> findIdentityTecCondition(IdentifyTec identifyTec) {
        return identifyTecDetailDao.findIdentityTecCondition(identifyTec);
    }
}