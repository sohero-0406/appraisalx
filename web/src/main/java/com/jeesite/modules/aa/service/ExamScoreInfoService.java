/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.ExamScoreInfo;
import com.jeesite.modules.aa.dao.ExamScoreInfoDao;

/**
 * 考试评分点Service
 * @author liangtao
 * @version 2019-07-16
 */
@Service
@Transactional(readOnly=true)
public class ExamScoreInfoService extends CrudService<ExamScoreInfoDao, ExamScoreInfo> {

	@Autowired
	private ExamScoreDetailService examScoreDetailService;
	/**
	 * 获取单条数据
	 * @param examScoreInfo
	 * @return
	 */
	@Override
	public ExamScoreInfo get(ExamScoreInfo examScoreInfo) {
		return super.get(examScoreInfo);
	}
	
	/**
	 * 查询分页数据
	 * @param examScoreInfo 查询条件
	 * @param examScoreInfo.page 分页对象
	 * @return
	 */
	@Override
	public Page<ExamScoreInfo> findPage(ExamScoreInfo examScoreInfo) {
		return super.findPage(examScoreInfo);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param examScoreInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ExamScoreInfo examScoreInfo) {
		super.save(examScoreInfo);
	}
	
	/**
	 * 更新状态
	 * @param examScoreInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ExamScoreInfo examScoreInfo) {
		super.updateStatus(examScoreInfo);
	}
	
	/**
	 * 删除数据
	 * @param examScoreInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ExamScoreInfo examScoreInfo) {
		super.delete(examScoreInfo);
	}

	/**
	 * 获取所有考试分值项
	 */
	@Transactional(readOnly=false)
	public List<ExamScoreClassify> getExamScoreInfo(String examId) {
		//获取考试得分项列表
		//判断有无考试id（examId）如果不为空，调取试卷保存的分值项
		if(StringUtils.isNotBlank(examId)){
			return dao.getExamScoreInfoExist(examId);
		}else{
			//如果为空，则取默认分值项分数
			return dao.getExamScoreInfo();
		}
	}
	
}