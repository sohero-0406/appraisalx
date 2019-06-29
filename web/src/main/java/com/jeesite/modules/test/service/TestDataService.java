/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.dao.TestDataDao;

/**
 * 测试数据Service
 * @author lvchangwei
 * @version 2019-06-18
 */
@Service
@Transactional(readOnly=true)
public class TestDataService extends CrudService<TestDataDao, TestData> {
	
	/**
	 * 获取单条数据
	 * @param testData
	 * @return
	 */
	@Override
	public TestData get(TestData testData) {
		return super.get(testData);
	}
	
	/**
	 * 查询分页数据
	 * @param testData 查询条件
	 * @param testData.page 分页对象
	 * @return
	 */
	@Override
	public Page<TestData> findPage(TestData testData) {
		return super.findPage(testData);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testData
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(TestData testData) {
		super.save(testData);
	}
	
	/**
	 * 更新状态
	 * @param testData
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(TestData testData) {
		super.updateStatus(testData);
	}
	
	/**
	 * 删除数据
	 * @param testData
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(TestData testData) {
		super.delete(testData);
	}
	
}