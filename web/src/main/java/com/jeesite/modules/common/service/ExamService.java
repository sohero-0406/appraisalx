/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.ExamDetail;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import com.jeesite.modules.aa.service.ExamDetailService;
import com.jeesite.modules.aa.service.ExamScoreDetailService;
import com.jeesite.modules.aa.service.ExamScoreInfoService;
import com.jeesite.modules.aa.vo.ExamVO;
import com.jeesite.modules.common.dao.ExamDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * common_examService
 * @author lvchangwei
 * @version 2019-07-10
 */
@Service
@Transactional(readOnly=true)
public class ExamService extends CrudService<ExamDao, Exam> {

	@Autowired
	private ExamScoreDetailService examScoreDetailService;
	@Autowired
	private ExamDetailService examDetailService;
	@Autowired
	private ExamUserService examUserService;
	@Autowired
	private ExamScoreInfoService examScoreInfoService;

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
	 *  keyword 搜索关键字
	 */
	@Transactional(readOnly=false)
	public List<Exam> getExamInfo(String keyword) {
		return dao.getExamInfo(keyword);
	}

	/**
	 *  保存考试功能
	 */
	@Transactional(readOnly=false)
	public CommonResult saveExamInfo(ExamVO examVO, String examScoreJson) {
		CommonResult comRes = new CommonResult();
		Exam exam = examVO.getExam();
		if(null==exam){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
			comRes.setMsg("请输入考试信息");
			return comRes;
		}
		if(!StringUtils.isNotBlank(exam.getPaperId())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
			comRes.setMsg("请选择试卷");
			return comRes;
		}
		//内容模块选择不能为空
		if(null==examVO.getExamDetail()){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
			comRes.setMsg("内容模块选择不能为空");
			return comRes;
		}
        //分值设定不能为空
		if(!StringUtils.isNotBlank(examScoreJson)){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
			comRes.setMsg("分值设定不能为空");
			return comRes;
		}
		String id = exam.getId();
		//判断新建/修改
		if(StringUtils.isNotBlank(id)){
			//修改
			if(!StringUtils.isNotBlank(exam.getState())){ //判断考试状态不能为空
				comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
				comRes.setMsg("考试状态不能为空");
				return comRes;
			}
			if(!"1".equals(exam.getState())){ //除新建状态下的数据 其余不可修改
				comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
				comRes.setMsg("此状态下的考试不能修改");
				return comRes;
			}
		}else{
			examVO.getExam().setState("1");
		}
		super.save(exam);
		//获取考试id
		String examId = examVO.getExam().getId();
		//保存--内容模块选择
		examDetailService.saveExamInfoDetail(examId,examVO.getExamDetail());
		//判断如果为 修改评分项 则先清空数据，在进行保存
		if(StringUtils.isNotBlank(id)){
			//先删除分值项
			examScoreDetailService.deleteExamScoreInfo(examId);
		}
		//保存--分值设定
		examScoreDetailService.saveExamScoreInfo(examScoreJson,examId);
		return comRes;
	}

	//新建/修改考试
	@Transactional(readOnly=false)
	public CommonResult addOrUpdateExam(String examId){
		CommonResult comRes = new CommonResult();
		//考试评分项
		List<ExamScoreClassify> examScoreInfoList = examScoreInfoService.getExamScoreInfo(examId);
		//内容模板选择
		ExamDetail examDetail = examDetailService.getExamInfoDetail(examId);
		Exam exam = new Exam();
		if(StringUtils.isNotBlank(examId)){
			exam.setId(examId);
			exam = dao.getByEntity(exam);
		}
		Map<String,Object> returnMap = new HashMap();
		returnMap.put("examScoreJson",examScoreInfoList);
		returnMap.put("examDetail",examDetail);
		returnMap.put("exam",exam);
		comRes.setData(returnMap);
		return comRes;
	}

	//删除考试 (考试id，考试状态)
	@Transactional(readOnly=false)
	public CommonResult deleteExam(Exam exam){
		CommonResult comRes = new CommonResult();
		if(null==exam && StringUtils.isBlank(exam.getId())){
			comRes.setMsg("请先选择考试!");
			return comRes;
		}else if ("".equals(exam.getState()) || null==exam.getState() || (!"1".equals(exam.getState()))){
			comRes.setMsg("此状态下的考试不能被删除!");
			return comRes;
		}
		//删除考试
		dao.delete(exam);
		//删除 评分项
		examScoreDetailService.deleteExamScoreInfo(exam.getId());
		//删除内容模板
		examDetailService.deleteExamDetail(exam.getId());
		return comRes;
	};


	@Transactional(readOnly=false)
	public Exam getByEntity(Exam exam) {
		return dao.getByEntity(exam);
	}

	/**
	 *  修改考试状态
	 *   参数
	 *   考试id 和
	 *   试卷id paperId
	 */
	@Transactional(readOnly=false)
	public CommonResult updateExamSate(Exam exam){
		//考试状态 state '状态（1:未开始;3:考试中;5:未统计;7:已出分）
		CommonResult comRes = new CommonResult();
		Exam examUpdate = dao.getByEntity(exam);
		switch(exam.getState()){
			case "1" ://未开始
				examUpdate.setState("3");
				examUpdate.setStartTime(new Date()); //记录考试开始时间
				super.save(examUpdate);
				break;
			case "3" ://考试中
				examUpdate.setState("5");
				examUpdate.setEndTime(new Date());  //记录考试结束时
				super.save(examUpdate);
				examUserService.saveExamEndTime(examUpdate.getId());
				break;
			case "5" ://未统计
				examUpdate.setState("7");
				examUserService.gradePapers(examUpdate);
				super.save(examUpdate);
				break;
			case "7" ://已出分
				comRes.setMsg("考试结果已出！");
				break;
			default :
				comRes.setMsg("未查询到此时卷！");
		}
		return comRes;
	}


}