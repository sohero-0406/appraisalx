/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.Tax;
import com.jeesite.modules.aa.dao.TaxDao;

/**
 * 税率表Service
 * @author lvchangwei
 * @version 2019-07-18
 */
@Service
@Transactional(readOnly=true)
public class TaxService extends CrudService<TaxDao, Tax> {
	
	/**
	 * 获取单条数据
	 * @param tax
	 * @return
	 */
	@Override
	public Tax get(Tax tax) {
		return super.get(tax);
	}
	
	/**
	 * 查询分页数据
	 * @param tax 查询条件
	 * @param tax.page 分页对象
	 * @return
	 */
	@Override
	public Page<Tax> findPage(Tax tax) {
		return super.findPage(tax);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param tax
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Tax tax) {
		super.save(tax);
	}
	
	/**
	 * 更新状态
	 * @param tax
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Tax tax) {
		super.updateStatus(tax);
	}
	
	/**
	 * 删除数据
	 * @param tax
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Tax tax) {
		super.delete(tax);
	}

    public Tax getByEntity(Tax tax) {
		return dao.getByEntity(tax);
    }

	/**
	 *  全部删除
	 */
	@Transactional(readOnly=false)
	public void phyDeleteByEntity(Tax tax){
		dao.phyDeleteByEntity(tax);
	}
	@Transactional(readOnly=false)
	public long insertTax(Tax tax){
		return dao.insert(tax);
	}
}