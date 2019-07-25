/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.PlaceFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 归档DAO接口
 * @author liangtao
 * @version 2019-07-20
 */
@MyBatisDao
public interface PlaceFileDao extends CrudDao<PlaceFile> {

    //根据试卷id 获取归档内教师选择的 鉴定评估二手车照片
    List<String> getFileByAssessedPicture(String paperId);

    List<String> getPlaceFileByStu(String examUserId);

}