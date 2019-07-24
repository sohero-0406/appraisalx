/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.service.ExamDetailService;
import com.jeesite.modules.aa.service.ExamScoreDetailService;
import com.jeesite.modules.aa.vo.ExamVO;
import com.jeesite.modules.common.dao.ExamDao;
import com.jeesite.modules.common.entity.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * common_examService
 * @author lvchangwei
 * @version 2019-07-10
 */
@Service
@Transactional(readOnly=true)
public class ExamService extends CrudService<ExamDao, Exam> {

	@Autowired
	private ExamDao examDao;
	@Autowired
	private ExamScoreDetailService examScoreDetailService;
	@Autowired
	private ExamDetailService examDetailService;

	/**
	 * 获取单条数据
	 * @param exam
	 * @return
	 */
	@Override
	public Exam get(Exam exam) {
		return super.get(exam);
	}
	
	/**
	 * 查询分页数据
	 * @param exam 查询条件
	 * @return
	 */
	@Override
	public Page<Exam> findPage(Exam exam) {
		return super.findPage(exam);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param exam
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Exam exam) {
		super.save(exam);
	}
	
	/**
	 * 更新状态
	 * @param exam
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Exam exam) {
		super.updateStatus(exam);
	}
	
	/**
	 * 删除数据
	 * @param exam
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Exam exam) {
		super.delete(exam);
	}

	/**
	 *  获取考试信息
	 */
	@Transactional(readOnly=false)
	public List<Exam> getExamInfo() {
		return dao.getExamInfo();
	}

	/**
	 *  新建考试功能
	 */
	@Transactional(readOnly=false)
	public void saveExamInfo(ExamVO examVO, String examScoreJson) {

		super.save(examVO.getExam());
		//获取考试id
		String examId = examVO.getExam().getId();
		//保存--分值设定
		examScoreDetailService.saveExamScoreInfo(examScoreJson,examId);
		//保存--内容模块选择
		examDetailService.saveExamInfoDetail(examId,examVO.getExamDetail());

	}

	/**
	 * 考试计时
	 */
    public Exam examTiming(Exam exam) {
		exam = examDao.getByEntity(exam);
		if(exam != null){
			if("1".equals(exam.getExamType())){
				//倒计时
				exam.setStartTime(null);
			}
		}
    	return exam;
    }

	@Transactional(readOnly=false)
	public Exam getByEntity(Exam exam) {
		return dao.getByEntity(exam);
	}

}