/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

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
	 * @param placeFile.page 分页对象
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


}