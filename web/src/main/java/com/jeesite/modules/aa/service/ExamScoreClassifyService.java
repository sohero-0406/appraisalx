/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import com.jeesite.modules.aa.dao.ExamScoreClassifyDao;

/**
 * 考试评分分类总分数Service
 * @author liangtao
 * @version 2019-07-16
 */
@Service
@Transactional(readOnly=true)
public class ExamScoreClassifyService extends CrudService<ExamScoreClassifyDao, ExamScoreClassify> {
	
	/**
	 * 获取单条数据
	 * @param examScoreClassify
	 * @return
	 */
	@Override
	public ExamScoreClassify get(ExamScoreClassify examScoreClassify) {
		return super.get(examScoreClassify);
	}
	
	/**
	 * 查询分页数据
	 * @param examScoreClassify 查询条件
	 * @param examScoreClassify.page 分页对象
	 * @return
	 */
	@Override
	public Page<ExamScoreClassify> findPage(ExamScoreClassify examScoreClassify) {
		return super.findPage(examScoreClassify);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param examScoreClassify
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ExamScoreClassify examScoreClassify) {
		super.save(examScoreClassify);
	}
	
	/**
	 * 更新状态
	 * @param examScoreClassify
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ExamScoreClassify examScoreClassify) {
		super.updateStatus(examScoreClassify);
	}
	
	/**
	 * 删除数据
	 * @param examScoreClassify
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ExamScoreClassify examScoreClassify) {
		super.delete(examScoreClassify);
	}

	/**
	 *  依据考试id 获取试卷评分项
	 */
	@Transactional(readOnly=false)
	public List<Map<String,Object>> getExamScoreInfo(String examId) {
		return dao.getExamScoreInfo();
	}

	@Transactional(readOnly=false)
	public ExamScoreClassify getByEntity(ExamScoreClassify examScoreClassify) {
		return dao.getByEntity(examScoreClassify);
	}


}