package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.Paper;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.aa.vo.HomePageVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamUserService;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class HomePageService {

    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private PictureUserService pictureUserService;
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 加载首页界面(学生)
     */
    public HomePageVO loadHomePageStu(HomePageVO homePageVO) {
        if (StringUtils.isBlank(homePageVO.getSort())) {
            //排序规则为空，默认降序
            homePageVO.setSort("2");
        }
        ExamUser examUser = UserUtils.getExamUser();
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getExamId());
        homePageVO.setCarInfo(carInfo);
        carInfo = carInfoService.findCarInfoBySortStu(homePageVO);
        if (carInfo != null) {
            BigDecimal mileage = new BigDecimal(carInfo.getMileage());
            carInfo.setMileage(mileage.divide(new BigDecimal("10000"), 1, BigDecimal.ROUND_HALF_UP) + "万公里");
            if (StringUtils.isNotBlank(carInfo.getPurchaseDate())) {
                String[] purchaseDate = carInfo.getPurchaseDate().substring(0, 10).split("-");
                carInfo.setPurchaseDate(purchaseDate[0] + "年" + purchaseDate[1] + "月");
            }

            PictureUser pictureUser = new PictureUser();
            pictureUser.setExamUserId(examUser.getExamId());
            pictureUser.setPictureTypeId("1143439344920567808");
            pictureUser = pictureUserService.getByEntity(pictureUser);
            homePageVO.setPictureUser(pictureUser);

            VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
            vehicleGradeAssess.setExamUserId(examUser.getExamId());
            vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
            homePageVO.setVehicleGradeAssess(vehicleGradeAssess);

            Map<String, String> map = new HashMap<>();
            map.put("chexingId", carInfo.getModel());
            CommonResult result = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, map);
            if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
                JSONObject vehicleInfo = JSONObject.parseObject(result.getData().toString());
                homePageVO.setVehicleInfo(vehicleInfo);
            }
        }

        return homePageVO;

    }

    /**
     * 加载首页界面(教师)
     */
    public List<HomePageVO> loadHomePageTea(HomePageVO homePageVO) {
        if (StringUtils.isBlank(homePageVO.getSort())) {
            //排序规则为空，默认降序
            homePageVO.setSort("2");
        }
        List<HomePageVO> list = new ArrayList<>();
        List<CarInfo> carInfoList = paperService.loadHomePageTea(homePageVO);
        if (carInfoList != null) {
            for (CarInfo carInfo : carInfoList) {
                HomePageVO temp = new HomePageVO();
                BigDecimal mileage = new BigDecimal(carInfo.getMileage());
                carInfo.setMileage(mileage.divide(new BigDecimal("10000"), 1, BigDecimal.ROUND_HALF_UP) + "万公里");
                if (StringUtils.isNotBlank(carInfo.getPurchaseDate())) {
                    String[] purchaseDate = carInfo.getPurchaseDate().substring(0, 10).split("-");
                    carInfo.setPurchaseDate(purchaseDate[0] + "年" + purchaseDate[1] + "月");
                }
                temp.setCarInfo(carInfo);

                PictureUser pictureUser = new PictureUser();
                pictureUser.setPaperId(carInfo.getPaperId());
                pictureUser.setPictureTypeId("1143439344920567808");
                pictureUser = pictureUserService.getByEntity(pictureUser);
                temp.setPictureUser(pictureUser);

                VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
                vehicleGradeAssess.setPaperId(carInfo.getPaperId());
                vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
                temp.setVehicleGradeAssess(vehicleGradeAssess);

                Map<String, String> map = new HashMap<>();
                map.put("chexingId", carInfo.getModel());
                CommonResult result = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, map);
                if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
                    JSONObject vehicleInfo = JSONObject.parseObject(result.getData().toString());
                    temp.setVehicleInfo(vehicleInfo);
                }
                list.add(temp);
            }
        }
        return list;
    }

    /**
     * 新建试卷
     */
    @Transactional
    public void newPaper() {
        ExamUser examUser = UserUtils.getExamUser();
        if (StringUtils.isNotBlank(examUser.getId())) {
            //学生
            if (null == examUser.getStartTime()) {
                examUser.setStartTime(new Date());
                examUserService.save(examUser);
                //更新session
                ServletUtils.getRequest().getSession().setAttribute("examUser", examUser);
            }
        } else {
            //教师
            Paper paper = new Paper();
            paper.setState("1");
            paperService.save(paper);
            // 0810 新建考试 教师端 新建完成后将 paperId 传入 session 存储
            ServletUtils.getRequest().getSession().setAttribute("paperId", paperService.get(paper).getId());
        }
    }
}
