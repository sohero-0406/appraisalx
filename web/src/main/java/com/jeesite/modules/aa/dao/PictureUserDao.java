/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.vo.PictureTypeAndUserVO;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户图片表DAO接口
 *
 * @author chenlitao
 * @version 2019-06-29
 */
@MyBatisDao
public interface PictureUserDao extends CrudDao<PictureUser> {
    /**
     * 加载图片列表
     *
     * @param examUser       考生用户
     * @param pictureTypeIds 图片类型代号
     * @return 返回所有类型的图片信息
     */
    List<PictureUser> findPictureList(@Param("examUser") ExamUser examUser, @Param("pictureTypeIds") String[] pictureTypeIds);

    /**
     * 根据考试id和父图片类型ids加载图片信息
     *
     * @param examUser
     * @param parentTypeIds
     * @return
     */
    List<PictureTypeAndUserVO> findVoListByExamUserIdAndParentTypeId(@Param("examUser") ExamUser examUser, @Param("parentTypeIds") String[] parentTypeIds);

    List<PictureTypeAndUserVO> findVoListByExamUserIdAndParentTypeIdTwo(@Param("examUser") ExamUser examUser, @Param("pictureTypeId") String pictureTypeId);


    /**
     * 根据考试id和父图片类型ids加载图片信息
     *
     * @param examUser
     * @param parentTypeIds
     * @return
     */
    List<PictureUser> findListByExamUserIdAndParentTypeId(@Param("examUser") ExamUser examUser, @Param("parentTypeIds") String[] parentTypeIds);

    /**
     * 批量删除
     */
    void deletePictureUseIds(@Param("ids") String[] ids);

    List<PictureUser> findTeaImg(@Param("examUser") ExamUser examUser, @Param("parentId") String parentId);

    void deleteCopyImg(@Param("examUserId") String examUserId, @Param("parentId") String parentId);
}