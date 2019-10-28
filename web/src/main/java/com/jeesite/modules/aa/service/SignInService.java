package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.jwt.JwtUtils;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.ExamUserService;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.common.service.OperationLogService;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SignInService {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private ExamService examService;

    /**
     * 登录
     *
     * @param vo
     * @return
     */
    @Transactional
    public CommonResult login(LoginVO vo) {
        String userName = vo.getUserName();
        String password = vo.getPassword();
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", password);
        CommonResult result = httpClientService.post(ServiceConstant.COMMONUSER_TEACHER_SIDE_LOGIN, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
            return result;
        }
        JSONObject data = JSONObject.parseObject(result.getData().toString());
        if (StringUtils.isBlank(data.toString())) {
            return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER, "用户不存在!");
        }
        String userId = data.getString("id");
        String roleId = data.getString("roleId");
        String trueName = data.getString("trueName");
        String token = JwtUtils.generateToken(userId);
        String isExamRight = data.getString("isExamRight");
        ExamUser examUser = new ExamUser();
        Map<String, Object> returnMap = new HashMap<>();
        //教师
        if ("2".equals(roleId)) {
            examUser.setUserId(userId);
            examUser.setRoleType(roleId);
            examUser.setTrueName(trueName);
            examUser.setUserNum(userName);
            examUser.setToken(token);
            examUser.setIsExamRight(isExamRight);
            CacheUtils.put("examUser", examUser.getUserId(), examUser);
            returnMap.put("roleType", isExamRight);
            returnMap.put("token", token);
            returnMap.put("role", "2");
            returnMap.put("trueName", trueName);
            return new CommonResult(returnMap);
        }
        //学生
        if ("3".equals(roleId)) {
            examUser.setUserId(userId);
            examUser = examUserService.getAllowLogin(examUser);
            if (null == examUser) {
                return new CommonResult(CodeConstant.EXAM_NO_ONGOING, "不存在正在进行的考试");
            }
            if (null != examUser.getEndTime()) {
                return new CommonResult(CodeConstant.EXAM_END, "考试已结束");
            }
            //判断考试是否已结束
            Exam exam = examService.get(examUser.getExamId());
            if ("1".equals(exam.getExamType())) {
                Calendar calendar = Calendar.getInstance();
                if (null != examUser.getStartTime()) {
                    calendar.setTime(examUser.getStartTime());
                }
                calendar.add(Calendar.MINUTE, exam.getDuration());
                //考试已结束
                Date nowDate = new Date();
                if (calendar.getTime().compareTo(nowDate) < 0) {
                    examUser.setEndTime(nowDate);
                    examUserService.save(examUser);
                    return new CommonResult(CodeConstant.EXAM_END, "考试已结束");
                }
            }
            //添加考生开始时间
            if (null == examUser.getStartTime()) {
                examUser.setStartTime(new Date());
            } else {
                examUser.setStartTime(examUser.getStartTime());
            }
            examUserService.save(examUser);
            ExamUser sessionUser = new ExamUser();
            sessionUser.setId(examUser.getId());
            sessionUser.setUserId(examUser.getUserId());
            sessionUser.setExamId(examUser.getExamId());
            sessionUser.setRoleType(roleId);
            sessionUser.setTrueName(trueName);
            sessionUser.setUserNum(userName);
            sessionUser.setToken(token);
            CacheUtils.put("examUser", examUser.getUserId(), sessionUser);
            operationLogService.saveObj(sessionUser, "登录成功");
            returnMap.put("token", token);
            returnMap.put("role", "1");
            returnMap.put("trueName", trueName);
            return new CommonResult(returnMap);
        }
        return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER, "角色未识别，不允许登录!");
    }

    /**
     * 生成登陆二维码
     *
     * @param url
     * @param width
     * @param height
     * @param logoPath
     * @return
     */
    public String generateQrCode(String url, int width, int height, String logoPath) {
        String resultImage = "";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);  //设置白边
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image, "png", os);
            resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        resultImage = writeToFile(bitMatrix, logoPath, os);

        return resultImage;
    }


    /**
     * @param matrix   二维码矩阵相关
     * @param logoPath logo路径
     * @throws IOException
     */
    public static String writeToFile(BitMatrix matrix, String logoPath, ByteArrayOutputStream os) {
        BufferedImage image = toBufferedImage(matrix);
        Graphics2D gs = image.createGraphics();

        int ratioWidth = image.getWidth() * 2 / 10;
        int ratioHeight = image.getHeight() * 2 / 10;

        try {
            //载入logo
            Image img = ImageIO.read(new File(logoPath));
            int logoWidth = img.getWidth(null) > ratioWidth ? ratioWidth : img.getWidth(null);
            int logoHeight = img.getHeight(null) > ratioHeight ? ratioHeight : img.getHeight(null);

            int x = (image.getWidth() - logoWidth) / 2;
            int y = (image.getHeight() - logoHeight) / 2;

            gs.drawImage(img, x, y, logoWidth, logoHeight, null);
            gs.setColor(Color.black);
            gs.setBackground(Color.WHITE);
            gs.dispose();
            ImageIO.write(image, "png", os);
            String resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));
            return resultImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    //判断考生是否存在 type 判断学生、老师
    public CommonResult judgmentExist(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", userId);
        CommonResult result = httpClientService.post(ServiceConstant.DERIVE_STUDENT_LOADCOMMONUSER, map);
        return result;
    }
}
