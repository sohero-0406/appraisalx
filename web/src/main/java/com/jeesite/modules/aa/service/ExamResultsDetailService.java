/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.ExamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
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
	@Autowired
	private CheckBodySkeletonService checkBodySkeletonService;
	@Autowired
	private PictureUserService pictureUserService;


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
		//试卷id
        Exam exam = new Exam();
		exam.setId(examUser.getExamId());
		exam  = examService.getByEntity(exam);
		//检查车体骨架数据
		saveCheckBodySkeleton(exam,examUserId);
		//鉴定技术状况保存--（不得分项）
		saveIdentifyTecDetail(exam,examUserId);
	}

	//获取车架
	public void saveCheckBodySkeleton(Exam exam,String examUserId){

		String examScoreClassifyId = examUserService.getScoreClassifyId(exam.getId(),"1151028180615991297"); //鉴定技术项 exam_score_info_id
		//教师数据
		List<CheckBodySkeleton> checkBodySkeletonListTec = dao.getCheckBodySkeleton(null,exam.getPaperId());
		//学生数据
		List<CheckBodySkeleton> checkBodySkeletonListStu = dao.getCheckBodySkeleton(examUserId,null);
		//如果 老师未录满答案
			for(CheckBodySkeleton checkBodySkeletonTec:checkBodySkeletonListTec){
				ExamResultsDetail examResultsDetail = new ExamResultsDetail();
				examResultsDetail.setExamUserId(examUserId);
				examResultsDetail.setScoreClassifyId(examScoreClassifyId);
				examResultsDetail.setScorePoints(checkBodySkeletonTec.getProjectName());
				if(StringUtils.isNotBlank(checkBodySkeletonTec.getProject())){
					for(CheckBodySkeleton checkBodySkeletonStu:checkBodySkeletonListStu){
						//如果鉴定项相同
						if(checkBodySkeletonTec.getProject().equals(checkBodySkeletonStu.getProject())){
							examResultsDetail.setTeacherAnswer(this.getStateDescription(checkBodySkeletonTec.getState(),checkBodySkeletonTec.getDescription()));
							examResultsDetail.setStudentAnswer(this.getStateDescription(checkBodySkeletonTec.getState(),checkBodySkeletonTec.getDescription()));
							break;
						}
					}
				}
				super.save(examResultsDetail);
			}
	}

	/**
	 *  数据拼接
	 * @param state
	 * @param description
	 * @return
	 */
	public String getStateDescription(String state,String description){
		if(StringUtils.isNotBlank(description)){
			return state+","+description;
		}else{
			return state;
		}

	}


	/**
	 * @param exam 考试数据
	 * @param examUserId 考生id
	 */
	public void saveIdentifyTecDetail(Exam exam,String examUserId){
		List<TechnologyInfo> identifyTecInfoList =  dao.getExamResults(examUserId,exam.getPaperId());
		String examScoreClassifyId = examUserService.getScoreClassifyId(exam.getId(),"1151028180616400897"); //鉴定技术项 exam_score_info_id
		for(TechnologyInfo technologyInfo:identifyTecInfoList){
			ExamResultsDetail examResultsDetail = new ExamResultsDetail();
			examResultsDetail.setExamUserId(examUserId);
			examResultsDetail.setScoreClassifyId(examScoreClassifyId);
			examResultsDetail.setScorePoints(technologyInfo.getName());
			for(IdentifyTecDetail identifyTecDetail:technologyInfo.getItemList()){
				//如果内容不为空 并且等于paperid 则为老师所填写内容
				if(StringUtils.isNotBlank(identifyTecDetail.getStuOrTec()) && exam.getPaperId().equals(identifyTecDetail.getStuOrTec())){
					if("".equals(identifyTecDetail.getDeductNum()) && StringUtils.isNotBlank(identifyTecDetail.getCode())){
						examResultsDetail.setTeacherAnswer(identifyTecDetail.getCode());
					}else if(!"".equals(identifyTecDetail.getDeductNum()) && null!=identifyTecDetail.getDeductNum() && StringUtils.isNotBlank(identifyTecDetail.getCode())){
						examResultsDetail.setTeacherAnswer(identifyTecDetail.getCode()+"，"+identifyTecDetail.getDeductNum());
					}
					//老师没填项是否显示
					else{
						examResultsDetail.setTeacherAnswer("");
					}
					//如果内容不为空 并且等于examUserId 则为老师所填写内容
				}else if(StringUtils.isNotBlank(identifyTecDetail.getStuOrTec()) && examUserId.equals(identifyTecDetail.getStuOrTec())){
					if("".equals(identifyTecDetail.getDeductNum()) && StringUtils.isNotBlank(identifyTecDetail.getCode())){
						examResultsDetail.setStudentAnswer(identifyTecDetail.getCode());
					}else if(!"".equals(identifyTecDetail.getDeductNum()) && null!=identifyTecDetail.getDeductNum() && StringUtils.isNotBlank(identifyTecDetail.getCode())){
						examResultsDetail.setStudentAnswer(identifyTecDetail.getCode()+"，"+identifyTecDetail.getDeductNum());
					}
//					//老师没填项是否显示
					else{
						examResultsDetail.setStudentAnswer("");
					}
				}
			}
			//保存
			super.save(examResultsDetail);
		}

	}


	/**
	 * 检查 成绩详情是否包含不得分项
	 */
	public Boolean validationData(String examUserId){
		List<ExamResultsDetail> examResultsDetails = dao.validationData(examUserId);
		if(examResultsDetails.size()<2){
			//不包含不得分项
			return false;
		}else{
			//包含得分xiang
			return true;
		}
	};

	/**
	 *  查询不得分项数据
	 */
	public List<ExamScoreClassify> getExamResultsDetail(ExamUser examUser){
		return  dao.getExamResultsDetail(examUser);
	}

	public Map<String,Object> getExamResultsVehiclePicture(ExamUser examUser){
        Map<String,Object> returnMap = new HashMap<String,Object>();
		//学生
		String[] parentTypeIds = new String[]{"1143439093974253568"};//记录车辆基本信息
		List<PictureUser> pictureUserListStu = pictureUserService.findChildPicture(examUser, parentTypeIds);
		//老师
		ExamUser tecUser = new ExamUser();
		examUser = examUserService.get(examUser);
		Exam exam = new Exam();
		exam.setId(examUser.getExamId());
		exam = examService.get(exam);
		tecUser.setPaperId(exam.getPaperId());
		List<PictureUser> pictureUserListTec = pictureUserService.findChildPicture(tecUser, parentTypeIds);
		returnMap.put("pictureUserListStu",pictureUserListStu);
		returnMap.put("pictureUserListTec",pictureUserListTec);
		return returnMap;
	}

	public Object getExamResultsPictureByType(String examUserId,String pictureTypeId){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		//学生
		PictureUser pictureUserStu = new PictureUser();
		pictureUserStu.setExamUserId(examUserId);
		pictureUserStu.setPictureTypeId(pictureTypeId);
		List<PictureUser> pictureUserListStu = pictureUserService.findList(pictureUserStu);
		//老师
		ExamUser examUser = new ExamUser();
		examUser.setId(examUserId);
		examUser = examUserService.get(examUser);
		Exam exam = new Exam();
		exam.setId(examUser.getExamId());
		exam = examService.getByEntity(exam);
		PictureUser pictureUserTec = new PictureUser();
		pictureUserTec.setPictureTypeId(pictureTypeId);
		pictureUserTec.setPaperId(exam.getPaperId());
		List<PictureUser> pictureUserListTec = pictureUserService.findList(pictureUserTec);
		returnMap.put("pictureUserListStu",pictureUserListStu);
		returnMap.put("pictureUserListTec",pictureUserListTec);
		return returnMap;
	}

}