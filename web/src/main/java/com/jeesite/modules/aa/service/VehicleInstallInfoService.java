/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.VehicleInstallInfoDao;
import com.jeesite.modules.aa.entity.VehicleInstallInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.vo.VehicleInstallVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.OperationLogService;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 车辆加装信息Service
 *
 * @author chenlitao
 * @version 2019-07-03
 */
@Service
@Transactional(readOnly = true)
public class VehicleInstallInfoService extends CrudService<VehicleInstallInfoDao, VehicleInstallInfo> {

	@Autowired
	private VehicleInstallInfoDao vehicleInstallInfoDao;
	@Autowired
	private OperationLogService operationLogService;

	/**
	 * 根据考生id加载车辆加装信息
	 * @param examUser 考生
	 * @return
	 */
	public CommonResult findList(ExamUser examUser){
		List<DictData> ddList = DictUtils.getDictList("aa_vehicle_install_type");
		VehicleInstallInfo vehicleInstallInfo = new VehicleInstallInfo();
		vehicleInstallInfo.setExamUserId(examUser.getId());
		vehicleInstallInfo.setPaperId(examUser.getPaperId());
		List<VehicleInstallInfo> vehicleInstallInfoList = this.findList(vehicleInstallInfo);
		List<VehicleInstallVO> vehicleInstallVOList = new ArrayList<>();
		VehicleInstallVO vehicleInstallVO;
		for(DictData dd : ddList){
			vehicleInstallVO = new VehicleInstallVO();
			vehicleInstallVO.setProjectName(dd.getDictLabel());
			vehicleInstallVO.setProject(dd.getDictValue());
			vehicleInstallVO.setSort(dd.getTreeSort());
			for(VehicleInstallInfo vii : vehicleInstallInfoList){
				if(vii == null || vii.getProject() == null || vii.getProject().trim().length() <= 0){
					continue;
				}
				if(dd.getDictValue().equals(vii.getProject())){
					vehicleInstallVO.setVehicleInstallId(vii.getId());
					vehicleInstallVO.setSelected(true);
					break;
				}
			}
			vehicleInstallVOList.add(vehicleInstallVO);
		}
		CommonResult comRes = new CommonResult();
		comRes.setData(vehicleInstallVOList);
		return comRes;
	}

    /**
     * 保存加装信息
     *
     * @param examUser
     * @param vehicleInstallVOList 待保存的加装信息
     * @return
     */
    @Transactional
    public CommonResult saveAndDelete(ExamUser examUser, List<VehicleInstallVO> vehicleInstallVOList) {
        //保证有用户登录，防止全表删除
        if (StringUtils.isNotBlank(examUser.getId()) || StringUtils.isNotBlank(examUser.getPaperId())) {
            VehicleInstallInfo vehicleInstallInfo = new VehicleInstallInfo();
            vehicleInstallInfo.setExamUserId(examUser.getId());
            vehicleInstallInfo.setPaperId(examUser.getPaperId());
            dao.deleteEntity(vehicleInstallInfo);
            //执行新增
            for (VehicleInstallVO vivo : vehicleInstallVOList) {
                VehicleInstallInfo vii = new VehicleInstallInfo();
                vii.setExamUserId(examUser.getId());
                vii.setPaperId(examUser.getPaperId());
                vii.setProject(vivo.getProject());
                this.save(vii);
            }
            operationLogService.saveObj(examUser,"保存车辆加装信息成功");
        } else {
            return new CommonResult(CodeConstant.LOGIN_TIMEOUT,"登录人信息丢失，请重新登录！");
        }
        return new CommonResult();
    }

	/**
	 * 获取单条数据
	 * @param vehicleInstallInfo
	 * @return
	 */
	@Override
	public VehicleInstallInfo get(VehicleInstallInfo vehicleInstallInfo) {
		return super.get(vehicleInstallInfo);
	}
	
	/**
	 * 查询分页数据
	 * @param vehicleInstallInfo 查询条件
	 * @param vehicleInstallInfo.page 分页对象
	 * @return
	 */
	@Override
	public Page<VehicleInstallInfo> findPage(VehicleInstallInfo vehicleInstallInfo) {
		return super.findPage(vehicleInstallInfo);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param vehicleInstallInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(VehicleInstallInfo vehicleInstallInfo) {
		super.save(vehicleInstallInfo);
	}
	
	/**
	 * 更新状态
	 * @param vehicleInstallInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(VehicleInstallInfo vehicleInstallInfo) {
		super.updateStatus(vehicleInstallInfo);
	}
	
	/**
	 * 删除数据
	 * @param vehicleInstallInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(VehicleInstallInfo vehicleInstallInfo) {
		super.delete(vehicleInstallInfo);
	}

	/**
	 * 依据 考生id 或者试卷id 获取车辆加装信息
	 */
	@Transactional(readOnly=false)
	public String getVehicleInstallName(String examUserId,String paperId) {
		List<Map<String,String>> nameList =  dao.getVehicleInstallName(examUserId,paperId);
		StringBuilder projectName = new StringBuilder();
		int len = nameList.size();
		for(int i=0;i<len;i++){
			if(i==(len-1)){
				projectName.append(nameList.get(i).get("name"));
			}else{
				projectName.append(nameList.get(i).get("name")+"、");
			}
		}
		return projectName.toString();
	}

    /**
     * 查询车辆加装信息
     * @param vehicleInstallInfo
     * @return
     */
    public String getProject(VehicleInstallInfo vehicleInstallInfo) {
        return vehicleInstallInfoDao.getProject(vehicleInstallInfo);
    }
}