/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.service.*;
import com.jeesite.modules.common.dao.ExamUserDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * common_exam_userService
 * @author lvchangwei
 * @version 2019-06-27
 */
@Service
@Transactional(readOnly=true)
public class ExamUserService extends CrudService<ExamUserDao, ExamUser> {

	@Autowired
	private ExamUserDao examUserDao;
	@Autowired
	private ExamService examService;

	@Autowired
	private ExamScoreClassifyService examScoreClassifyService;

	// 委托方信息Service
	@Autowired
	private DelegateUserService delegateUserService;
	//委托车辆信息Service
	@Autowired
	private CarInfoService carInfoService;
	//车辆单证信息Service
	@Autowired
	private VehicleDocumentInfoService vehicleDocumentInfoService;
	//检查可交易车辆Service
	@Autowired
	private CheckTradableVehiclesService checkTradableVehiclesService;
	//车辆加装信息
	@Autowired
	private VehicleInstallInfoService vehicleInstallInfoService;
	//鉴定技术情况
	@Autowired
	private VehicleGradeAssessService vehicleGradeAssessService;
	//计算车辆价值Service
	@Autowired
	private CalculateService calculateService;
	//二手车鉴定评估报告Service
	@Autowired
	private AppraisalReportService appraisalReportService;
	//归档
	@Autowired
	private PlaceFileService placeFileService;
	//学生成绩详情表Service
	@Autowired
	private ExamResultsDetailService examResultsDetailService;


	/**
	 * 获取单条数据
	 * @param examUser
	 * @return
	 */
	@Override
	public ExamUser get(ExamUser examUser) {
		return super.get(examUser);
	}

	/**
	 * 查询分页数据
	 * @param examUser 查询条件
	 * @return
	 */
	@Override
	public Page<ExamUser> findPage(ExamUser examUser) {
		return super.findPage(examUser);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param examUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ExamUser examUser) {
		super.save(examUser);
	}

	/**
	 * 更新状态
	 * @param examUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ExamUser examUser) {
		super.updateStatus(examUser);
	}

	/**
	 * 删除数据
	 * @param examUser
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ExamUser examUser) {
		super.delete(examUser);
	}

	public ExamUser getByEntity(ExamUser examUser) {
		return dao.getByEntity(examUser);
	}


	//依据考试id和评分项id查询考试想分类id
	public String getScoreClassifyId(String examId,String scoreInfoId){
		ExamScoreClassify examScoreClassify = new ExamScoreClassify();
		examScoreClassify.setExamId(examId);
		examScoreClassify.setExamScoreInfoId(scoreInfoId);
		examScoreClassify = examScoreClassifyService.getByEntity(examScoreClassify);
		return examScoreClassify.getId();
	}

	/**
	 * 学生考试成绩详情接口
	 */
	public void saveExamDetail(String examUserId,String examId,String scoreInfoId,String scorePoints,
							   String score,String teacherAnswer,String studentAnswer,String righrOrWrong){
		ExamResultsDetail examResultsDetail = new ExamResultsDetail();
		examResultsDetail.setExamUserId(examUserId);
		examResultsDetail.setScoreClassifyId(getScoreClassifyId(examId,scoreInfoId));
		examResultsDetail.setScorePoints(scorePoints);
		examResultsDetail.setScore(score);
		examResultsDetail.setStudentAnswer(studentAnswer);
		examResultsDetail.setTeacherAnswer(teacherAnswer);
		examResultsDetail.setRightOrWrong(righrOrWrong);
		examResultsDetailService.save(examResultsDetail);
	}

	/**
	 *  判卷
	 */
	@Transactional(readOnly = false)
	public Object gradePapers() {
		//页面获取考试id  examId
		String examId = "1151435216635924480";
		String paperId = "1151374360001810432"; //页面获取数据

		//创建  分数项--分数
		Map<String, Object> examScoreMap = new HashMap<>();
		//创建  分数项--名称
		Map<String, Object> examNameMap = new HashMap<>();
		//依据考试 获取所有的评分项分类以及评分项
		List<Map<String, Object>> examScoreItems = examScoreClassifyService.getExamScoreInfo(examId);
		//处理评分项 封成一组map 将考试分值项
		for (Map<String, Object> score : examScoreItems) {
			examScoreMap.put((String) score.get("score_info_id"), score.get("score"));
			examNameMap.put((String) score.get("score_info_id"), score.get("name"));
		}

		DelegateUser delegateUserTec = new DelegateUser();  //委托人
		CarInfo carInfoTec = new CarInfo();                 //委托车辆
		VehicleDocumentInfo vehicleDocumentInfoTec = new VehicleDocumentInfo(); // 车辆单证信息
		CheckTradableVehicles checkTradableVehiclesTec = new CheckTradableVehicles(); //检查可交易车辆
		VehicleInstallInfo vehicleInstallInfoTec = new VehicleInstallInfo(); //车辆加装信息
		VehicleGradeAssess vehicleGradeAssessTec = new VehicleGradeAssess();
		Calculate calculateTec = new Calculate();//计算车辆价值
		AppraisalReport appraisalReportTec = new AppraisalReport();
		PlaceFile placeFileTec = new PlaceFile();


		delegateUserTec.setPaperId(paperId);
		carInfoTec.setPaperId(paperId);
		vehicleDocumentInfoTec.setPaperId(paperId);
		checkTradableVehiclesTec.setPaperId(paperId);
		vehicleInstallInfoTec.setPaperId(paperId);
		vehicleGradeAssessTec.setPaperId(paperId);
		calculateTec.setPaperId(paperId);
		appraisalReportTec.setPaperId(paperId);
		placeFileTec.setPaperId(paperId);


		//试卷 答案(老师)
		//一、委托方
		DelegateUser delegateUserT = delegateUserService.getByEntity(delegateUserTec);
		CarInfo carInfoT = carInfoService.getByEntity(carInfoTec);
		//二、将 判别可交易车辆内数据整理到Map内
		List<VehicleDocumentInfo> vehicleDocumentInfoListT = vehicleDocumentInfoService.findList(vehicleDocumentInfoTec);
		CheckTradableVehicles checkTradableVehiclesT = checkTradableVehiclesService.getByEntity(checkTradableVehiclesTec);
		//三、记录车辆基本信息
		List<VehicleInstallInfo> vehicleInstallInfoListT = vehicleInstallInfoService.findList(vehicleInstallInfoTec);
		//四、
		//五、鉴定技术状况
		VehicleGradeAssess vehicleGradeAssessT = vehicleGradeAssessService.getByEntity(vehicleGradeAssessTec);
		//六、估算汽车价值
		Calculate calculateT = calculateService.getByEntity(calculateTec);
		AppraisalReport appraisalReportT = appraisalReportService.getByEntity(appraisalReportTec);
		List<PlaceFile> placeFileListT = placeFileService.findList(placeFileTec); //归档



		//学生答题结果
		//根据考试获取 学生
		ExamUser examUser = new ExamUser();
		examUser.setExamId(examId);
		List<ExamUser> examUserList = dao.findList(examUser);
		//一、学生 --委托方
		DelegateUser delegateUserStu = new DelegateUser();
		CarInfo carInfoStu  = new CarInfo();
		//二、学生判别车辆
		VehicleDocumentInfo vehicleDocumentInfoStu = new VehicleDocumentInfo();
		CheckTradableVehicles checkTradableVehiclesStu = new CheckTradableVehicles();
		//三、记录车辆基本信息
		VehicleInstallInfo vehicleInstallInfoStu = new VehicleInstallInfo(); //车辆加装信息
		//四、判别事故车

		//五、鉴定技术状况
		VehicleGradeAssess vehicleGradeAssessStu = new VehicleGradeAssess();
		//六、估算价值
		Calculate calculateStu = new Calculate();//计算车辆价值
		AppraisalReport appraisalReportStu = new AppraisalReport();
		PlaceFile placeFileStu = new PlaceFile();




		for (ExamUser user : examUserList) {
			//一、委托人
			delegateUserStu.setExamUserId(user.getId());
			DelegateUser delegateUserS = delegateUserService.getByEntity(delegateUserStu);
			carInfoStu.setExamUserId(user.getId());
			CarInfo carInfoS = carInfoService.getByEntity(carInfoStu);
			BigDecimal delegateCount = getDelegateInfo(delegateUserS,carInfoS,delegateUserT,carInfoT,examScoreMap,user,examNameMap);

			//二、判别可交易车辆
			vehicleDocumentInfoStu.setExamUserId(user.getId());
			List<VehicleDocumentInfo> vehicleDocumentInfoListS = vehicleDocumentInfoService.findList(vehicleDocumentInfoStu);
			checkTradableVehiclesStu.setExamUserId(user.getId());
			CheckTradableVehicles checkTradableVehiclesS = checkTradableVehiclesService.getByEntity(checkTradableVehiclesStu);
			BigDecimal vehicleDocumentCount = getVehicleDocumentInfo(vehicleDocumentInfoListS,vehicleDocumentInfoListT,examScoreMap,checkTradableVehiclesS,checkTradableVehiclesT,user,examNameMap);

			//三、记录车辆基本信息
			vehicleInstallInfoStu.setExamUserId(user.getId());
			List<VehicleInstallInfo> vehicleInstallInfoListS = vehicleInstallInfoService.findList(vehicleInstallInfoStu);
			BigDecimal carInfoCount = getCarInfo(carInfoT,carInfoS,examScoreMap,vehicleInstallInfoListT,vehicleInstallInfoListS,user,examNameMap,paperId);

//			//四、判别事故车
			BigDecimal accidentCount = getAccidentVehicles(checkTradableVehiclesT,checkTradableVehiclesS,examScoreMap,user,examNameMap);
//			//五、鉴定技术状况
			vehicleGradeAssessStu.setExamUserId(user.getId());
			VehicleGradeAssess vehicleGradeAssessS = vehicleGradeAssessService.getByEntity(vehicleGradeAssessStu);
//			BigDecimal identificationCount = getIdentification(vehicleGradeAssessT,vehicleGradeAssessS,examScoreMap,user,examNameMap);
//			//六、估算价值
			appraisalReportStu.setExamUserId(user.getId());
			AppraisalReport appraisalReportS = appraisalReportService.getByEntity(appraisalReportStu);
			List<PlaceFile> placeFileListS =  placeFileService.findList(placeFileStu);
			BigDecimal calculateCount = getCalculate(calculateT,user,examScoreMap,checkTradableVehiclesT,checkTradableVehiclesS,
					appraisalReportT,appraisalReportS,placeFileListT,placeFileListS,examNameMap);

//			BigDecimal count = delegateCount.add(vehicleDocumentCount.add(carInfoCount));
//			user.setScore(String.valueOf(count));
//			super.save(user);
		}
		return null;
	}

	//六、估算价值
	/**
	 *
	 * @param calculateT  老师-估算价值
	 * @param user     考生
	 * @param examScoreMap  评分分数项
	 * @param checkTradableVehiclesT  检查可交易车辆-老师
	 * @param checkTradableVehiclesS  检查可交易车辆-学生
	 * @param appraisalReportT  二手车鉴定评估报告-老师
	 * @param appraisalReportS  二手车鉴定评估报告-学生
	 * @param placeFileListT 归档-老师
	 * @param placeFileListS 归档-学生
	 * @return
	 */
	public BigDecimal getCalculate(Calculate calculateT,ExamUser user,Map<String,Object> examScoreMap,
								   CheckTradableVehicles checkTradableVehiclesT,CheckTradableVehicles checkTradableVehiclesS,
								   AppraisalReport appraisalReportT,AppraisalReport appraisalReportS,
								   List<PlaceFile> placeFileListT,List<PlaceFile> placeFileListS,Map<String,Object> examNameMap){
		BigDecimal calculateCount = new BigDecimal("0");
		//学生估算方式
		String studentPrice ="";
		if(calculateService.getEstimateByType(user.getId())!=null){
			studentPrice = calculateService.getEstimateByType(user.getId()).get("price");
		}
		if(calculateT.getBeginPrice()!=null && calculateT.getEndPrice()!=null){
			if(judgeStringBetween(String.valueOf(calculateT.getBeginPrice()),String.valueOf(calculateT.getEndPrice()),studentPrice)){
				calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665733999"))));
				calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665739992"))));
				saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
						(String)examNameMap.get("1151013343665733999"),(String)examScoreMap.get("1151013343665733999"),
						calculateT.getBeginPrice()+"-"+calculateT.getEndPrice()+"元",
						studentPrice+"元","0");
				saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
						(String)examNameMap.get("1151013343665739992"),(String)examScoreMap.get("1151013343665739992"),
						calculateT.getBeginPrice()+"-"+calculateT.getEndPrice()+"元",
						studentPrice+"元","0");
			}else{
				saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
						(String)examNameMap.get("1151013343665733999"),"0",
						calculateT.getBeginPrice()+"-"+calculateT.getEndPrice()+"元",
						studentPrice+"元","1");
				saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
						(String)examNameMap.get("1151013343665739992"),"0",
						calculateT.getBeginPrice()+"-"+calculateT.getEndPrice()+"元",
						studentPrice+"元","1");
			}
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343665733999"),"0",
					calculateT.getBeginPrice()+"-"+calculateT.getEndPrice()+"元",
					studentPrice+"元","1");
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343665739992"),"0",
					calculateT.getBeginPrice()+"-"+calculateT.getEndPrice()+"元",
					studentPrice+"元","1");
		}

		//是否查封、抵押车辆
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getCheck3())&& checkTradableVehiclesT.getCheck3().equals(checkTradableVehiclesS.getCheck3()) ){
			calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663128577"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343663128577"),(String)examScoreMap.get("1151013343663128577"),
					checkTradableVehiclesT.getCheck3(),checkTradableVehiclesS.getCheck3(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343663128577"),"0",
					checkTradableVehiclesT.getCheck3(),checkTradableVehiclesS.getCheck3(),"1");
		}
		//未接受处理的交通违法记录：
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getTrafficIllegalRecord())&& checkTradableVehiclesT.getTrafficIllegalRecord().equals(checkTradableVehiclesS.getTrafficIllegalRecord())){
			calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343662706689"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343662706689"),(String)examScoreMap.get("1151013343662706689"),
					checkTradableVehiclesT.getTrafficIllegalRecord(),checkTradableVehiclesS.getTrafficIllegalRecord(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343662706689"),"0",
					checkTradableVehiclesT.getTrafficIllegalRecord(),checkTradableVehiclesS.getTrafficIllegalRecord(),"1");
		}
		//车辆鉴定评估价值为人民币（元）   1151013343665739992

		//自鉴定评估基准日至（日）
		if(StringUtils.isNotEmpty(appraisalReportT.getBaseDateEnd())&& appraisalReportT.getBaseDateEnd().equals(appraisalReportS.getBaseDateEnd()) ){
			calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665541121"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343665541121"),(String)examScoreMap.get("1151013343665541121"),
					appraisalReportT.getBaseDateEnd(),appraisalReportS.getBaseDateEnd(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615860225",
					(String)examNameMap.get("1151013343665541121"),"0",
					appraisalReportT.getBaseDateEnd(),appraisalReportS.getBaseDateEnd(),"1");
		}
		//归档



		return calculateCount;
	}




	//五、鉴定技术状况
	public BigDecimal getIdentification(VehicleGradeAssess vehicleGradeAssessT, VehicleGradeAssess vehicleGradeAssessS,
										Map<String,Object> examScoreMap, ExamUser user,Map<String,Object> examNameMap){
		BigDecimal identificationCount = new BigDecimal(0);
		//老师答案不为空
		if(this.judgeStringBetween(vehicleGradeAssessT.getStartScore(),vehicleGradeAssessT.getEndScore(),vehicleGradeAssessS.getScore())){
			identificationCount = identificationCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343662993409")))); //车辆鉴定得分值（区间值）
			saveExamDetail(user.getId(),user.getExamId(),"1151028180616400897",
					(String)examNameMap.get("1151013343662993409"),(String)examScoreMap.get("1151013343662993409"),
					vehicleGradeAssessT.getStartScore()+"-"+vehicleGradeAssessT.getEndScore()+"分",
					vehicleGradeAssessS.getScore()+"分","0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180616400897",
					(String)examNameMap.get("1151013343662993409"),"0",
					vehicleGradeAssessT.getStartScore()+"-"+vehicleGradeAssessT.getEndScore()+"分",
					vehicleGradeAssessS.getScore()+"分","1");

		}
		//鉴定日期
		if(StringUtils.isNotBlank(vehicleGradeAssessT.getIdentifyDate()) && vehicleGradeAssessT.getIdentifyDate().equals(vehicleGradeAssessS.getIdentifyDate()) ){
			identificationCount = identificationCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151028180615864562")))); //鉴定日期
			saveExamDetail(user.getId(),user.getExamId(),"1151028180616400897",
					(String)examNameMap.get("1151028180615864562"),(String)examScoreMap.get("1151028180615864562"),
					vehicleGradeAssessT.getIdentifyDate(),vehicleGradeAssessS.getIdentifyDate(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180616400897",
					(String)examNameMap.get("1151028180615864562"),"0",
					vehicleGradeAssessT.getIdentifyDate(),vehicleGradeAssessS.getIdentifyDate(),"1");
		}
		return identificationCount;
	}



	//判断 某字符串类型 在某字符串类型区间 并判断是或否为空
	//中间值 大于等于开始值 小于等于结束值
	public Boolean judgeStringBetween(String begin,String end,String middle){
		if(StringUtils.isNotBlank(begin)&&StringUtils.isNotBlank(end)&&StringUtils.isNotBlank(middle)){
			BigDecimal middleNumber = new BigDecimal(middle);
			BigDecimal beginNumber = new BigDecimal(begin);
			BigDecimal endNumber = new BigDecimal(end);
			if(middleNumber.compareTo(beginNumber)>-1 && middleNumber.compareTo(endNumber)<1){
				return true;
			}
		}
		return false;
	}


	//四、判别事故车
	/** @param checkTradableVehiclesT  教师-检查可交易车辆
	 * @param checkTradableVehiclesS  学生-检查可交易车辆
	 * @param examScoreMap              分数项分值
	 * @return
	 */
	public BigDecimal getAccidentVehicles(CheckTradableVehicles checkTradableVehiclesT,CheckTradableVehicles checkTradableVehiclesS,
										  Map<String,Object> examScoreMap,ExamUser user,Map<String,Object> examNameMap){
		BigDecimal accidentCount = new BigDecimal(0);
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getIsAccident()) && checkTradableVehiclesT.getIsAccident().equals(checkTradableVehiclesS.getIsAccident())){
			accidentCount = accidentCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664201729"))));  //判定事故车选择正确
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615991297",
					(String)examNameMap.get("1151013343664201729"),(String)examScoreMap.get("1151013343664201729"),
					getIsAccidentName(checkTradableVehiclesT.getIsAccident()),getIsAccidentName(checkTradableVehiclesS.getIsAccident()),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180615991297",
					(String)examNameMap.get("1151013343664201729"),"0",
					getIsAccidentName(checkTradableVehiclesT.getIsAccident()),getIsAccidentName(checkTradableVehiclesS.getIsAccident()),"1");
		}

		return  accidentCount;
	}

	//判断是否为事故车 0 正常车，1 非正常车
	public String getIsAccidentName(String isAccident){
		if(StringUtils.isNotBlank(isAccident)){
			if("0".equals(isAccident)){
				return "正常车";
			}else if("1".equals(isAccident)){
				return "非正常车";
			}
		}
		return "";
	}




	//三、记录车辆基本信息
	public BigDecimal getCarInfo(CarInfo carInfoT,CarInfo carInfoS,Map<String,Object> examScoreMap,List<VehicleInstallInfo> vehicleInstallInfoListT,
								 List<VehicleInstallInfo> vehicleInstallInfoListS,ExamUser user,Map<String,Object> examNameMap,String paperId){
		BigDecimal carInfoCount = new BigDecimal(0);
		if(StringUtils.isNotBlank(carInfoT.getBrand())  && carInfoT.getBrand().equals(carInfoS.getBrand())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664197633")))); //品牌
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664197633"),(String)examScoreMap.get("1151013343664197633"),
					carInfoT.getBrand(),carInfoS.getBrand(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664197633"),"0",
					carInfoT.getBrand(),carInfoS.getBrand(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getModel()) && carInfoT.getModel().equals(carInfoS.getModel())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664799745")))); //车型
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664799745"),(String)examScoreMap.get("1151013343664799745"),
					carInfoT.getModel(),carInfoS.getModel(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664799745"),"0",
					carInfoT.getModel(),carInfoS.getModel(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getLevel()) && carInfoT.getLevel().equals(carInfoS.getLevel())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664439297")))); //级别
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664439297"),(String)examScoreMap.get("1151013343664439297"),
					carInfoT.getLevel(),carInfoS.getLevel(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664439297"),"0",
					carInfoT.getLevel(),carInfoS.getLevel(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getManufactureDate()) &&carInfoT.getManufactureDate().equals(carInfoS.getManufactureDate())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665321234")))); //出厂日期
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343665321234"),(String)examScoreMap.get("1151013343665321234"),
					carInfoT.getManufactureDate(),carInfoS.getManufactureDate(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343665321234"),"0",
					carInfoT.getManufactureDate(),carInfoS.getManufactureDate(),"1");
		}
		if(carInfoT.getChangeNum()==carInfoS.getChangeNum() ){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663411201")))); //过户次数
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663411201"),(String)examScoreMap.get("1151013343663411201"),
					String.valueOf(carInfoT.getChangeNum()),String.valueOf(carInfoS.getChangeNum()),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663411201"),"0",
					String.valueOf(carInfoT.getChangeNum()),String.valueOf(carInfoS.getChangeNum()),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getEnvironmentalStandard()) &&carInfoT.getEnvironmentalStandard().equals(carInfoS.getEnvironmentalStandard())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663699964")))); //环保标准
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663699964"),(String)examScoreMap.get("1151013343663699964"),
					carInfoT.getEnvironmentalStandard(),carInfoS.getEnvironmentalStandard(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663699964"),"0",
					carInfoT.getEnvironmentalStandard(),carInfoS.getEnvironmentalStandard(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getCurbWeight()) &&carInfoT.getCurbWeight().equals(carInfoS.getCurbWeight())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663931393")))); //整备质量
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663931393"),(String)examScoreMap.get("1151013343663931393"),
					carInfoT.getCurbWeight(),carInfoS.getCurbWeight(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663931393"),"0",
					carInfoT.getCurbWeight(),carInfoS.getCurbWeight(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getEngineModel()) &&carInfoT.getEngineModel().equals(carInfoS.getEngineModel())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666056124")))); //发动机型号
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343666056124"),(String)examScoreMap.get("1151013343666056124"),
					carInfoT.getEngineModel(),carInfoS.getEngineModel(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343666056124"),"0",
					carInfoT.getEngineModel(),carInfoS.getEngineModel(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getEngineNum()) &&carInfoT.getEngineNum().equals(carInfoS.getEngineNum())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666704366")))); //发动机号码
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343666704366"),(String)examScoreMap.get("1151013343666704366"),
					carInfoT.getEngineNum(),carInfoS.getEngineNum(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343666704366"),"0",
					carInfoT.getEngineNum(),carInfoS.getEngineNum(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getBodySize()) &&carInfoT.getBodySize().equals(carInfoS.getBodySize())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666286593")))); //车身尺寸
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343666286593"),(String)examScoreMap.get("1151013343666286593"),
					carInfoT.getBodySize(),carInfoS.getBodySize(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343666286593"),"0",
					carInfoT.getBodySize(),carInfoS.getBodySize(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getVehicleWarranty()) &&carInfoT.getVehicleWarranty().equals(carInfoS.getVehicleWarranty())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665496065")))); //整车质保
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343665496065"),(String)examScoreMap.get("1151013343665496065"),
					carInfoT.getVehicleWarranty(),carInfoS.getVehicleWarranty(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343665496065"),"0",
					carInfoT.getVehicleWarranty(),carInfoS.getVehicleWarranty(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getYearCheckDue()) &&carInfoT.getYearCheckDue().equals(carInfoS.getYearCheckDue())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663656961")))); //年检到期
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663656961"),(String)examScoreMap.get("1151013343663656961"),
					carInfoT.getYearCheckDue(),carInfoS.getYearCheckDue(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343663656961"),"0",
					carInfoT.getYearCheckDue(),carInfoS.getYearCheckDue(),"1");
		}
		//定义开关
		Boolean flag = false;
		StringBuilder vehicleInstallProject = new StringBuilder();
		//车辆加装信息
		for(int i = 0;i<vehicleInstallInfoListT.size();i++){
			for(VehicleInstallInfo vehicleStu:vehicleInstallInfoListS){
				if(StringUtils.isNotBlank(vehicleInstallInfoListT.get(i).getProject()) && vehicleInstallInfoListT.get(i).getProject().equals(vehicleStu.getProject())){
					flag = true;
					break;
				}
			}
			if(i==(vehicleInstallInfoListT.size()-1) && flag ){
			}else if(flag){
				flag = false;
			}else{
				break;
			}
		}
		//判断加装信息 老师所选学生答案内全部包含 并且学生不能多选
		if(flag && (vehicleInstallInfoListT.size()==vehicleInstallInfoListS.size())){
			carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664160769")))); //车辆加装信息
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664160769"),(String)examScoreMap.get("1151013343664160769"),
					vehicleInstallInfoService.getVehicleInstallName(null,paperId),vehicleInstallInfoService.getVehicleInstallName(user.getId(),null),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617760769",
					(String)examNameMap.get("1151013343664160769"),"0",
					vehicleInstallInfoService.getVehicleInstallName(null,paperId),vehicleInstallInfoService.getVehicleInstallName(user.getId(),null),"1");

		}
		return carInfoCount;
	}



	/**
	 *  二 获取 判别可交易车辆分数
	 * @param vehicleDocumentInfoListS   判别可交易车辆学生类
	 * @param vehicleDocumentInfoListT 判别可交易车辆 教师答案
	 * @param examScoreMap 评分项分值
	 * @return
	 */
	public BigDecimal getVehicleDocumentInfo(List<VehicleDocumentInfo> vehicleDocumentInfoListS, List<VehicleDocumentInfo> vehicleDocumentInfoListT,
											 Map<String, Object> examScoreMap,CheckTradableVehicles checkTradableVehiclesS,
											 CheckTradableVehicles checkTradableVehiclesT,ExamUser user,Map<String,Object> examNameMap){

		BigDecimal  vehicleDocumentCount = new BigDecimal(0);
		for(VehicleDocumentInfo vehicleDocumentT:vehicleDocumentInfoListT){
			//定义开关 判断学生是否存在未填数据
			Boolean flag = false;
			//学生所填 答案
			VehicleDocumentInfo vehicleDocument = null;
			for(VehicleDocumentInfo vehicleDocumentS:vehicleDocumentInfoListS){
				//单证项目   是否包含所选项
				if(StringUtils.isNotBlank(vehicleDocumentT.getProject()) &&vehicleDocumentT.getProject().equals(vehicleDocumentS.getProject())){
					flag = true;
					vehicleDocument = vehicleDocumentS;
					break;
				}
			}
			if(flag){
				if(StringUtils.isNotBlank(vehicleDocumentT.getState()) && vehicleDocumentT.getState().equals(vehicleDocument.getState())){
					vehicleDocumentCount = vehicleDocumentCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())))));
					saveExamDetail(user.getId(),user.getExamId(),"1151028180614815745",
							(String)examNameMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),(String)examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),
							vehicleDocumentT.getState(),vehicleDocument.getState(),"0");
				}else{
					saveExamDetail(user.getId(),user.getExamId(),"1151028180614815745",
							(String)examNameMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),"0",
							vehicleDocumentT.getState(),vehicleDocument.getState(),"1");
				}
			}else{
				saveExamDetail(user.getId(),user.getExamId(),"1151028180614815745",
						(String)examNameMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),"0",
						vehicleDocumentT.getState(),"","1");
			}

		}
		//判别是否为可交易车辆
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getIsTrade()) &&checkTradableVehiclesT.getIsTrade().equals(checkTradableVehiclesS.getIsTrade())){
			vehicleDocumentCount = vehicleDocumentCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663693825"))));  //判别是否可交易
			saveExamDetail(user.getId(),user.getExamId(),"1151028180614815745",
					(String)examNameMap.get("1151013343663693825"),(String)examScoreMap.get("1151013343663693825"),
					checkTradableVehiclesT.getIsTrade(),checkTradableVehiclesS.getIsTrade(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180614815745",
					(String)examNameMap.get("1151013343663693825"),"0",
					checkTradableVehiclesT.getIsTrade(),checkTradableVehiclesS.getIsTrade(),"1");
		}
		return vehicleDocumentCount;
	}

	/**
	 * 获取对应的分值项
	 * @param project
	 * @return
	 */
	public String getExamScoreInfo(String project){
		switch(project){
			case "1" :
				return "1151013343663300609"; //行驶证（正、副页）
			case "2" :
				return "1151013343662862337"; //机动车登记证书
			case "3" :
				return "1151013343665299457"; //车船使用税缴付凭证
			case "4" :
				return "1151013343665225729"; //机动车交通事故责任强制保险单
			case "5" :
				return "1151013343664046081"; //机动车综合商业保险保单
			case "6" :
				return "1151013343664152577"; //年审检验合格标志（正反页）
			case "7" :
				return "1151013343664046021"; //强制保险标志
			case "8" :
				return "1151013343663435777"; //车辆购置税完税证明
			default :
		}
		return null;
	}



	/**
	 *  委托人 模块
	 * @param delegateUserS 委托人-学生
	 * @param carInfoS  委托车辆-学生
	 * @param delegateUserT   委托人--老师答案
	 * @param carInfoT      委托车辆-老师答案
	 * @param examScoreMap 考试评分项分值
	 * @return
	 */
	public BigDecimal getDelegateInfo(DelegateUser delegateUserS,CarInfo carInfoS,DelegateUser delegateUserT,
									  CarInfo carInfoT,Map<String, Object> examScoreMap,ExamUser user,Map<String,Object> examNameMap){
		BigDecimal delegateCount = new BigDecimal(0);
		//一、委托人

		if(StringUtils.isNotBlank(delegateUserT.getName()) && delegateUserT.getName().equals(delegateUserS.getName())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664762880"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664762880"),(String)examScoreMap.get("1151013343664762880"),
					delegateUserT.getName(),delegateUserS.getName(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664762880"),"0",
					delegateUserT.getName(),delegateUserS.getName(),"1");
		}
		if(StringUtils.isNotBlank(delegateUserT.getIdNum()) &&delegateUserT.getIdNum().equals(delegateUserS.getIdNum())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663427585"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663427585"),(String)examScoreMap.get("1151013343663427585"),
					delegateUserT.getIdNum(),delegateUserS.getIdNum(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663427585"),"0",
					delegateUserT.getIdNum(),delegateUserS.getIdNum(),"1");
		}
		if(StringUtils.isNotBlank(delegateUserT.getAddress()) &&delegateUserT.getAddress().equals(delegateUserS.getAddress())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663116289"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663116289"),(String)examScoreMap.get("1151013343663116289"),
					delegateUserT.getAddress(),delegateUserS.getAddress(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663116289"),"0",
					delegateUserT.getAddress(),delegateUserS.getAddress(),"1");
		}
		if(StringUtils.isNotBlank(delegateUserT.getContact()) &&delegateUserT.getContact().equals(delegateUserS.getContact())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664889857"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664889857"),(String)examScoreMap.get("1151013343664889857"),
					delegateUserT.getContact(),delegateUserS.getContact(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664889857"),"0",
					delegateUserT.getContact(),delegateUserS.getContact(),"1");
		}
		if(StringUtils.isNotBlank(delegateUserT.getPhone()) &&delegateUserT.getPhone().equals(delegateUserS.getPhone())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664988161"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664988161"),(String)examScoreMap.get("1151013343664988161"),
					delegateUserT.getPhone(),delegateUserS.getPhone(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664988161"),"0",
					delegateUserT.getPhone(),delegateUserS.getPhone(),"1");
		}
		if(StringUtils.isNotBlank(delegateUserT.getApplyReason()) &&delegateUserT.getApplyReason().equals(delegateUserS.getApplyReason())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663902721"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663902721"),(String)examScoreMap.get("1151013343663902721"),
					delegateUserT.getApplyReason(),delegateUserS.getApplyReason(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663902721"),"0",
					delegateUserT.getApplyReason(),delegateUserS.getApplyReason(),"1");
		}
		if(StringUtils.isNotBlank(delegateUserT.getCompleteDate()) &&delegateUserT.getCompleteDate().equals(delegateUserS.getCompleteDate())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663427585"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663427585"),(String)examScoreMap.get("1151013343663427585"),
					delegateUserT.getCompleteDate(),delegateUserS.getCompleteDate(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663427585"),"0",
					delegateUserT.getCompleteDate(),delegateUserS.getCompleteDate(),"1");
		}

		if(StringUtils.isNotBlank(carInfoT.getLicensePlateNum()) &&carInfoT.getLicensePlateNum().equals(carInfoS.getLicensePlateNum())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666503681"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666503681"),(String)examScoreMap.get("1151013343666503681"),
					carInfoT.getLicensePlateNum(),carInfoS.getLicensePlateNum(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666503681"),"0",
					carInfoT.getLicensePlateNum(),carInfoS.getLicensePlateNum(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getVehicleType()) &&carInfoT.getVehicleType().equals(carInfoS.getVehicleType())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664988163"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664988163"),(String)examScoreMap.get("1151013343664988163"),
					carInfoT.getVehicleType(),carInfoS.getVehicleType(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664988163"),"0",
					carInfoT.getVehicleType(),carInfoS.getVehicleType(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getVinCode()) &&carInfoT.getVinCode().equals(carInfoS.getVinCode())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664328705"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664328705"),(String)examScoreMap.get("1151013343664328705"),
					carInfoT.getVinCode(),carInfoS.getVinCode(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664328705"),"0",
					carInfoT.getVinCode(),carInfoS.getVinCode(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getLabelType()) &&carInfoT.getLabelType().equals(carInfoS.getLabelType())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666032641"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666032641"),(String)examScoreMap.get("1151013343666032641"),
					carInfoT.getLabelType(),carInfoS.getLabelType(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666032641"),"0",
					carInfoT.getLabelType(),carInfoS.getLabelType(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getUsage()) &&carInfoT.getUsage().equals(carInfoS.getUsage())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666728961"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666728961"),(String)examScoreMap.get("1151013343666728961"),
					carInfoT.getUsage(),carInfoS.getUsage(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666728961"),"0",
					carInfoT.getUsage(),carInfoS.getUsage(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getTotalQuality()) &&carInfoT.getTotalQuality().equals(carInfoS.getTotalQuality())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663927297"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663927297"),(String)examScoreMap.get("1151013343663927297"),
					carInfoT.getTotalQuality(),carInfoS.getTotalQuality(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663927297"),"0",
					carInfoT.getTotalQuality(),carInfoS.getTotalQuality(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getSear()) &&carInfoT.getSear().equals(carInfoS.getSear())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664050177"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664050177"),(String)examScoreMap.get("1151013343664050177"),
					carInfoT.getSear(),carInfoS.getSear(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664050177"),"0",
					carInfoT.getSear(),carInfoS.getSear(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getDisplacement()) &&carInfoT.getDisplacement().equals(carInfoS.getDisplacement())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343662784513"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343662784513"),(String)examScoreMap.get("1151013343662784513"),
					carInfoT.getDisplacement(),carInfoS.getDisplacement(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343662784513"),"0",
					carInfoT.getDisplacement(),carInfoS.getDisplacement(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getFuelType()) &&carInfoT.getFuelType().equals(carInfoS.getFuelType())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665344513"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665344513"),(String)examScoreMap.get("1151013343665344513"),
					carInfoT.getFuelType(),carInfoS.getFuelType(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665344513"),"0",
					carInfoT.getFuelType(),carInfoS.getFuelType(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getRegisterDate()) &&carInfoT.getRegisterDate().equals(carInfoS.getRegisterDate())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343662788609"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343662788609"),(String)examScoreMap.get("1151013343662788609"),
					carInfoT.getRegisterDate(),carInfoS.getRegisterDate(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343662788609"),"0",
					carInfoT.getRegisterDate(),carInfoS.getRegisterDate(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getColor()) &&carInfoT.getColor().equals(carInfoS.getColor())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664787457"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664787457"),(String)examScoreMap.get("1151013343664787457"),
					carInfoT.getColor(),carInfoS.getColor(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664787457"),"0",
					carInfoT.getColor(),carInfoS.getColor(),"1");
		}
		if(carInfoT.getUseYear()==carInfoS.getUseYear() && carInfoT.getUseMonth()==carInfoS.getUseMonth()) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665397761"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665397761"),(String)examScoreMap.get("1151013343665397761"),
					String.valueOf(carInfoT.getUseMonth()),String.valueOf(carInfoS.getUseMonth()),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665397761"),"0",
					String.valueOf(carInfoT.getUseMonth()),String.valueOf(carInfoS.getUseMonth()),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getMileage()) &&carInfoT.getMileage().equals(carInfoS.getMileage())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665954817"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665954817"),(String)examScoreMap.get("1151013343665954817"),
					carInfoT.getMileage(),carInfoS.getMileage(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665954817"),"0",
					carInfoT.getMileage(),carInfoS.getMileage(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getEngineOverhaulTimes()) &&carInfoT.getEngineOverhaulTimes().equals(carInfoS.getEngineOverhaulTimes())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666315265"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666315265"),(String)examScoreMap.get("1151013343666315265"),
					carInfoT.getEngineOverhaulTimes(),carInfoS.getEngineOverhaulTimes(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666315265"),"0",
					carInfoT.getEngineOverhaulTimes(),carInfoS.getEngineOverhaulTimes(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getCarOverhaulTimes()) &&carInfoT.getCarOverhaulTimes().equals(carInfoS.getCarOverhaulTimes())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343664455681"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664455681"),(String)examScoreMap.get("1151013343664455681"),
					carInfoT.getCarOverhaulTimes(),carInfoS.getCarOverhaulTimes(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343664455681"),"0",
					carInfoT.getCarOverhaulTimes(),carInfoS.getCarOverhaulTimes(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getMaintenanceSituation()) &&carInfoT.getMaintenanceSituation().equals(carInfoS.getMaintenanceSituation())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665635329"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665635329"),(String)examScoreMap.get("1151013343665635329"),
					carInfoT.getMaintenanceSituation(),carInfoS.getMaintenanceSituation(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665635329"),"0",
					carInfoT.getMaintenanceSituation(),carInfoS.getMaintenanceSituation(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getAccident()) &&carInfoT.getAccident().equals(carInfoS.getAccident())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343665983489"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665983489"),(String)examScoreMap.get("1151013343665983489"),
					carInfoT.getAccident(),carInfoS.getAccident(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343665983489"),"0",
					carInfoT.getAccident(),carInfoS.getAccident(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getPurchaseDate()) &&carInfoT.getPurchaseDate().equals(carInfoS.getPurchaseDate())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343666704385"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666704385"),(String)examScoreMap.get("1151013343666704385"),
					carInfoT.getPurchaseDate(),carInfoS.getPurchaseDate(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343666704385"),"0",
					carInfoT.getPurchaseDate(),carInfoS.getPurchaseDate(),"1");
		}
		if(StringUtils.isNotBlank(carInfoT.getOriginalPrice()) &&carInfoT.getOriginalPrice().equals(carInfoS.getOriginalPrice())) {
			delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String)examScoreMap.get("1151013343663955969"))));
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663955969"),(String)examScoreMap.get("1151013343663955969"),
					carInfoT.getOriginalPrice(),carInfoS.getOriginalPrice(),"0");
		}else{
			saveExamDetail(user.getId(),user.getExamId(),"1151028180617777153",
					(String)examNameMap.get("1151013343663955969"),"0",
					carInfoT.getOriginalPrice(),carInfoS.getOriginalPrice(),"1");
		}
		//盖章
		return delegateCount;
	}

	/**
	 * 考试计时
	 */
	@Transactional(readOnly = false)
	public CommonResult examTiming(ExamUser examUser) {
        CommonResult comRes = new CommonResult();
        examUser = this.get(examUser);
        if (examUser == null) {
            comRes.setCode(CodeConstant.REQUEST_FAILED);
            comRes.setMsg("未查询到考生信息！");
            return comRes;
        }
        Exam exam = new Exam();
        exam.setId(examUser.getExamId());
        exam = examService.getByEntity(exam);
        if (exam == null) {
            comRes.setCode(CodeConstant.REQUEST_FAILED);
            comRes.setMsg("未查询到考试信息！");
            return comRes;
        }
		if ("1".equals(exam.getExamType())) {
			//倒计时
			BigDecimal duration = new BigDecimal(exam.getDuration());       //考试总时长
			duration = duration.multiply(new BigDecimal("60000"));      //换算毫秒值
			BigDecimal startTime = new BigDecimal(examUser.getStartTime().getTime());       //考试开始时间
			BigDecimal nowTime = new BigDecimal(new Date().getTime());      //当前时间
			BigDecimal spentTime = nowTime.subtract(startTime);     //考生考试已用时长（nowTime-startTime）
			BigDecimal surplusTime = duration.subtract(spentTime);      //本场考试剩余时长(duration-spentTime)

			//对剩余时长进行校验，如果剩余时长小于零，则考试结束，返回状态码。
			if (surplusTime.compareTo(new BigDecimal("0")) >= 0) {
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
				comRes.setData(df.format(new Date(surplusTime.longValue())));
			}else {
				comRes.setCode(CodeConstant.EXAM_END);
				comRes.setMsg("考试时间到，考试结束！");
				return comRes;
			}
		}else {
			//正计时
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			comRes.setData(df.format(new Date()));
		}
		return comRes;

	}

}