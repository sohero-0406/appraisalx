/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.idgen.IdWorker;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.BaiDuAiUtil;
import com.jeesite.modules.aa.dao.PictureUserDao;
import com.jeesite.modules.aa.entity.PictureType;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.vo.PictureTypeAndUserVO;
import com.jeesite.modules.aa.vo.PictureUserVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 用户图片表Service
 *
 * @author chenlitao
 * @version 2019-06-29
 */
@Service
@Transactional(readOnly = true)
public class PictureUserService extends CrudService<PictureUserDao, PictureUser> {
    @Autowired
    private PictureUserDao pictureUserDao;
    @Autowired
    private PictureTypeService pictureTypeService;

    /**
     * 上传图片并保存和识别图片信息
     * <p>图片默认上传至D:/appraisalPic/exam/考试用户id/</p>
     *
     * @param examUser      考试用户
     * @param picFile       图片文件
     * @param pictureTypeId 图片类型id
     * @param id            原图片id
     * @param needDiscern   是否需要识别，是:1,否:0
     *                      可识别的图片类型包括：身份证正面、车牌照、车辆识别代号（VIN）、
     *                      机动车行驶证正页、机动车登记证1、机动车销售统一发票
     * @return
     */
    @Transactional
    public CommonResult saveAndDiscernPicture(ExamUser examUser, MultipartFile picFile, String id, String pictureTypeId, String needDiscern) throws IOException {
        String examUserId = examUser.getId();
        String paperId = examUser.getPaperId();
        //校验最多传三张图片
        if (StringUtils.isBlank(id)) {
            PictureUser pictureUser = new PictureUser();
            pictureUser.setExamUserId(examUserId);
            pictureUser.setPaperId(paperId);
            pictureUser.setPictureTypeId(pictureTypeId);
            List<PictureUser> list = dao.findList(pictureUser);
            if (CollectionUtils.isNotEmpty(list) && list.size() >= 3) {
                return new CommonResult(CodeConstant.UPLOAD_FAIL_THREE, "上传失败，图片不可多于三张！");
            }
        }

        String url = examUserId;
        if (StringUtils.isBlank(examUserId)) {
            url = paperId;
        }
        if (picFile == null || picFile.isEmpty()) {
            return new CommonResult("1003", "上传失败，请选择文件！");
        }
        //图片默认存储路径，读取picture.properties
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config/picture");
        String prefix = bundle.getString("url");
        String filePath = prefix + "exam/" + url + "/";
        PictureType pictureType = pictureTypeService.get(pictureTypeId);
        String fileName = new IdWorker(-1, -1).nextId() + "";
        String originFileName = picFile.getOriginalFilename();
        String [] originFileNameArray = originFileName.split("\\.");
        int bigSize = originFileNameArray.length-1;
        String fileType = "." + originFileNameArray[bigSize];
        File destFile = new File(filePath + fileName + fileType);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        //执行上传
        picFile.transferTo(destFile);

        //删除原图片
        if (StringUtils.isNotBlank(id)) {
            PictureUser pictureUser = this.get(id);
            String picUrl = pictureUser.getUrl();
            FileUtils.deleteFile(prefix + picUrl);
        }

        //保存图片信息
        PictureUser pictureUser = new PictureUser();
        pictureUser.setId(id);
        pictureUser.setExamUserId(examUserId);
        pictureUser.setPaperId(paperId);
        pictureUser.setPictureTypeId(pictureTypeId);
        pictureUser.setUrl("exam/" + url + "/" + fileName + fileType);
        pictureUser.setName(pictureType.getName());
        this.save(pictureUser);

        PictureUserVO pictureUserVO = new PictureUserVO(pictureUser);

        CommonResult comRes = new CommonResult();
        //识别图片信息
        if ("1".equals(needDiscern)) {
            BaiDuAiUtil baiDuAiUtil = new BaiDuAiUtil();
            String res = baiDuAiUtil.discern(pictureTypeId, filePath + fileName + fileType);
            pictureUserVO.setDetail(res);
        }
        comRes.setData(pictureUserVO);
        return comRes;
    }

    /**
     * 根据考生id和图片父类型id加载图片数据
     *
     * @param examUser      用户
     * @param parentTypeIds 图片父类型ids
     * @return
     */
    public CommonResult findPictureByParentTypeId(ExamUser examUser, String[] parentTypeIds) {
        if (parentTypeIds == null || parentTypeIds.length <= 0) {
            return new CommonResult();
        }
        List<PictureTypeAndUserVO> picTypeAndUserList = pictureUserDao.findVoListByExamUserIdAndParentTypeId(examUser, parentTypeIds);
        if (picTypeAndUserList == null) {
            picTypeAndUserList = new ArrayList<>();
        }
        List<PictureType> picTypeList = pictureTypeService.findListByIds(parentTypeIds);

        List<PictureTypeAndUserVO> picTypeAndUserVOs = new ArrayList<>();
        for (PictureType picType : picTypeList) {
            PictureTypeAndUserVO pictureTypeAndUserVO = new PictureTypeAndUserVO();
            pictureTypeAndUserVO.setParentPictureType(picType);
            List<PictureUser> childPicUserList = new ArrayList<>();
            for (PictureTypeAndUserVO picTypeAndUser : picTypeAndUserList) {
                if (picTypeAndUser == null || picTypeAndUser.getPictureType() == null) {
                    continue;
                }
                if (picType.getId().equals(picTypeAndUser.getPictureType().getParentId())) {
                    childPicUserList.add(picTypeAndUser.getPictureUser());
                }
            }

            pictureTypeAndUserVO.setChildren(childPicUserList);
            picTypeAndUserVOs.add(pictureTypeAndUserVO);
        }
        CommonResult comRes = new CommonResult();
        comRes.setData(picTypeAndUserVOs);
        return comRes;
    }

    /**
     * 查询子项图片
     *
     * @param examUser
     * @param parentTypeIds
     * @return
     */
    public List<PictureUser> findChildPicture(ExamUser examUser, String[] parentTypeIds) {
        return this.pictureUserDao.findListByExamUserIdAndParentTypeId(examUser, parentTypeIds);
    }

    /**
     * 加载图片列表
     *
     * @param examUser       考生用户
     * @param pictureTypeIds 图片类型id
     * @return 如果examUserId为空，返回null，如果pictureTypeIds为空，返回所有类型的图片信息
     */
    public List<PictureUser> findPictureList(ExamUser examUser, String[] pictureTypeIds) {
        return pictureUserDao.findPictureList(examUser, pictureTypeIds);
    }

    /**
     * 获取单条数据
     *
     * @param pictureUser
     * @return
     */
    @Override
    public PictureUser get(PictureUser pictureUser) {
        return super.get(pictureUser);
    }

    /**
     * 查询分页数据
     *
     * @param pictureUser 查询条件
     * @return
     */
    @Override
    public Page<PictureUser> findPage(PictureUser pictureUser) {
        return super.findPage(pictureUser);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param pictureUser
     */
    @Override
    @Transactional(readOnly = false)
    public void save(PictureUser pictureUser) {
        super.save(pictureUser);
    }

    /**
     * 更新状态
     *
     * @param pictureUser
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(PictureUser pictureUser) {
        super.updateStatus(pictureUser);
    }

    /**
     * 删除数据
     *
     * @param pictureUser
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(PictureUser pictureUser) {
        super.delete(pictureUser);
    }

    public PictureUser getByEntity(PictureUser pictureUser) {
        return dao.getByEntity(pictureUser);
    }

    /**
     * 批量删除
     */
    @Transactional(readOnly = false)
    public CommonResult deletePictureUseIds(String ids) {
        String[] pictureIds = ids.split(",");
        //删除文件
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config/picture");
        String prefix = bundle.getString("url");
        PictureUser pictureUser = new PictureUser();
        pictureUser.setId_in(pictureIds);
        List<PictureUser> list = this.findList(pictureUser);
        if (CollectionUtils.isNotEmpty(list)) {
            for (PictureUser user : list) {
                String url = user.getUrl();
                FileUtils.deleteFile(prefix + url);
            }
        }
        //删除数据库
        dao.deletePictureUseIds(pictureIds);
        return new CommonResult();
    }

    /**
     * 删除照片
     *
     * @param id
     */
    @Transactional
    public CommonResult phyDelete(String id) {
        if (StringUtils.isBlank(id)) {
            return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER, "请求参数不全！");
        }
        //删除文件
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config/picture");
        String prefix = bundle.getString("url");
        PictureUser pictureUser = this.get(id);
        String url = pictureUser.getUrl();
        FileUtils.deleteFile(prefix + url);

        //删除数据库
        pictureUser = new PictureUser();
        pictureUser.setId(id);
        dao.phyDelete(pictureUser);
        return new CommonResult();
    }

    /**
     * 保存报告类型
     */
    @Transactional
    public void savePictureWorker(ExamUser examUser, String pictureTypeId, String name) {
        PictureUser pictureUser = new PictureUser();
        pictureUser.setPaperId(examUser.getPaperId());
        pictureUser.setExamUserId(examUser.getId());
        pictureUser.setPictureTypeId(pictureTypeId);
        pictureUser.setName(name);
        if (null == super.dao.getByEntity(pictureUser)) {
            this.save(pictureUser);
        }
    }
}