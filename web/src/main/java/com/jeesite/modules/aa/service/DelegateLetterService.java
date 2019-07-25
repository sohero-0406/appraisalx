/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.DelegateLetterDao;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.DelegateLetter;
import com.jeesite.modules.aa.entity.DelegateUser;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.vo.DelegateLetterVO;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

/**
 * 委托书信息Service
 * @author lvchangwei
 * @version 2019-07-18
 */
@Service
@Transactional(readOnly=true)
public class DelegateLetterService extends CrudService<DelegateLetterDao, DelegateLetter> {

	@Autowired
	private DelegateUserService delegateUserService;
	@Autowired
	private CarInfoService carInfoService;
	@Autowired
	private PictureUserService pictureUserService;
	
	/**
	 * 获取单条数据
	 * @param delegateLetter
	 * @return
	 */
	@Override
	public DelegateLetter get(DelegateLetter delegateLetter) {
		return super.get(delegateLetter);
	}
	
	/**
	 * 查询分页数据
	 * @param delegateLetter 查询条件
	 * @return
	 */
	@Override
	public Page<DelegateLetter> findPage(DelegateLetter delegateLetter) {
		return super.findPage(delegateLetter);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param delegateLetter
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(DelegateLetter delegateLetter) {
		super.save(delegateLetter);
	}
	
	/**
	 * 更新状态
	 * @param delegateLetter
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(DelegateLetter delegateLetter) {
		super.updateStatus(delegateLetter);
	}
	
	/**
	 * 删除数据
	 * @param delegateLetter
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(DelegateLetter delegateLetter) {
		super.delete(delegateLetter);
	}

	public DelegateLetter getByEntity(DelegateLetter delegateLetter) {
		return dao.getByEntity(delegateLetter);
	}

	/**
	 * 查询单个委托书信息
	 */
	public DelegateLetterVO findDelegateLetter(ExamUser examUser) {

		DelegateLetterVO delegateLetterVO = new DelegateLetterVO();

		DelegateLetter delegateLetter = new DelegateLetter();
		delegateLetter.setExamUserId(examUser.getExamId());
		delegateLetter.setPaperId(examUser.getPaperId());
		delegateLetter = this.getByEntity(delegateLetter);
		delegateLetterVO.setDelegateLetter(delegateLetter);

		DelegateUser delegateUser = new DelegateUser();
		delegateUser.setExamUserId(examUser.getExamId());
		delegateUser.setPaperId(examUser.getPaperId());
		delegateUser = delegateUserService.getByEntity(delegateUser);
		if(StringUtils.isNotBlank(delegateUser.getCompleteDate())){
			String[] completeDateArr = delegateUser.getCompleteDate().split("-");
			delegateUser.setCompleteDate(completeDateArr[0] + "年" + completeDateArr[1] + "月" + completeDateArr[2] + "日");
		}
		delegateLetterVO.setDelegateUser(delegateUser);

		CarInfo carInfo = new CarInfo();
		carInfo.setExamUserId(examUser.getExamId());
		carInfo.setPaperId(examUser.getPaperId());
		carInfo = carInfoService.getByEntity(carInfo);
		carInfo.setColor(carInfoService.getColor(carInfo));
		if(StringUtils.isNotBlank(carInfo.getRegisterDate())){
			carInfo.setRegisterDate(new SimpleDateFormat("yyyy年MM月dd日").format(carInfo.getRegisterDate()));
		}
		if(StringUtils.isNotBlank(carInfo.getYearCheckDue())){
			String[] yearCheckDue = carInfo.getYearCheckDue().split("-");
			carInfo.setYearCheckDue(yearCheckDue[0] + "年" + yearCheckDue[1] + "月" + yearCheckDue[2] + "日");
		}
		if(StringUtils.isNotBlank(carInfo.getPurchaseDate())){
			String[] purchaseDate = carInfo.getPurchaseDate().split("-");
			carInfo.setPurchaseDate(purchaseDate[0] + "年" + purchaseDate[1] + "月" + purchaseDate[2] + "日");
		}
		delegateLetterVO.setCarInfo(carInfo);

		PictureUser pictureUser = new PictureUser();
		pictureUser.setExamUserId(examUser.getExamId());
		pictureUser.setPaperId(examUser.getPaperId());
		pictureUser.setPictureTypeId("1143436249238634496");
		pictureUser = pictureUserService.getByEntity(pictureUser);
		delegateLetterVO.setPictureUser(pictureUser);

		return delegateLetterVO;
	}

}