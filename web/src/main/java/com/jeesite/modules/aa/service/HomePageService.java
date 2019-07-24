package com.jeesite.modules.aa.service;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.Paper;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.aa.vo.HomePageVO;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.entity.VehicleInfo;
import com.jeesite.modules.common.service.ExamUserService;
import com.jeesite.modules.common.service.VehicleInfoService;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class HomePageService {

    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private PictureUserService pictureUserService;
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    @Autowired
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private ExamUserService examUserService;

    /**
     * 加载首页界面(学生)
     */
    public HomePageVO loadHomePageStu(HomePageVO homePageVO) {

        if(StringUtils.isBlank(homePageVO.getSort())){
            //排序规则为空，默认降序
            homePageVO.setSort("2");
        }
        ExamUser examUser = UserUtils.getExamUser();
        HomePageVO result = new HomePageVO();
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getExamId());
        homePageVO.setCarInfo(carInfo);
        carInfo = carInfoService.findCarInfoBySortStu(homePageVO);
        if(carInfo != null){
            BigDecimal mileage = new BigDecimal(carInfo.getMileage());
            carInfo.setMileage(mileage.divide(new BigDecimal("10000"),1,BigDecimal.ROUND_HALF_UP)+"万公里");
            if (StringUtils.isNotBlank(carInfo.getPurchaseDate())) {
                String[] purchaseDate = carInfo.getPurchaseDate().substring(0, 10).split("-");
                carInfo.setPurchaseDate(purchaseDate[0] + "年" + purchaseDate[1] + "月");
            }
            result.setCarInfo(carInfo);

            PictureUser pictureUser = new PictureUser();
            pictureUser.setExamUserId(examUser.getExamId());
            pictureUser.setPictureTypeId("1143439344920567808");
            pictureUser = pictureUserService.getByEntity(pictureUser);
            homePageVO.setPictureUser(pictureUser);
            result.setPictureUser(pictureUser);

            VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
            vehicleGradeAssess.setExamUserId(examUser.getExamId());
            vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
            homePageVO.setVehicleGradeAssess(vehicleGradeAssess);
            result.setVehicleGradeAssess(vehicleGradeAssess);

            VehicleInfo vehicleInfo = new VehicleInfo();
            vehicleInfo.setChexingId(carInfo.getModel());
            vehicleInfo = vehicleInfoService.getCarModel(vehicleInfo);
            homePageVO.setVehicleInfo(vehicleInfo);
            result.setVehicleInfo(vehicleInfo);
        }

        return result;

    }

    /**
     * 加载首页界面(教师)
     */
    public List<HomePageVO> loadHomePageTea(HomePageVO homePageVO) {
        if(StringUtils.isBlank(homePageVO.getSort())){
            //排序规则为空，默认降序
            homePageVO.setSort("2");
        }
        List<HomePageVO> result = new ArrayList<HomePageVO>();
        List<CarInfo> carInfoList = paperService.loadHomePageTea(homePageVO);
        if(carInfoList != null){
            for(CarInfo carInfo: carInfoList){
                HomePageVO temp = new HomePageVO();
                BigDecimal mileage = new BigDecimal(carInfo.getMileage());
                carInfo.setMileage(mileage.divide(new BigDecimal("10000"),1,BigDecimal.ROUND_HALF_UP)+"万公里");
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

                VehicleInfo vehicleInfo = new VehicleInfo();
                vehicleInfo.setChexingId(carInfo.getModel());
                vehicleInfo = vehicleInfoService.getCarModel(vehicleInfo);
                temp.setVehicleInfo(vehicleInfo);

                result.add(temp);
            }
        }
        return result;
    }

    /**
     * 新建试卷
     */
    @Transactional
    public void newPaper() {
        ExamUser examUser = UserUtils.getExamUser();
        if(StringUtils.isNotBlank(examUser.getExamId())){
            //学生
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                examUser.setStartTime(df.parse(df.format(new Date())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            examUser.setExamId(examUser.getExamId());
            examUser.setState(3);
            examUserService.insert(examUser);
        }else {
            //教师
            Paper paper = new Paper();
            paper.setState("1");
            paperService.insert(paper);
        }
    }
}
