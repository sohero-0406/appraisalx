/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.entity.PlaceFile;
import com.jeesite.modules.common.entity.ExamUser;
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

    //查询 接受委托、检查可交易车辆、记录车辆基本信息、判别事故车辆  type=0
    //查询 鉴定技术状况 数据列表 type=1
    List<PictureUser> selectPlaceListFrist(@Param("examUserId") String examUserId ,@Param("paperId") String paperId,@Param("type") String type);

    // 归档数据列表 委托书
    List<PictureUser> selectPlace(ExamUser examUser);

}