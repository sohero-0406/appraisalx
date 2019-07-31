/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.util.List;

/**
 * 用户图片表Entity
 * @author chenlitao
 * @version 2019-06-29
 */
@Table(name="aa_picture_user", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="url", attrName="url", label="图片路径"),
		@Column(name="exam_user_id", attrName="examUserId", label="外键id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="picture_type_id", attrName="pictureTypeId", label="图片类型id"),
		@Column(name="name", attrName="name", label="图片名称", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class PictureUser extends PreEntity<PictureUser> {
	
	private static final long serialVersionUID = 1L;
	private String url;		// 图片路径
	private String examUserId;		// 外键id
	private String paperId;		// 试卷id
	private String pictureTypeId;		// 图片类型id
	private String name;		// 图片名称

	private List<PictureUser> itemList;
	private String choose;  //是否选择
	private String pictureId; //图片id
	
	public PictureUser() {
		this(null);
	}

	public PictureUser(String id){
		super(id);
	}
	
	@Length(min=0, max=1024, message="图片路径长度不能超过 1024 个字符")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getExamUserId() {
		return examUserId;
	}

	public void setExamUserId(String examUserId) {
		this.examUserId = examUserId;
	}
	
	@Length(min=0, max=64, message="试卷id长度不能超过 64 个字符")
	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	
	@Length(min=0, max=64, message="图片类型id长度不能超过 64 个字符")
	public String getPictureTypeId() {
		return pictureTypeId;
	}

	public void setPictureTypeId(String pictureTypeId) {
		this.pictureTypeId = pictureTypeId;
	}
	
	@Length(min=0, max=128, message="图片名称长度不能超过 128 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PictureUser> getItemList() {
		return itemList;
	}

	public void setItemList(List<PictureUser> itemList) {
		this.itemList = itemList;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}
}