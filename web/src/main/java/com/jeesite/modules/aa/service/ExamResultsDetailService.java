/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;
import java.util.Map;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.IdentifyTecDetail;
import com.jeesite.modules.aa.entity.TechnologyInfo;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.ExamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.ExamResultsDetail;
import com.jeesite.modules.aa.dao.ExamResultsDetailDao;

/**
 * 学生成绩详情表Service
 * @author liangtao
 * @version 2019-07-22
 */
@Service
@Transactional(readOnly=true)
public class ExamResultsDetailService extends CrudService<ExamResultsDetailDao, ExamResultsDetail> {

	@Autowired
	private ExamUserService examUserService;
	@Autowired
	private ExamService examService;

	/**
	 * 获取单条数据
	 * @param examResultsDetail
	 * @return
	 */
	@Override
	public ExamResultsDetail get(ExamResultsDetail examResultsDetail) {
		return super.get(examResultsDetail);
	}

	/**
	 * 查询分页数据
	 * @param examResultsDetail 查询条件
	 * @param examResultsDetail.page 分页对象
	 * @return
	 */
	@Override
	public Page<ExamResultsDetail> findPage(ExamResultsDetail examResultsDetail) {
		return super.findPage(examResultsDetail);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param examResultsDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ExamResultsDetail examResultsDetail) {
		super.save(examResultsDetail);
	}
	
	/**
	 * 更新状态
	 * @param examResultsDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ExamResultsDetail examResultsDetail) {
		super.updateStatus(examResultsDetail);
	}
	
	/**
	 * 删除数据
	 * @param examResultsDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ExamResultsDetail examResultsDetail) {
		super.delete(examResultsDetail);
	}

	/**
	 *  不得分项
	 */
	@Transactional(readOnly=false)
	public void saveExamResults(ExamUser examUser) {
		//考生id
		String examUserId = examUser.getId();
		//考试id
		String examId = examUser.getExamId();
		//试卷id
        Exam exam = new Exam();
		exam.setId(examUser.getExamId());
		exam  = examService.getByEntity(exam);
		List<TechnologyInfo> identifyTecInfoList =  dao.getExamResults(examUserId,null);
		String examScoreClassifyId = examUserService.getScoreClassifyId(examUser.getExamId(),"1151028180616400897"); //鉴定技术项 exam_score_info_id
		for(TechnologyInfo technologyInfo:identifyTecInfoList){
			ExamResultsDetail examResultsDetail = new ExamResultsDetail();
			examResultsDetail.setExamUserId(examUserId);
			examResultsDetail.setScoreClassifyId(examScoreClassifyId);
			examResultsDetail.setScorePoints(technologyInfo.getName());
			for(IdentifyTecDetail identifyTecDetail:technologyInfo.getItemList()){
				     //如果内容不为空 并且等于paperid 则为老师所填写内容
				if(StringUtils.isNotBlank(identifyTecDetail.getStuOrTec()) && exam.getPaperId().equals(identifyTecDetail.getStuOrTec())){
					if("0".equals(identifyTecDetail.getDeductNum())){
						examResultsDetail.setTeacherAnswer(identifyTecDetail.getCode()+",不扣分");
					}else if(!"".equals(identifyTecDetail.getDeductNum()) || null!=identifyTecDetail.getDeductNum()){
						examResultsDetail.setTeacherAnswer(identifyTecDetail.getCode()+"，扣"+identifyTecDetail.getDeductNum()+"分");
					}
					//如果内容不为空 并且等于examUserId 则为老师所填写内容
				}else if(StringUtils.isNotBlank(identifyTecDetail.getStuOrTec()) && examUser.getId().equals(identifyTecDetail.getStuOrTec())){
					if("0".equals(identifyTecDetail.getDeductNum())){
						examResultsDetail.setStudentAnswer(identifyTecDetail.getCode()+",不扣分");
					}else if(!"".equals(identifyTecDetail.getDeductNum()) || null!=identifyTecDetail.getDeductNum()){
						examResultsDetail.setStudentAnswer(identifyTecDetail.getCode()+"，扣"+identifyTecDetail.getDeductNum()+"分");
					}
				}
			}
			//保存
			super.save(examResultsDetail);
		}
	}





}