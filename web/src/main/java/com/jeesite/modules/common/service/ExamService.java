/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.ExamDetail;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import com.jeesite.modules.aa.entity.Paper;
import com.jeesite.modules.aa.service.ExamDetailService;
import com.jeesite.modules.aa.service.ExamScoreDetailService;
import com.jeesite.modules.aa.service.ExamScoreInfoService;
import com.jeesite.modules.aa.service.PaperService;
import com.jeesite.modules.aa.vo.ExamVO;
import com.jeesite.modules.common.dao.ExamDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.vo.AddStudentReturnVO;
import com.jeesite.modules.common.vo.ExamUserVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;

/**
 * common_examService
 *
 * @author lvchangwei
 * @version 2019-07-10
 */
@Service
@Transactional(readOnly = true)
public class ExamService extends CrudService<ExamDao, Exam> {

    @Autowired
    private ExamScoreDetailService examScoreDetailService;
    @Autowired
    private ExamDetailService examDetailService;
    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private ExamScoreInfoService examScoreInfoService;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private PaperService paperService;

    /**
     * 获取单条数据
     *
     * @param exam
     * @return
     */
    @Override
    public Exam get(Exam exam) {
        return super.get(exam);
    }

    /**
     * 查询分页数据
     *
     * @param exam 查询条件
     * @return
     */
    @Override
    public Page<Exam> findPage(Exam exam) {
        return super.findPage(exam);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param exam
     */
    @Override
    @Transactional(readOnly = false)
    public void save(Exam exam) {
        super.save(exam);
    }

    /**
     * 更新状态
     *
     * @param exam
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Exam exam) {
        super.updateStatus(exam);
    }

    /**
     * 删除数据
     *
     * @param exam
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Exam exam) {
        super.delete(exam);
    }

    /**
     * 获取考试信息
     * keyword 搜索关键字
     */
    @Transactional(readOnly = false)
    public List<Exam> getExamInfo(String keyword, String type) {
        return dao.getExamInfo(keyword, type);
    }

    /**
     * 保存、修改  考试/练习功能
     */
    @Transactional(readOnly = false)
    public CommonResult saveExamInfo(ExamVO examVO, String examScoreJson, String studentJson) {
        CommonResult comRes = new CommonResult();
        Exam exam = examVO.getExam();
        if (null == exam) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("请输入考试信息");
            return comRes;
        }
        //如果考试类型为空 或者考试类型参数不符合规范 都定义为保存考试失败
        if (StringUtils.isBlank(exam.getType())) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("请求参数异常");
            return comRes;
        } else if (!("1".equals(exam.getType()) || "2".equals(exam.getType()))) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("请求参数异常");
            return comRes;
        }
        if (StringUtils.isBlank(exam.getPaperId())) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("请选择试卷");
            return comRes;
        }
        if (StringUtils.isBlank(exam.getName())) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("名称不能为空");
            return comRes;
        }
        if (StringUtils.isBlank(exam.getExamType())) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("练习计时不能为空!");
            return comRes;
        }
        //内容模块选择不能为空
        if (null == examVO.getExamDetail()) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("内容模块选择不能为空");
            return comRes;
        }
        //分值设定不能为空
        if (StringUtils.isBlank(examScoreJson)) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("分值设定不能为空");
            return comRes;
        }
        //考试学生不能为空
        if (StringUtils.isBlank(studentJson)) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
            comRes.setMsg("考试学生不能为空");
            return comRes;
        }

        //考试id
        String examId = exam.getId();
        //判断新建/修改
        if (StringUtils.isNotBlank(examId)) {
            //修改
            if (StringUtils.isBlank(exam.getState())) { //判断考试状态不能为空
                comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
                comRes.setMsg("考试状态不能为空");
                return comRes;
            }
            if (!"1".equals(exam.getState())) { //除新建状态下的数据 其余不可修改
                comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);//请求参数有误
                comRes.setMsg("此状态下的考试不能修改");
                return comRes;
            }
            //如果考试详情未编辑
            if (StringUtils.isBlank(examVO.getExamDetail().getId())) {
                ExamDetail detail = new ExamDetail();
                detail.setExamId(examId);
                examVO.getExamDetail().setId(examDetailService.getByEntity(detail).getId());
            }
        } else {
            examVO.getExam().setState("1");
        }
        super.save(exam);
        String saveExamId = exam.getId();
        //保存--内容模块选择
        examDetailService.saveExamInfoDetail(saveExamId, examVO.getExamDetail());
        //判断如果为 修改评分项 则先清空数据，在进行保存
        if (StringUtils.isNotBlank(examId)) {
            //先删除分值项
            examScoreDetailService.deleteExamScoreInfo(examId);
        }
        //保存--分值设定
        examScoreDetailService.saveExamScoreInfo(examScoreJson, saveExamId);
        //保存--学生
        examUserService.saveExamUser(studentJson, saveExamId);

        return comRes;
    }

    //新建/修改考试
    @Transactional
    public CommonResult addOrUpdateExam(String examId) {
        CommonResult comRes = new CommonResult();
        Map<String, Object> returnMap = new HashMap();
        //考试评分项
        List<ExamScoreClassify> examScoreInfoList = examScoreInfoService.getExamScoreInfo(examId);
        //内容模板选择
        ExamDetail examDetail = examDetailService.getExamInfoDetail(examId);
        Exam exam = new Exam();
        //学生
        if (StringUtils.isNotBlank(examId)) {
            exam.setId(examId);
            exam = dao.getByEntity(exam);
            //查询已选考生信息
            ExamUser examUser = new ExamUser();
            examUser.setExamId(exam.getId());

            StringBuilder studentUserIds = new StringBuilder();
            List<ExamUser> examUserList = examUserService.findList(examUser);
            int len = examUserList.size();
            if (CollectionUtils.isNotEmpty(examUserList)) {
                Map<String, String> map = new HashMap<>();
                if ("1".equals(exam.getType())) {
                    for (int i = 0; i < len; i++) {
                        if (i == len - 1) {
                            studentUserIds.append(examUserList.get(i).getServerExamUserId());
                        } else {
                            studentUserIds.append(examUserList.get(i).getServerExamUserId()).append(",");
                        }
                    }
                    map.put("serverExamStuId", examUserList.get(0).getServerExamUserId());
                    CommonResult result = httpClientService.post(ServiceConstant.COMMONASSESSMENTSTU_LOAD_ONE_EXAM_STU, map);
                    if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
                        JSONObject object = JSONObject.parseObject(result.getData().toString());
                        returnMap.put("assessmentName", object.getString("assessmentName"));
                        returnMap.put("assessmentDate", object.getString("assessmentDate"));
                        returnMap.put("assessmentTime", object.getString("assessmentTime"));
                    }

                    returnMap.put("examUserList",
                            this.getExamUserList(studentUserIds, examUserList, ServiceConstant.COMMONUSER_LOAD_STU_LIST_BY_EXAM_USER_IDS, "examUserIds"));

                }
                if ("2".equals(exam.getType())) {
                    for (int i = 0; i < len; i++) {
                        if (i == len - 1) {
                            studentUserIds.append(examUserList.get(i).getUserId());
                        } else {
                            studentUserIds.append(examUserList.get(i).getUserId()).append(",");
                        }
                    }
                    returnMap.put("examUserList",
                            this.getExamUserList(studentUserIds, examUserList, ServiceConstant.DERIVE_STUDENT_ACHIEVEMENT, "ids"));

                }
            }
        }
        returnMap.put("examScoreJson", examScoreInfoList);
        returnMap.put("examDetail", examDetail);
        returnMap.put("exam", exam);
        comRes.setData(returnMap);
        return comRes;
    }


    /**
     * 获取考生信息
     *
     * @return
     */
    public Object getExamUserList(StringBuilder studentUserIds, List<ExamUser> examUserList, String connectionPath, String parameter) {
        Map<String, String> map = new HashMap<>();
        map.put(parameter, studentUserIds.toString());
        //调取到平台 获取不符合规范的考生
        CommonResult result = httpClientService.post(connectionPath, map);
        if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
            JSONArray array = JSONArray.parseArray(result.getData().toString());
            for (ExamUser user : examUserList) {
                for (Object object : array) {
                    JSONObject platformUser = (JSONObject) object;
                    if (platformUser.getString("id").equals(user.getUserId())) {
                        user.setUserNum(platformUser.getString("userName"));
                        user.setTrueName(platformUser.getString("trueName"));
                        user.setSchoolName(platformUser.getString("schoolName"));
                        user.setMajorName(platformUser.getString("majorName"));
                        user.setClassName(platformUser.getString("className"));
                        user.setGender(platformUser.getString("gender"));
                        break;
                    }
                }
            }
        }
        return examUserList;
    }


    //删除考试 (考试id，考试状态)
    @Transactional(readOnly = false)
    public CommonResult deleteExam(Exam exam) {
        CommonResult comRes = new CommonResult();
        if (null == exam && StringUtils.isBlank(exam.getId())) {
            comRes.setMsg("请先选择考试!");
            return comRes;
        } else if ("".equals(exam.getState()) || null == exam.getState() || (!"1".equals(exam.getState()))) {
            comRes.setMsg("此状态下的考试不能被删除!");
            return comRes;
        }
        //删除考试
        dao.delete(exam);
        //删除 评分项
        examScoreDetailService.deleteExamScoreInfo(exam.getId());
        //删除内容模板
        examDetailService.deleteExamDetail(exam.getId());
        return comRes;
    }

    ;


    @Transactional(readOnly = false)
    public Exam getByEntity(Exam exam) {
        return dao.getByEntity(exam);
    }

    /**
     * 修改考试状态
     * 参数
     * 考试id 和
     * 试卷id paperId
     */
    @Transactional(readOnly = false)
    public CommonResult updateExamSate(Exam exam) {
        //考试状态 state '状态（1:未开始;3:考试中;5:未统计;7:已出分）
        CommonResult comRes = new CommonResult();
        Exam examUpdate = dao.getByEntity(exam);
        if (null == examUpdate) {
            comRes.setCode(CodeConstant.DATA_NOT_FOUND);
            comRes.setMsg("您所查询的考试不存在!");
            return comRes;
        }
        switch (examUpdate.getState()) {
            case "1"://未开始
                List<String> userIdList = dao.getUserByExamId(examUpdate.getId());
                comRes = examUserService.getExamStateByUserId(userIdList, examUpdate);
                if (CodeConstant.REQUEST_SUCCESSFUL.equals(comRes.getCode())) {
                    examUpdate.setState("3");
                    examUpdate.setStartTime(new Date());  //记录考试结束时
                    super.save(examUpdate);
                }
                break;
            case "3"://考试中
                examUpdate.setState("5");
                examUpdate.setEndTime(new Date());  //记录考试结束时
                super.save(examUpdate);
                examUserService.saveExamEndTime(examUpdate.getId());
                break;
            case "5"://未统计
                examUpdate.setState("7");
                examUserService.gradePapers(examUpdate);
                super.save(examUpdate);
                break;
            case "7"://已出分
                comRes.setMsg("考试结果已出！");
                break;
            default:
                comRes.setMsg("未查询到此时卷！");
        }
        return comRes;
    }


    /**
     * @description: 结束考试 - 教师端
     * @param: [paper]
     * @return: void
     * @author: Jiangyf
     * @date: 2019/8/10
     * @time: 16:09
     */
    public void endExamTea(Paper paper) {
        paperService.save(paper);
    }

    public CommonResult uploadScore(String examId) {
        ExamUser examUser = new ExamUser();
        examUser.setExamId(examId);
        List<ExamUser> list = examUserService.findList(examUser);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("softwareId", "7");
        JSONArray jsonArray = new JSONArray();
        for (ExamUser user : list) {
            JSONObject object = new JSONObject();
            object.put("serverExamStuId", user.getServerExamUserId());
            object.put("commonUserId", user.getUserId());
            object.put("score", user.getScore());
            jsonArray.add(object);
        }
        jsonObject.put("scores", jsonArray);
        Map<String, String> map = new HashMap<>();
        map.put("scoreInfo", jsonObject.toString());
        CommonResult result = httpClientService.post(ServiceConstant.COMMONASSESSMENT_UPLOAD_SCORES, map);
        return result;
    }

    public CommonResult findExamUser(ExamUser examUser, ExamUserVO vo) {
        String userId = examUser.getUserId();
        String examId = vo.getExamId();
        String type = vo.getType();
        Map<String, String> map = new HashMap<>();
        map.put("commonUserId", userId);
        //考试
        if ("1".equals(type)) {
            if (StringUtils.isBlank(vo.getAssessmentName()) ||
                    StringUtils.isBlank(vo.getAssessmentDate()) ||
                    StringUtils.isBlank(vo.getAssessmentTime())) {
                return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER, "请求参数不全！");
            }
            map.put("examOrPractice", "exam");
            map.put("assessmentName", vo.getAssessmentName());
            map.put("assessmentDate", vo.getAssessmentDate());
            map.put("assessmentTime", vo.getAssessmentTime());
        }
        //练习
        if ("2".equals(type)) {
            map.put("examOrPractice", "practice");
            map.put("majorName", vo.getMajorName());
            map.put("className", vo.getClassName());
        }
        CommonResult result = httpClientService.post(ServiceConstant.COMMONUSER_LOAD_STU_LIST_IN_PLATFORM, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
            return result;
        }
        List<ExamUser> platFormUserlist = new ArrayList<>();
        JSONArray array = JSONArray.parseArray(result.getData().toString());
        for (Object object : array) {
            JSONObject platformUser = (JSONObject) object;
            ExamUser user = new ExamUser();
            user.setUserId(platformUser.getString("id"));
            user.setUserNum(platformUser.getString("userName"));
            user.setTrueName(platformUser.getString("trueName"));
            user.setMajorName(platformUser.getString("majorName"));
            user.setClassName(platformUser.getString("className"));
            user.setGender(platformUser.getString("gender"));
            user.setIsSelect(false);
            if ("1".equals(type)) {
                user.setSchoolName(platformUser.getString("schoolName"));
                user.setServerExamUserId(platformUser.getString("commonAssessmentStuId"));
            }
            platFormUserlist.add(user);
        }

        //对已有学生做勾选
        if (StringUtils.isNotBlank(examId)) {
            ExamUser examUser1 = new ExamUser();
            examUser1.setExamId(examId);
            List<ExamUser> examUserList = examUserService.findList(examUser1);
            for (ExamUser user : examUserList) {
                for (ExamUser platformUser : platFormUserlist) {
                    if (platformUser.getUserId().equals(user.getUserId())) {
                        platformUser.setIsSelect(true);
                        break;
                    }
                }
            }
        }
        return new CommonResult(platFormUserlist);
    }
}