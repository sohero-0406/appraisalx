/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.PaperDao;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.Paper;
import com.jeesite.modules.aa.vo.HomePageVO;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 试卷Service
 * @author lvchangwei
 * @version 2019-07-16
 */
@Service
@Transactional(readOnly=true)
public class PaperService extends CrudService<PaperDao, Paper> {

	@Autowired
	private PaperDao paperDao;

	@Autowired
	private ExamService examService;
	
	/**
	 * 获取单条数据
	 * @param paper
	 * @return
	 */
	@Override
	public Paper get(Paper paper) {
		return super.get(paper);
	}
	
	/**
	 * 查询分页数据
	 * @param paper 查询条件
	 * @return
	 */
	@Override
	public Page<Paper> findPage(Paper paper) {
		return super.findPage(paper);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param paper
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(Paper paper) {
		super.save(paper);
	}
	
	/**
	 * 更新状态
	 * @param paper
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(Paper paper) {
		super.updateStatus(paper);
	}
	
	/**
	 * 删除数据
	 * @param paper
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(Paper paper) {
		super.delete(paper);
	}

	/**
	 * 查询试卷列表
	 * @return
	 */
    public List<Paper> findPaper(Paper paper) {
        return paperDao.findPaper(paper);
	}

	/**
	 * 加载首页界面(教师端)
	 */
	public List<CarInfo> loadHomePageTea(HomePageVO homePageVO) {
		Map<String,String> hs = new HashMap<>();
		hs.put("queryCriteria",homePageVO.getQueryCriteria());
		hs.put("sort",homePageVO.getSort());
		return paperDao.findPaperBySortTea(hs);
	}

	/**
	* @description: 加载考试信息
	* @param: [exam]
	* @return: com.jeesite.modules.common.entity.Exam
	* @author: Jiangyf
	* @date: 2019/8/10
	* @time: 16:37
	*/
    public List<Exam> findExamForCheck(Exam exam) {
        return examService.findExamForCheck(exam);
    }

    public List<Paper> selectExamPaperList(Paper paper) {
        return dao.selectExamPaperList(paper);
    }

	@Transactional
	public void deletePaper(String id) {
		String[] idList = id.split(",");
		dao.deletePaper(idList);
	}
}