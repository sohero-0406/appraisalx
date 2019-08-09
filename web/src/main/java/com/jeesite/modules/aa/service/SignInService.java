package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SignInService {

    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 考生端登录
     *
     * @param vo
     * @return
     */
    @Transactional
    public CommonResult stuLogin(LoginVO vo) {
        String userName = vo.getUserName();
        String password = vo.getPassword();
        Map<String, String> map = new HashMap<>();
        map.put("userName", vo.getUserName());
        map.put("password", vo.getPassword());
        CommonResult result = httpClientService.post(ServiceConstant.COMMONUSER_TEACHER_SIDE_LOGIN, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
            return result;
        }
        JSONObject data = JSONObject.parseObject(result.getData().toString());
        String userId = data.getString("id");
        ExamUser examUser = new ExamUser();
        examUser.setUserId(userId);
        examUser = examUserService.getAllowLogin(examUser);
        if (null == examUser) {
            return new CommonResult(CodeConstant.EXAM_NO_ONGOING,"不存在正在进行的考试");
        }
        ExamUser sessionUser = new ExamUser();
        sessionUser.setId(examUser.getId());
        sessionUser.setUserId(examUser.getUserId());
        sessionUser.setExamId(examUser.getExamId());
        sessionUser.setStartTime(examUser.getStartTime());
        ServletUtils.getRequest().getSession().setAttribute("examUser", sessionUser);
        operationLogService.saveObj(sessionUser, "登录成功");
        return new CommonResult();
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
        } catch (WriterException e) {
            e.printStackTrace();
        }
        resultImage = writeToFile(bitMatrix, logoPath, os);
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

    //判断考生是否存在
    public Boolean judgmentExist(ExamUser examUser) {
        ExamUser user = examUserService.getByEntity(examUser);
        if (null == user) {
            return true; //用户不存在
        } else {
            return false;//用户存在
        }
    }


}
