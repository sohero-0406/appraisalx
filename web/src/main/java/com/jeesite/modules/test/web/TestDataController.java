/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.test.web;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.utils.download.DownloadWordUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2Cache;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.service.TestDataService;
import sun.misc.BASE64Encoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

/**
 * 测试数据Controller
 * @author lvchangwei
 * @version 2019-06-18
 */
@Controller
@RequestMapping(value = "${adminPath}/test/testData")
public class TestDataController extends BaseController {

	@Autowired
	private TestDataService testDataService;

	private static final int BLACK = 0xFF000000;
	//二维码颜色
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public TestData get(String id, boolean isNewRecord) {
		return testDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = {"list", ""})
	public String list(TestData testData, Model model) {
		model.addAttribute("testData", testData);
		return "modules/test/testDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<TestData> listData(TestData testData, HttpServletRequest request, HttpServletResponse response) {
		testData.setPage(new Page<>(request, response));
		Page<TestData> page = testDataService.findPage(testData);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("test:testData:view")
	@RequestMapping(value = "form")
	public String form(TestData testData, Model model) {
		model.addAttribute("testData", testData);
		return "modules/test/testDataForm";
	}

	/**
	 * 保存测试数据
	 */
	@RequiresPermissions("test:testData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated TestData testData) {
		testDataService.save(testData);
		return renderResult(Global.TRUE, text("保存测试数据成功！"));
	}
	
	/**
	 * 删除测试数据
	 */
	@RequiresPermissions("test:testData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(TestData testData) {
		testDataService.delete(testData);
		return renderResult(Global.TRUE, text("删除测试数据成功！"));
	}


	/**
	 *
	 * @param testData
	 * @return
	 */
	@RequestMapping(value = "getDate")
	@ResponseBody
	public CommonResult  getDate(TestData testData) {
		CommonResult comRes = new CommonResult();

		String text = "http://fanyi.youdao.com";
		int width  = 300;
		int height = 300;
		String outPutPath = "D:/qrcode.jpg";
		String imageType = "jpg";
		Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
		//设置编码字符集
		his.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
		  //1、生成二维码
		  BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);

		  //2、获取二维码宽高
		  int codeWidth = encode.getWidth();
		  int codeHeight = encode.getHeight();

		  //3、将二维码放入缓冲流
		  BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
		  for (int i = 0; i < codeWidth; i++) {
			for (int j = 0; j < codeHeight; j++) {
			  //4、循环将二维码内容定入图片
			  image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
			}
		  }
		  File outPutImage = new File(outPutPath);
		  //如果图片不存在创建图片
		  if(!outPutImage.exists())
			outPutImage.createNewFile();
		  //5、将二维码写入图片
		  ImageIO.write(image, imageType, outPutImage);
		} catch (WriterException e) {
		  e.printStackTrace();
		  System.out.println("二维码生成失败");
		} catch (IOException e) {
		  e.printStackTrace();
		  System.out.println("生成二维码图片失败");
		}
		return comRes;
	}


	@RequestMapping(value = "getDate3")
	@ResponseBody
	public CommonResult  getDate3(String uuid) {
		ExamUser examUser = UserUtils.getExamUser();
		CacheUtils.put("sysCache",uuid,examUser);
		return new CommonResult();
	}

	@RequestMapping(value = "getUUid")
	@ResponseBody
	public CommonResult  getUUid(String uuid) {
		CommonResult comRes = new CommonResult();
		ExamUser examUser = CacheUtils.get(uuid);
		comRes.setData(examUser);
		ServletUtils.getRequest().setAttribute("examUser",examUser);
		return comRes;
	}


	@RequestMapping(value = "getUUid11")
	@ResponseBody
	public String  getUUid11() throws IOException, WriterException {
		String uuid = UUID.randomUUID().toString();
		System.out.println("生成uuid"+uuid);
		String text = "localhost:8980/appraisal/test/testData/getDate3"+"?uuid="+uuid;
		int width = 300;
		int height = 300;
		String logoPath = "E:/ray.jpg";
		String resultImage = "";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 2);  //设置白边
		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		resultImage = writeToFile(bitMatrix,logoPath,os);
		return resultImage;

	}

	/**
	 *
	 * @param matrix 二维码矩阵相关
	 * @param logoPath logo路径
	 * @throws IOException
	 */

	public static String  writeToFile(BitMatrix matrix,String logoPath,ByteArrayOutputStream os) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		Graphics2D gs = image.createGraphics();

		int ratioWidth = image.getWidth()*2/10;
		int ratioHeight = image.getHeight()*2/10;
		//载入logo
		Image img = ImageIO.read(new File(logoPath));
		int logoWidth = img.getWidth(null)>ratioWidth?ratioWidth:img.getWidth(null);
		int logoHeight = img.getHeight(null)>ratioHeight?ratioHeight:img.getHeight(null);

		int x = (image.getWidth() - logoWidth) / 2;
		int y = (image.getHeight() - logoHeight) / 2;

		gs.drawImage(img, x, y, logoWidth, logoHeight, null);
		gs.setColor(Color.black);
		gs.setBackground(Color.WHITE);
		gs.dispose();
		ImageIO.write(image, "png", os);
		String resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));
		return  resultImage;
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
	
}