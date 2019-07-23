/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.service.*;
import com.jeesite.modules.common.dao.ExamUserDao;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private ExamScoreDetailService examScoreDetailService;

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

	/**
	 *  判卷
	 */
	@Transactional(readOnly = false)
	public Object gradePapers() {
		//页面获取考试id  examId
		String examId = "1151435216635924480";
		String paperId = "1151374360001810432"; //页面获取数据


		//依据考试 获取所有的评分项
		List<Map<String, Object>> examScoreItems = examScoreClassifyService.getExamScoreInfo(examId);
		//创建分数项
		Map<String, BigDecimal> examScoreMap = new HashMap<>();
		//处理评分项 封成一组map 将考试分值项
		for (Map<String, Object> score : examScoreItems) {
			examScoreMap.put((String) score.get("score_info_id"), BigDecimal.valueOf(Integer.parseInt((String)score.get("score"))));
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
			BigDecimal delegateCount = getDelegateInfo(delegateUserS,carInfoS,delegateUserT,carInfoT,examScoreMap);

			//二、判别可交易车辆
			vehicleDocumentInfoStu.setExamUserId(user.getId());
			List<VehicleDocumentInfo> vehicleDocumentInfoListS = vehicleDocumentInfoService.findList(vehicleDocumentInfoStu);
			checkTradableVehiclesStu.setExamUserId(user.getId());
			CheckTradableVehicles checkTradableVehiclesS = checkTradableVehiclesService.getByEntity(checkTradableVehiclesStu);
			BigDecimal vehicleDocumentCount = getVehicleDocumentInfo(vehicleDocumentInfoListS,vehicleDocumentInfoListT,examScoreMap,checkTradableVehiclesS,checkTradableVehiclesT);

			//三、记录车辆基本信息
			vehicleInstallInfoStu.setExamUserId(user.getId());
			List<VehicleInstallInfo> vehicleInstallInfoListS = vehicleInstallInfoService.findList(vehicleInstallInfoStu);
			BigDecimal carInfoCount = getCarInfo(carInfoT,carInfoS,examScoreMap,vehicleInstallInfoListT,vehicleInstallInfoListS);

			//四、判别事故车
			BigDecimal accidentCount = getAccidentVehicles(checkTradableVehiclesT,checkTradableVehiclesS,examScoreMap);
			//五、鉴定技术状况
			vehicleGradeAssessStu.setExamUserId(user.getId());
			VehicleGradeAssess vehicleGradeAssessS = vehicleGradeAssessService.getByEntity(vehicleGradeAssessStu);
			BigDecimal identificationCount = getIdentification(vehicleGradeAssessT,vehicleGradeAssessS,examScoreMap);
			//六、估算价值
			appraisalReportStu.setExamUserId(user.getId());
			AppraisalReport appraisalReportS = appraisalReportService.getByEntity(appraisalReportStu);
			List<PlaceFile> placeFileListS =  placeFileService.findList(placeFileStu);
			BigDecimal calculateCount = getCalculate(calculateT,user,examScoreMap,checkTradableVehiclesT,checkTradableVehiclesS,appraisalReportT,appraisalReportS,placeFileListT,placeFileListS);

			BigDecimal count = delegateCount.add(vehicleDocumentCount.add(carInfoCount.add(accidentCount.add(identificationCount.add(calculateCount)))));
			user.setScore(String.valueOf(count));
			super.save(user);
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
	public BigDecimal getCalculate(Calculate calculateT,ExamUser user,Map<String,BigDecimal> examScoreMap,
								   CheckTradableVehicles checkTradableVehiclesT,CheckTradableVehicles checkTradableVehiclesS,
								   AppraisalReport appraisalReportT,AppraisalReport appraisalReportS,
								   List<PlaceFile> placeFileListT,List<PlaceFile> placeFileListS){
		BigDecimal calculateCount = new BigDecimal("0");
		//学生估算方式
		if(calculateT.getBeginPrice()!=null && calculateT.getEndPrice()!=null){
			if(judgeStringBetween(String.valueOf(calculateT.getBeginPrice()),String.valueOf(calculateT.getEndPrice()),calculateService.getEstimateByType(user.getId()).get("price"))){
				calculateCount = calculateCount.add(examScoreMap.get("1151013343665733999"));
			}
		}
		//是否查封、抵押车辆
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getCheck3())&& checkTradableVehiclesT.getCheck3().equals(checkTradableVehiclesS.getCheck3()) ){
			calculateCount = calculateCount.add(examScoreMap.get("1151013343663128577"));
		}
		//未接受处理的交通违法记录：
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getTrafficIllegalRecord())&& checkTradableVehiclesT.getTrafficIllegalRecord().equals(checkTradableVehiclesS.getTrafficIllegalRecord())){
			calculateCount = calculateCount.add(examScoreMap.get("1151013343662706689"));
		}
		//车辆鉴定评估价值为人民币（元）  1151028180615860225
//		if(){
//              未定
//		}
		//自鉴定评估基准日至（日）
		if(StringUtils.isNotEmpty(appraisalReportT.getBaseDateEnd())&& appraisalReportT.getBaseDateEnd().equals(appraisalReportS.getBaseDateEnd()) ){
			calculateCount = calculateCount.add(examScoreMap.get("1151028180615860225"));
		}
		//归档



		return calculateCount;
	}




	//五、鉴定技术状况
	public BigDecimal getIdentification(VehicleGradeAssess vehicleGradeAssessT,VehicleGradeAssess vehicleGradeAssessS,Map<String,BigDecimal> examScoreMap){
		BigDecimal identificationCount = new BigDecimal(0);
		//老师答案不为空
		if(this.judgeStringBetween(vehicleGradeAssessT.getStartScore(),vehicleGradeAssessT.getEndScore(),vehicleGradeAssessS.getScore())){
			identificationCount = identificationCount.add(examScoreMap.get("1151028180616400897")); //车辆鉴定得分值（区间值）
		}
		//鉴定日期
//        if(StringUtils.isNotBlank(vehicleGradeAssessT.get) )
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
	public BigDecimal getAccidentVehicles(CheckTradableVehicles checkTradableVehiclesT,CheckTradableVehicles checkTradableVehiclesS,Map<String,BigDecimal> examScoreMap){
		BigDecimal accidentCount = new BigDecimal(0);
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getIsAccident()) && checkTradableVehiclesT.getIsAccident().equals(checkTradableVehiclesS.getIsAccident())){
			accidentCount = accidentCount.add(examScoreMap.get("1151013343664201729"));  //判定事故车选择正确
		}
		return  accidentCount;
	}





	//三、记录车辆基本信息
	public BigDecimal getCarInfo(CarInfo carInfoT,CarInfo carInfoS,Map<String,BigDecimal> examScoreMap,List<VehicleInstallInfo> vehicleInstallInfoListT,List<VehicleInstallInfo> vehicleInstallInfoListS){
		BigDecimal carInfoCount = new BigDecimal(0);
		if(StringUtils.isNotBlank(carInfoT.getBrand())  && carInfoT.getBrand().equals(carInfoS.getBrand())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343664197633")); //品牌
		}
		if(StringUtils.isNotBlank(carInfoT.getModel()) && carInfoT.getModel().equals(carInfoS.getModel())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343664799745")); //车型
		}
		if(StringUtils.isNotBlank(carInfoT.getLevel()) && carInfoT.getLevel().equals(carInfoS.getLevel())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343664439297")); //级别
		}
		if(StringUtils.isNotBlank(carInfoT.getManufactureDate()) &&carInfoT.getManufactureDate().equals(carInfoS.getManufactureDate())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343665321234")); //出厂日期
		}
		if(carInfoT.getChangeNum()==carInfoS.getChangeNum() ){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343663411201")); //过户次数
		}
		if(StringUtils.isNotBlank(carInfoT.getEnvironmentalStandard()) &&carInfoT.getEnvironmentalStandard().equals(carInfoS.getEnvironmentalStandard())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343663699964")); //环保标准
		}
		if(StringUtils.isNotBlank(carInfoT.getCurbWeight()) &&carInfoT.getCurbWeight().equals(carInfoS.getCurbWeight())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343663931393")); //整备质量
		}
		if(StringUtils.isNotBlank(carInfoT.getEngineModel()) &&carInfoT.getEngineModel().equals(carInfoS.getEngineModel())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343666056124")); //发动机型号
		}
		if(StringUtils.isNotBlank(carInfoT.getEngineNum()) &&carInfoT.getEngineNum().equals(carInfoS.getEngineNum())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343666704366")); //发动机号码
		}
		if(StringUtils.isNotBlank(carInfoT.getBodySize()) &&carInfoT.getBodySize().equals(carInfoS.getBodySize())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343666286593")); //车身尺寸
		}
		if(StringUtils.isNotBlank(carInfoT.getVehicleWarranty()) &&carInfoT.getVehicleWarranty().equals(carInfoS.getVehicleWarranty())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343665496065")); //整车质保
		}
		if(StringUtils.isNotBlank(carInfoT.getYearCheckDue()) &&carInfoT.getYearCheckDue().equals(carInfoS.getYearCheckDue())){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343663656961")); //年检到期
		}
		Boolean flag = false;
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
		if(flag){
			carInfoCount = carInfoCount.add(examScoreMap.get("1151013343664160769")); //车辆加装信息
		}
		return carInfoCount;
	}




	/**
	 *  获取 判别可交易车辆分数
	 * @param vehicleDocumentInfoListS   判别可交易车辆学生类
	 * @param vehicleDocumentInfoListT 判别可交易车辆 教师答案
	 * @param examScoreMap 评分项分值
	 * @return
	 */
	public BigDecimal getVehicleDocumentInfo(List<VehicleDocumentInfo> vehicleDocumentInfoListS, List<VehicleDocumentInfo> vehicleDocumentInfoListT,Map<String, BigDecimal> examScoreMap,CheckTradableVehicles checkTradableVehiclesS,CheckTradableVehicles checkTradableVehiclesT){

		BigDecimal  vehicleDocumentCount = new BigDecimal(0);
		for(VehicleDocumentInfo vehicleDocumentT:vehicleDocumentInfoListT){
			for(VehicleDocumentInfo vehicleDocumentS:vehicleDocumentInfoListS){
				//单证项目
				if(StringUtils.isNotBlank(vehicleDocumentT.getProject()) &&vehicleDocumentT.getProject().equals(vehicleDocumentS.getProject())){
					vehicleDocumentCount = vehicleDocumentCount.add(examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())));
				}
			}
		}
		//判别是否为可交易车辆
		if(StringUtils.isNotBlank(checkTradableVehiclesT.getIsTrade()) &&checkTradableVehiclesT.getIsTrade().equals(checkTradableVehiclesS.getIsTrade())){
			vehicleDocumentCount = vehicleDocumentCount.add(examScoreMap.get("1151013343663693825"));  //判别是否可交易
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
	public BigDecimal getDelegateInfo(DelegateUser delegateUserS,CarInfo carInfoS,DelegateUser delegateUserT,CarInfo carInfoT,Map<String, BigDecimal> examScoreMap){
		BigDecimal delegateCount = new BigDecimal(0);
		//一、委托人

		if(StringUtils.isNotBlank(delegateUserT.getName()) && delegateUserT.getName().equals(delegateUserS.getName())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664762880"));
		}
		if(StringUtils.isNotBlank(delegateUserT.getIdNum()) &&delegateUserT.getIdNum().equals(delegateUserS.getIdNum())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343663427585"));
		}
		if(StringUtils.isNotBlank(delegateUserT.getAddress()) &&delegateUserT.getAddress().equals(delegateUserS.getAddress())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343663116289"));
		}
		if(StringUtils.isNotBlank(delegateUserT.getContact()) &&delegateUserT.getContact().equals(delegateUserS.getContact())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664889857"));
		}
		if(StringUtils.isNotBlank(delegateUserT.getPhone()) &&delegateUserT.getPhone().equals(delegateUserS.getPhone())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664988161"));
		}
		if(StringUtils.isNotBlank(delegateUserT.getApplyReason()) &&delegateUserT.getApplyReason().equals(delegateUserS.getApplyReason())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343663902721"));
		}
		if(StringUtils.isNotBlank(delegateUserT.getCompleteDate()) &&delegateUserT.getCompleteDate().equals(delegateUserS.getCompleteDate())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343663427585"));
		}

		if(StringUtils.isNotBlank(carInfoT.getLicensePlateNum()) &&carInfoT.getLicensePlateNum().equals(carInfoS.getLicensePlateNum())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343666503681"));
		}
		if(StringUtils.isNotBlank(carInfoT.getVehicleType()) &&carInfoT.getVehicleType().equals(carInfoS.getVehicleType())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664988163"));
		}
		if(StringUtils.isNotBlank(carInfoT.getVinCode()) &&carInfoT.getVinCode().equals(carInfoS.getVinCode())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664328705"));
		}
		if(StringUtils.isNotBlank(carInfoT.getLabelType()) &&carInfoT.getLabelType().equals(carInfoS.getLabelType())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343666032641"));
		}
		if(StringUtils.isNotBlank(carInfoT.getUsage()) &&carInfoT.getUsage().equals(carInfoS.getUsage())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343666728961"));
		}
		if(StringUtils.isNotBlank(carInfoT.getTotalQuality()) &&carInfoT.getTotalQuality().equals(carInfoS.getTotalQuality())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343663927297"));
		}
		if(StringUtils.isNotBlank(carInfoT.getSear()) &&carInfoT.getSear().equals(carInfoS.getSear())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664050177"));
		}
		if(StringUtils.isNotBlank(carInfoT.getDisplacement()) &&carInfoT.getDisplacement().equals(carInfoS.getDisplacement())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343662784513"));
		}
		if(StringUtils.isNotBlank(carInfoT.getFuelType()) &&carInfoT.getFuelType().equals(carInfoS.getFuelType())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343665344513"));
		}
		if(StringUtils.isNotBlank(carInfoT.getRegisterDate()) &&carInfoT.getRegisterDate().equals(carInfoS.getRegisterDate())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343662788609"));
		}
		if(StringUtils.isNotBlank(carInfoT.getColor()) &&carInfoT.getColor().equals(carInfoS.getColor())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664787457"));
		}
		if(carInfoT.getUseYear()==carInfoS.getUseYear() && carInfoT.getUseMonth()==carInfoS.getUseMonth()) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343665397761"));
		}
		if(StringUtils.isNotBlank(carInfoT.getMileage()) &&carInfoT.getMileage().equals(carInfoS.getMileage())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343665954817"));
		}
		if(StringUtils.isNotBlank(carInfoT.getEngineOverhaulTimes()) &&carInfoT.getEngineOverhaulTimes().equals(carInfoS.getEngineOverhaulTimes())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343666315265"));
		}
		if(StringUtils.isNotBlank(carInfoT.getCarOverhaulTimes()) &&carInfoT.getCarOverhaulTimes().equals(carInfoS.getCarOverhaulTimes())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343664455681"));
		}
		if(StringUtils.isNotBlank(carInfoT.getMaintenanceSituation()) &&carInfoT.getMaintenanceSituation().equals(carInfoS.getMaintenanceSituation())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343665635329"));
		}
		if(StringUtils.isNotBlank(carInfoT.getAccident()) &&carInfoT.getAccident().equals(carInfoS.getAccident())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343665983489"));
		}
		if(StringUtils.isNotBlank(carInfoT.getPurchaseDate()) &&carInfoT.getPurchaseDate().equals(carInfoS.getPurchaseDate())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343666704385"));
		}
		if(StringUtils.isNotBlank(carInfoT.getOriginalPrice()) &&carInfoT.getOriginalPrice().equals(carInfoS.getOriginalPrice())) {
			delegateCount = delegateCount.add(examScoreMap.get("1151013343663955969"));
		}
		//盖章
		return delegateCount;
	}

}