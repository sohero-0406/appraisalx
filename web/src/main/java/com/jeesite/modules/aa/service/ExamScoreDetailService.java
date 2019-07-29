/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.ExamScoreDetail;
import com.jeesite.modules.aa.dao.ExamScoreDetailDao;

/**
 * 考试评分详情表Service
 * @author liangtao
 * @version 2019-07-16
 */
@Service
@Transactional(readOnly=true)
public class ExamScoreDetailService extends CrudService<ExamScoreDetailDao, ExamScoreDetail> {

	@Autowired
	private ExamScoreClassifyService examScoreClassifyService;
	/**
	 * 获取单条数据
	 * @param examScoreDetail
	 * @return
	 */
	@Override
	public ExamScoreDetail get(ExamScoreDetail examScoreDetail) {
		return super.get(examScoreDetail);
	}
	
	/**
	 * 查询分页数据
	 * @param examScoreDetail 查询条件
	 * @param examScoreDetail.page 分页对象
	 * @return
	 */
	@Override
	public Page<ExamScoreDetail> findPage(ExamScoreDetail examScoreDetail) {
		return super.findPage(examScoreDetail);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param examScoreDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ExamScoreDetail examScoreDetail) {
		super.save(examScoreDetail);
	}
	
	/**
	 * 更新状态
	 * @param examScoreDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ExamScoreDetail examScoreDetail) {
		super.updateStatus(examScoreDetail);
	}
	
	/**
	 * 删除数据
	 * @param examScoreDetail
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ExamScoreDetail examScoreDetail) {
		super.delete(examScoreDetail);
	}

	@Transactional(readOnly=false)
	public List<ExamScoreClassify> findData(String examId) {
		return  dao.findData(examId);
	}

	/**
	 * 保存评分项
	 */
	@Transactional(readOnly=false)
	public void saveExamScoreInfo(String examScoreJson,String examId) {
		examScoreJson = examScoreJson.replace("\n","");
		examScoreJson = examScoreJson.replace(" ","");
		JSONObject obj = JSONObject.parseObject(examScoreJson);
		JSONArray recordsArray = obj.getJSONArray("data");
		//判断非空
		if(CollectionUtils.isNotEmpty(recordsArray)){
			for(Object examScoreClassifyJson:recordsArray){
				if(examScoreClassifyJson!=null){
					//得到json数据转换为对应的实体类 保存到哦数据库内
					ExamScoreClassify examScoreClassify = JSONObject.parseObject(examScoreClassifyJson.toString(),ExamScoreClassify.class);
					examScoreClassify.setExamId(examId);
					examScoreClassifyService.save(examScoreClassify);
					//循环分类下 评分项
					List<ExamScoreDetail> examScoreDetailList = examScoreClassify.getItemList();
					if(CollectionUtils.isNotEmpty(examScoreDetailList)){  //非空判断
						for(ExamScoreDetail examScoreDetail:examScoreDetailList){
							//保存关联外键
							examScoreDetail.setScoreClassifyId(examScoreClassify.getId());
							this.save(examScoreDetail);
						}
					}
				}
			}
		}
	}

	//先删除
	@Transactional(readOnly=false)
	public void deleteExamScoreInfo(String examId){
		if(StringUtils.isNotBlank(examId)){
			ExamScoreClassify examScoreClassify = new ExamScoreClassify();
			examScoreClassify.setExamId(examId);
			List<ExamScoreClassify> examScoreClassifyList = examScoreClassifyService.findList(examScoreClassify);
			//如果数组不为空 进行删除
			if(CollectionUtils.isNotEmpty(examScoreClassifyList)){
				dao.deleteExamScoreDetail(examScoreClassifyList);
				examScoreClassifyService.deleteExamScoreClassify(examId);
			}
		}


	}



}