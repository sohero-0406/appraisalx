/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.CheckTradableVehicles;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.OperationLogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.PlaceFile;
import com.jeesite.modules.aa.dao.PlaceFileDao;

/**
 * 归档Service
 * @author liangtao
 * @version 2019-07-20
 */
@Service
@Transactional(readOnly=true)
public class PlaceFileService extends CrudService<PlaceFileDao, PlaceFile> {

	@Autowired
	private CheckTradableVehiclesService checkTradableVehiclesService;
	@Autowired
	private OperationLogService operationLogService;

	/**
	 * 获取单条数据
	 * @param placeFile
	 * @return
	 */
	@Override
	public PlaceFile get(PlaceFile placeFile) {
		return super.get(placeFile);
	}
	
	/**
	 * 查询分页数据
	 * @param placeFile 查询条件
	 * @return
	 */
	@Override
	public Page<PlaceFile> findPage(PlaceFile placeFile) {
		return super.findPage(placeFile);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param placeFile
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(PlaceFile placeFile) {
		super.save(placeFile);
	}
	
	/**
	 * 更新状态
	 * @param placeFile
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(PlaceFile placeFile) {
		super.updateStatus(placeFile);
	}
	
	/**
	 * 删除数据
	 * @param placeFile
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(PlaceFile placeFile) {
		super.delete(placeFile);
	}

	@Transactional(readOnly=false)
	public List<String> getFileByAssessedPicture(String paperId) {
		return dao.getFileByAssessedPicture(paperId);
	}

	@Transactional(readOnly=false)
	public List<String> getPlaceFileByStu(String examUserId) {
		return dao.getPlaceFileByStu(examUserId);
	}


	@Transactional
	public Map getPlaceFileList(ExamUser examUser){
		Map<String,Object> returnMap = new HashMap<>();
		//接受委托、检查可交易车辆、记录车辆基本信息、判别事故车辆 归档数据列表 type = 0
		String type = "0";
		List<PictureUser> pictureUserList =  dao.selectPlaceListFrist(examUser.getId(),examUser.getPaperId(),type);
		type = "1"; // 鉴定技术状况 数据列表
		List<PictureUser> pictureUserListIdentification =  dao.selectPlaceListFrist(examUser.getId(),examUser.getPaperId(),type);
		//车辆鉴定报告
		PictureUser pictureUser = new PictureUser();
		pictureUser.setPictureId("1143446339264172032"); //鉴定技术状况
		pictureUser.setName("鉴定技术状况");
		pictureUser.setItemList(pictureUserListIdentification);
		//报告
		List<PictureUser> pictureUserPlace = dao.selectPlace(examUser);
		pictureUserList.add(pictureUser);
		pictureUserList.addAll(pictureUserPlace);

		CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
		checkTradableVehicles.setExamUserId(examUser.getId());
		checkTradableVehicles.setPaperId(examUser.getPaperId());
		checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);
		returnMap.put("pictureUserList",pictureUserList);
		returnMap.put("fileDuring",null==checkTradableVehicles?"":checkTradableVehicles.getFileDuring());
		return returnMap;
	}


	//保存 用户归档
	@Transactional(readOnly=false)
	public void saveArchive(String pictureUserJson, String fileDuring, ExamUser examUser){
		pictureUserJson = pictureUserJson.replace("\n","");
		pictureUserJson = pictureUserJson.replace(" ","");
		JSONArray jsonArray = JSONObject.parseArray(pictureUserJson);
		//先删除 在保存
		PlaceFile place = new PlaceFile();
		place.setExamUserId(examUser.getId());
		place.setPaperId(examUser.getPaperId());
		dao.phyDeleteByEntity(place);  //s
		//保存归档
		if(CollectionUtils.isNotEmpty(jsonArray)){
			for(Object picture : jsonArray){
				JSONObject p = (JSONObject)picture;
                 PlaceFile placeFile = new PlaceFile();
                 placeFile.setPaperId(examUser.getPaperId());
                 placeFile.setExamUserId(examUser.getId());
                 placeFile.setPictureUserId(p.getString("id"));
                 super.save(placeFile);
			}
		}
		if(StringUtils.isNotBlank(fileDuring)){
			//更新归档时效 aa_check_tradable_vehicles
			CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
			checkTradableVehicles.setExamUserId(examUser.getId());
			checkTradableVehicles.setPaperId(examUser.getPaperId());
			checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);
			checkTradableVehicles.setFileDuring(fileDuring);
			checkTradableVehiclesService.save(checkTradableVehicles);
		}
		operationLogService.saveObj(examUser,"保存归档成功");
	}
}