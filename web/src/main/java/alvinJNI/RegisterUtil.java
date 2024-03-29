package alvinJNI;

import alvinJNI.util.IniWriteEx;
import alvinJNI.util.LocalCommandExecutor;
import alvinJNI.util.RegWriteEx;
import com.jeesite.common.lang.StringUtils;
import com.sun.xml.internal.ws.util.UtilException;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUtil {
	
	public static int sum = 0;


	public static String outsideProductSerialNumber = "TJSHA9AD422V1.0";// 外部产品序列号(明文)-程序打包之前公司服务器端提供
	public static String insideProductSerialNumber = "D3F80AEDF21848DF0373A538A4DA7F3A54804731BDA233BC21540286BE865E3F";// 内部产品序列号(密文)-程序打包之前公司服务器端提供
	public static String insideKey = "RLM4XS1X";// 内部序列号解密密钥-程序打包之前公司服务器端提供
	public static Integer DevID = 1497912136;// 加密锁开发商ID
	public static Integer ProID = 0xFFFFFFFF;// 加密锁产品ID
	public static String userpwd = "b8v6n7x9";// 加密锁登录密码
	public static String filename = "RLM4XS1X.evx";// 加密锁内部文件名称
	public static Integer devindex = 0;// 加密锁顺序号

	/**
	 * 判断是否允许通过
	 */
	@SuppressWarnings("unused")
	public static String isAllowLogin(HttpServletRequest request) {
		if(UrlDecrypt.map.size()>0){
			return null;
		}
		String flag = null;
		boolean isZs = false;
		String urlInfo = "";
		//ServletContext application = ServletActionContext.getServletContext();
		ServletContext application = null;
		try {
			application = request.getSession().getServletContext();
		} catch (Exception e1) {
			e1.printStackTrace();
			return "asda";
		}
		
		
		//测试使用代码
		//20150611 fyj
		String aa="";
		if (RegisterUtil.registerTableExist("uniquezs", request)
				&& RegisterUtil.registerTableExist("RegInfozs", request)) {// 验证正式注册(判断正式注册是否存在)
			try {
				String hwKeyStr = (String) application.getAttribute("hwKeyStr");// 获取application中加密狗密钥值
				String hwIdStr = (String) application.getAttribute("hwIdStr");// 获取application中加密狗硬件ID值
				if (hwIdStr == null || "".equals(hwIdStr)) {// 如果硬件ID为空,检索EncryJavaDll信息
					HardwareTime.runFun(application);
					hwKeyStr = (String) application.getAttribute("hwKeyStr");
					hwIdStr = (String) application.getAttribute("hwIdStr");
				}
				//System.out.println("zs");
				if (hwIdStr != null && !"".equals(hwIdStr) && hwKeyStr != null && !"".equals(hwKeyStr)) {// 判断是否插入加密狗    是不是有锁号，并且密钥值不能null，不能为空字符串
					if (!RegisterUtil.tableAndFileIsEqual("zs", application)) {// 判断注册表信息与注册文件信息是否相符
						// 注册表与注册文件不相符
						flag = "对不起，软件注册信息被破坏，软件无法正常登录！提示代码：012";
					}
					String regInfo_str = application.getAttribute("zs_regInfo_str").toString();
					String[] regInfo = regInfo_str.split("_");
					if (Integer.parseInt(regInfo[1]) <= Integer 
							.parseInt(application.getAttribute("userNum")
									.toString())) {
						flag = "对不起，站点数已超过限制，软件无法正常登录！提示代码：007";
					}
				} else {
					// 未插入加密狗
					flag = "对不起，系统未检测到产品硬件信息，软件无法正常登录！提示代码：011";
				}
			} catch (Exception e) {
				// 解密失败
				flag = "对不起，站点信息异常，软件无法正常登录！提示代码：010";
				e.printStackTrace();
				//System.out.println(flag);
			}
			isZs = true;
		} else {// 验证试用注册			
			if (RegisterUtil.registerTableExist("unique", request)
					&& RegisterUtil.registerTableExist("RegInfo", request)
					&& RegisterUtil.registerFileExist()) {// 判断注册表与注册文件是否存在
				if (RegisterUtil.tableAndFileIsEqual("sy", application)) {// 判断注册表信息与注册文件信息是否相符
					try {
						String regInfo_str = application.getAttribute("sy_regInfo_str").toString();
						String[] regInfo = regInfo_str.split("_");
						Date regDate = new SimpleDateFormat("yyyy/MM/dd")
								.parse(regInfo[0]);// 注册日期
						String regSite = regInfo[1];// 站点数
						Date useDate = new SimpleDateFormat("yyyy/MM/dd")
								.parse(regInfo[2]);// 最近使用时间
						Date overDate = new SimpleDateFormat("yyyy/MM/dd")
								.parse(regInfo[3]);// 试用截止时间
						String useNum = regInfo[4];// 已使用次数
						String maxNum = regInfo[5];// 最多使用次数
						String regCode = regInfo[6];// 注册码
						Date sdate = new SimpleDateFormat("yyyy/MM/dd")
								.parse((new SimpleDateFormat("yyyy/MM/dd"))
										.format(new Date()));// 获取当前系统日期
						if (regDate.getTime() <= sdate.getTime()
								&& sdate.getTime() < overDate.getTime()) {// 判断系统时间是否在注册期内
							if (sdate.getTime() >= useDate.getTime()) {// 判断系统时间是否在最近试用时间前
								if (!useNum.equals(maxNum)) {// 判断已使用次数与最多使用次数是否相等
//									HttpServletRequest request = ServletActionContext
//											.getRequest();
									ServletContext sc = request.getSession()
											.getServletContext();
									if (sc.getAttribute("userNum") == null
											|| Integer.parseInt(sc
													.getAttribute("userNum")
													.toString()) < Integer
													.parseInt(regSite)) {// 判断站点数
										// 重新拼接注册密文
										sum++;
//										Integer useNumInt = Integer
//												.parseInt(useNum) + 1;
//										String useNumStr = useNumInt.toString();
//										String regPlaintext = new SimpleDateFormat(
//												"yyyy/MM/dd").format(regDate)
//												+ "_"
//												+ regSite
//												+ "_"
//												+ new SimpleDateFormat(
//														"yyyy/MM/dd")
//														.format(sdate)
//												+ "_"
//												+ new SimpleDateFormat(
//														"yyyy/MM/dd")
//														.format(overDate)
//												+ "_"
//												+ useNumStr
//												+ "_"
//												+ maxNum
//												+ "_" + regCode;
//										// String regCiphertext = RegisterUtil
//										// .encryptStr(regPlaintext,
//										// uniquePlaintext.substring(
//										// 6, 14));
//										String regCiphertext = RegisterUtil
//												.regInfoEncryptStr(regPlaintext, application);// 加密注册信息
//										RegisterUtil
//												.updateRegTable(regCiphertext, application);// 更新注册表
//										RegisterUtil
//												.updateRegFile(regCiphertext, false);// 更新注册文件
									} else {
										// 当前登录用户已超过站点数
										flag = "对不起，站点数已超过限制，软件无法正常登录！提示代码：007";
									}
								} else {
									// 已使用次数与最多使用次数相等
									flag = "对不起，软件试用注册次数已用完！提示代码：006";
								}
							} else {
								// 系统时间在上次使用时间之前
								flag = "对不起，系统时间被人为修改，软件无法正常登录！提示代码：005";
							}
						} else {
							// 系统时间不在试用期内
							flag = "对不起，软件试用注册已过期！提示代码：004";
						}
					} catch (Exception e) {
						// 解密失败
						e.printStackTrace();
						flag = "对不起，软件注册信息被破坏，软件无法正常登录！提示代码：003";
					}
				} else {
					// 注册表与注册文件不相符
					flag = "对不起，软件注册信息被破坏，软件无法正常登录！提示代码：002";
				}
			} else {
				// 注册表或注册文件不存在
				flag = "对不起，软件未注册或软件注册信息被破坏，软件无法正常登录！提示代码：001";
			}
		}
//		boolean bb = RegisterUtil.registerTableExist("UrlInfo", request);
//		if(!bb){
//			if(isZs){
//				urlInfo = RegisterUtil.registerFileValue(hwIdStr, "UrlInfo");
//			}else{
//				urlInfo = RegisterUtil.registerFileValue("SOHERO", "UrlInfo");
//			}
//			updateRegTableValue("UrlInfo", urlInfo, application);
//		}
		
		return flag;
	}

	/**
	 * 根据urlKey值获取urlVal值
	 */
	public static String urlValue(String urlKey, String type,ServletContext application) {

		String resultStr = "";

		try {
//			ServletContext application = ServletActionContext
//					.getServletContext();

			String decStr = "";
			if ("zs".equals(type)) {// 正式注册
				String hwKeyStr = (String) application.getAttribute("hwKeyStr");// 获取application中加密狗密钥值
				String hwIdStr = (String) application.getAttribute("hwIdStr");// 获取application中加密狗硬件ID值
//				//System.out.println("方法：urlValue---加密密钥："+hwKeyStr);
//				//System.out.println("方法：urlValue---硬件ID："+hwIdStr);
				if (hwIdStr == null || "".equals(hwIdStr)) {// 如果硬件ID为空,检索EncryJavaDll信息
					HardwareTime.runFun(application);
					hwKeyStr = (String) application.getAttribute("hwKeyStr");
					hwIdStr = (String) application.getAttribute("hwIdStr");
//					
//					//System.out.println("方法1：urlValue---加密密钥："+hwKeyStr);
//					//System.out.println("方法1：urlValue---硬件ID："+hwIdStr);
				}
				if((hwKeyStr==null||"".equals(hwKeyStr))&&(hwIdStr!=null&&!"".equals(hwIdStr))){ // 如果拿不到密钥值,但是硬件id却可以取到
					return "nokeystr";
				}
//				String urlInfo = RegisterUtil.registerFileValue(hwIdStr,
//						"UrlInfo");
				String urlInfo = application.getAttribute("zs_urlInfo").toString();
				decStr = RegisterUtil.decryptStr(urlInfo, hwKeyStr);

			} else {
				String sy_key = application.getAttribute("sy_key").toString();
//				String urlInfo = RegisterUtil.registerFileValue("SOHERO",
//						"UrlInfo");
				System.out.println("sy_key="+sy_key);
				String urlInfo = application.getAttribute("sy_urlInfo").toString();
//				decStr = RegisterUtil.decryptStr(urlInfo,
//						("sy",application));
				System.out.println("sy_urlInfo="+urlInfo);
				decStr = RegisterUtil.decryptStr(urlInfo, sy_key);
			}
			String[] resultList = decStr.split(";");
			for (int i = 0; i < resultList.length; i++) {
				String[] keys = resultList[i].split("---");
				if (keys[0].equals(urlKey)) {
					resultStr = keys[1];
					break;
				}
			}
//			//System.out.println("resultStr="+resultStr);
		} catch (Exception e) {
			e.printStackTrace();
			return resultStr;
		}
		
		return resultStr;
	}

	/**
	 * 加密注册信息
	 */
	public static String regInfoEncryptStr(String regPlaintext,ServletContext application) {
		String resultStr = "";
		try {
			String regTableUnique = RegisterUtil.registerTableValue("unique",application);// 获取注册表中unique值
			String uniquePlaintext = RegisterUtil.decryptStr(regTableUnique,
					RegisterUtil.ipsnDecryptStr().substring(0, 8));// 将注册表中unique值进行解密
			resultStr = RegisterUtil.encryptStr(regPlaintext,
					uniquePlaintext.substring(uniquePlaintext.length() - 8));
		} catch (Exception e) {
			return resultStr;
		}
		return resultStr;
	}

	/**
	 * 获取注册信息解密密钥值
	 */
	public static String regInfoKeyStr(String type,ServletContext application) {

		String resultStr = "";
		try {
			String regTableUnique = "";// 获取注册表中unique值
			if ("sy".equals(type)) {
				regTableUnique = RegisterUtil.registerTableValue("unique",application);
			} else {
				regTableUnique = RegisterUtil.registerTableValue("uniquezs",application);
			}
			String uniquePlaintext = RegisterUtil.decryptStr(regTableUnique,
					RegisterUtil.ipsnDecryptStr().substring(0, 8));// 将注册表中unique值进行解密
			resultStr = uniquePlaintext.substring(uniquePlaintext.length() - 8);

		} catch (Exception e) {
		}

		return resultStr;
	}

	/**
	 * 针对注册表中注册信息进行解密
	 */
	public static String regInfoDecryptStr(String type,ServletContext application) {
		String regInfoPlaintext = "";
		try {
			String regTableUnique = "";// 获取注册表中unique值
			String regTableInfo = "";// 获取注册表中RegInfo值
			if ("sy".equals(type)) {
				regTableUnique = RegisterUtil.registerTableValue("unique",application);
				regTableInfo = RegisterUtil.registerTableValue("RegInfo",application);
			} else {
				regTableUnique = RegisterUtil.registerTableValue("uniquezs",application);
				regTableInfo = RegisterUtil.registerTableValue("RegInfozs",application);
			}
			String uniquePlaintext = RegisterUtil.decryptStr(regTableUnique,
					RegisterUtil.ipsnDecryptStr().substring(0, 8));// 将注册表中unique值进行解密
			// //System.out.println(RegisterUtil.myReplace(uniquePlaintext.substring(uniquePlaintext.length()-8)));
			regInfoPlaintext = RegisterUtil.decryptStr(regTableInfo,
					RegisterUtil.myReplace(uniquePlaintext
							.substring(uniquePlaintext.length() - 8)));// 将注册表中RegInfo值进行解密
		} catch (Exception e) {
			return regInfoPlaintext;
		}

		return regInfoPlaintext;
	}

	/**
	 * 针对内部序列号进行解密
	 */
	public static String ipsnDecryptStr() {
		String result = null;
		try {
			result = RegisterUtil.decryptStr(insideProductSerialNumber,
					insideKey).substring(3, 27);
		} catch (Exception e) {
		}
		return result;
	}

	public static void main(String[] args) {
		// //System.out.println(RegisterUtil.EncoderByMd5(
		// RegisterUtil.ipsnDecryptStr())
		// .toUpperCase());
		//System.out.println(RegisterUtil.ipsnDecryptStr());
	}

	/**
	 * 更新注册表
	 */
	public static void updateRegTable(String regCiphertext, ServletContext application) {

		// ServletContext application = request.getSession().getServletContext();
		String pathStr = (String) application.getAttribute("pathStr");// 获取application中注册表路径值

//		try {
//			// for (Integer i = 0; i < 13; i++) {
//			Process ps = Runtime.getRuntime().exec(
//					"REG QUERY "
//							// + RegisterUtil.registerTablePathList().get(i)
//							+ pathStr
//							+ "\\"
//							+ RegisterUtil.EncoderByMd5(
//									RegisterUtil.ipsnDecryptStr())
//									.toUpperCase());
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					ps.getInputStream()));
//			if (br.readLine() != null) {
//				String execStr = "REG ADD "
//						// + RegisterUtil.registerTablePathList().get(i)
//						+ pathStr
//						+ "\\"
//						+ RegisterUtil.EncoderByMd5(
//								RegisterUtil.ipsnDecryptStr()).toUpperCase()
//						+ " /v RegInfo /d " + regCiphertext + " /f";
//				Runtime.getRuntime().exec(execStr);
//				
//				// break;
//			}
//			br.close();
//			// }
//		} catch (Exception e) {
//		}
		LocalCommandExecutor lce=new LocalCommandExecutor();
		String er;
		String execStr;
		String tempPath= RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr()).toUpperCase();
		try {
			execStr="REG QUERY "+ pathStr+ "\\"+ tempPath;
			er=lce.executeCommand(execStr);
			if (StringUtils.isNotBlank(er)){
//				 execStr = "REG ADD "+ pathStr+ "\\"+ tempPath+ " /v RegInfo /d " + regCiphertext + " /f";
//				 lce.executeCommand(execStr);
				//使用dll向注册表内写入信息
				RegWriteEx re=new RegWriteEx();
				re.writeRegeditDll(pathStr+ "\\"+ tempPath,"RegInfo",regCiphertext);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新注册文件
	 */
	public static void updateRegFile(String regCiphertext, boolean isUrl) {
		String path = RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr())
				.toUpperCase()
				+ "\\"
				+ RegisterUtil.EncoderByMd5("SOHERO").toUpperCase();
		//这个判断表示是32位
		if(System.getProperty("os.arch").equals("x86")||!System.getProperty("sun.arch.data.model").equals("64")){
//			System.out.println("xxxxx");
			IniWriteEx re = new IniWriteEx();
			if(isUrl){
				re.writeIniDll("C:\\Windows\\" + path + ".ini","UrlInfo" ,regCiphertext);
			}else{
				re.writeIniDll("C:\\Windows\\" + path + ".ini",outsideProductSerialNumber,regCiphertext);
			}	
		}else{ // 其他情况就是64位的了
			if(isUrl){
				try {
					RegisterUtil.setProfileString("C:\\Windows\\" + path + ".ini",
							outsideProductSerialNumber, "UrlInfo",
							regCiphertext);
				} catch (IOException e) {
					
				}
			}else{
				try {
//					System.out.println(regCiphertext+"------------");
					RegisterUtil.setProfileString("C:\\Windows\\" + path + ".ini",
							outsideProductSerialNumber, outsideProductSerialNumber,
							regCiphertext);
				} catch (IOException e) {
					
				}
			}
		}
	}
	
	
	public static void updateRegFileZs(String regCiphertext, String hwIdStr, boolean isUrl) {
		String path = RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr())
				.toUpperCase()
				+ "\\"
				+ RegisterUtil.EncoderByMd5(hwIdStr).toUpperCase();
		//这个判断表示是32位
		if(System.getProperty("os.arch").equals("x86")||!System.getProperty("sun.arch.data.model").equals("64")){
//			System.out.println("xxxxx");
			IniWriteEx re = new IniWriteEx();
			if(isUrl){
				re.writeIniDll("C:\\Windows\\" + path + ".ini","UrlInfo" ,regCiphertext);
			}else{
				re.writeIniDll("C:\\Windows\\" + path + ".ini",outsideProductSerialNumber,regCiphertext);
			}
		}else{ // 其他情况就是64位的了
			if(isUrl){
				try {
					RegisterUtil.setProfileString("C:\\Windows\\" + path + ".ini",
							outsideProductSerialNumber, "UrlInfo",
							regCiphertext);
				} catch (IOException e) {
					
				}
			}else{
				try {
//					System.out.println(regCiphertext+"------------");
					RegisterUtil.setProfileString("C:\\Windows\\" + path + ".ini",
							outsideProductSerialNumber, outsideProductSerialNumber,
							regCiphertext);
				} catch (IOException e) {
					
				}
			}
		}
	}

	/**
	 * 判断注册表信息与注册文件信息是否相符
	 */
	@SuppressWarnings("unused")
	public static boolean tableAndFileIsEqual(String type,ServletContext application) {
		boolean flag = false;
		
		String regTableStr = "";// 获取注册表中值
		String regFileStr = "";// 获取注册文件中值
		if ("sy".equals(type)) {
//			regTableStr = RegisterUtil.registerTableValue("RegInfo", application);
//			regFileStr = RegisterUtil.registerFileValue("SOHERO",
//					outsideProductSerialNumber);
			regTableStr = application.getAttribute("sy_regTableStr").toString();
			regFileStr = application.getAttribute("sy_regFileStr").toString();
		} else {
//			regTableStr = RegisterUtil.registerTableValue("RegInfozs", application);
//			regFileStr = RegisterUtil.registerFileValue(hwIdStr,
//					outsideProductSerialNumber);
			regTableStr = application.getAttribute("zs_regTableStr").toString();
			regFileStr = application.getAttribute("zs_regFileStr").toString();
		}
		if(regFileStr==null){
			regFileStr = "";
		}
		/***************** 以下代码适用于x64、x86位系统 -mayuhu - start ****************/
		//if(regTableStr.trim().equals(regFileStr.trim())){
		if(StringUtils.isNotBlank(regTableStr.trim())&& StringUtils.isNotBlank(regFileStr.trim())){
			flag=true;
			updateRegTableValue("RegCheck","0",application);//一次比较成功，则将RegCheck重置为空
		}else{
			String regCheck= RegisterUtil.registerTableValue("RegCheck", application);
			regCheck=(regCheck==null||"".equals(regCheck.trim()))?"0":regCheck.trim();
			int regCheckNum=Integer.parseInt(regCheck);
			if(regCheckNum<101){//为64位系统增加三次容错验证
				flag=true;
				regCheckNum++;
				updateRegTableValue("RegCheck",regCheckNum+"",application);
				String urlInfo = RegisterUtil.registerTableValue("UrlInfo", application);
				updateRegTableValue("UrlInfo", urlInfo, application);
				if("sy".equals(type)){
					updateRegFile(urlInfo, true);
				}else{
					String hwKeyStr = (String) application.getAttribute("hwKeyStr");// 获取application中加密狗密钥值
					String hwIdStr = (String) application.getAttribute("hwIdStr");// 获取application中加密狗硬件ID值
					if (hwIdStr == null || "".equals(hwIdStr)) {// 如果硬件ID为空,检索EncryJavaDll信息
						HardwareTime.runFun(application);
						hwKeyStr = (String) application.getAttribute("hwKeyStr");
						hwIdStr = (String) application.getAttribute("hwIdStr");
					}
					updateRegFileZs(urlInfo, hwIdStr, true);
				}
				
			}else{//连续三次比对不成功，默认为注册文件信息被破坏
				flag=false;
			}
		}
		/***************** 以上代码适用于x64、x86位系统 -mayuhu - end ******************/
		return flag;
	}
	
	/**
	 * 若某路径下的注册表存在则按Key值更新Value值 
	 *
	 * @author zsl
	 * @date 2018-12-4 下午03:27:36
	 */
	public static void updateRegTableValue(String regKey,String regValue,ServletContext sc){
		String pathStr = (String) sc.getAttribute("pathStr");// 获取application中注册表路径值
		LocalCommandExecutor lce=new LocalCommandExecutor();
		String er;
		String execStr;
		String tempPath= RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr()).toUpperCase();
		try {
			execStr="REG QUERY "+ pathStr+ "\\"+ tempPath;
			er=lce.executeCommand(execStr);
			if (StringUtils.isNotBlank(er)){
				//Java原生实现
				//execStr = "REG ADD "+ pathStr+ "\\"+ tempPath+ " /v RegInfo /d " + regCiphertext + " /f";
				//lce.executeCommand(execStr);
				
				//使用dll向注册表内写入信息，适用于32、64位系统
				RegWriteEx re=new RegWriteEx();
				re.writeRegeditDll(pathStr+ "\\"+ tempPath,regKey,regValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取注册表中value值
	 */
	public static String registerTableValue(String name, ServletContext application) {

//		String resultStr = "";
//
//		//ServletContext application = ServletActionContext.getServletContext();
//		String pathStr = (String) application.getAttribute("pathStr");// 获取application中注册表路径值
//
//		try {
//			// for (Integer i = 0; i < 13; i++) {
//			String execStr = "REG QUERY "
//					// + RegisterUtil.registerTablePathList().get(i)
//					+ pathStr
//					+ "\\"
//					+ RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr())
//							.toUpperCase() + " /v " + name;
//
//			Process ps = Runtime.getRuntime().exec(execStr);
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					ps.getInputStream()));
//			if (br.readLine() != null) {// 判断是否存在
//				String line;
//				StringBuffer sb = new StringBuffer();
//				while ((line = br.readLine()) != null) {
//					sb.append(line);
//				}
//				if (sb.indexOf(name) != -1) {
//					resultStr = sb.substring(sb.lastIndexOf("	") + 1);
//					resultStr = resultStr
//							.substring(resultStr.lastIndexOf(" ") + 1);
//				}
//			}
//			// }
//		} catch (IOException e) {
//			return resultStr;
//		}
//
//		return resultStr;
		String resultStr = "";
		//ServletContext application = ServletActionContext.getServletContext();
		if(application.getAttribute("pathStr")!=null&& StringUtils.isNotBlank(application.getAttribute("pathStr").toString())){
			String pathStr = (String) application.getAttribute("pathStr");// 获取application中注册表路径值
			LocalCommandExecutor lce=new LocalCommandExecutor();
			String er;
			String execStr;
			String tempPath= RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr()).toUpperCase();
			execStr = "REG QUERY "+ pathStr+ "\\"+ tempPath + " /v " + name;
			er=lce.executeCommand(execStr);
			if (StringUtils.isNotBlank(er)&& er.indexOf(name) != -1) {
				resultStr = er.substring(er.lastIndexOf("	") + 1);
				resultStr = resultStr.substring(resultStr.lastIndexOf(" ") + 1);
			}
		}
		return resultStr;
	}

	/**
	 * 获取注册文件中信息
	 */
	public static String registerFileValue(String fileName, String key) {

		String value = "";

		try {
			Properties prop = new Properties();
			String path = RegisterUtil.EncoderByMd5(
					RegisterUtil.ipsnDecryptStr()).toUpperCase()
					+ "\\" + RegisterUtil.EncoderByMd5(fileName).toUpperCase();
			prop.load(new FileInputStream("C:\\Windows\\" + path + ".ini"));
			value = prop.getProperty(key);
		} catch (FileNotFoundException fnfe) {
			return value;
		} catch (IOException ioe) {
			return value;
		}

		return value;
	}
	
	public static String registerFileValueByPath(String fileName, String key) {

		String value = "";

		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(fileName));
			value = prop.getProperty(key);
		} catch (FileNotFoundException fnfe) {
			return value;
		} catch (IOException ioe) {
			return value;
		}
		if(value==null){
			value = "";
		}
		return value;
	}
	

	/**
	 * 判断注册表是否存在
	 */
	public static boolean registerTableExist(String key, HttpServletRequest request) {

		boolean flag = false;

		ServletContext application = request.getSession().getServletContext();
		String pathStr = (String) application.getAttribute("pathStr");// 获取application中注册表路径值

//		try {
//			// for (Integer i = 0; i < 13; i++) {
//			String execStr = "REG QUERY "
//					// + RegisterUtil.registerTablePathList().get(i)
//					+ pathStr
//					+ "\\"
//					+ RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr())
//							.toUpperCase() + " /v " + key;
//			Process ps = Runtime.getRuntime().exec(execStr);
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					ps.getInputStream()));
//			if (br.readLine() != null) {// 判断是否存在
//				String line;
//				StringBuffer sb = new StringBuffer();
//				while ((line = br.readLine()) != null) {
//					sb.append(line);
//				}
//				if (sb.indexOf(key) != -1) {
//					flag = true;
//					// break;// 跳出循环
//				}
//			}
//			// }
//		} catch (IOException e) {
//			return flag;
//		}
//
//		return flag;
		LocalCommandExecutor lce=new LocalCommandExecutor();
		String tempPath= RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr()).toUpperCase();
		String execStr = "REG QUERY "+ pathStr+ "\\"+ tempPath + " /v " + key;
		String er=lce.executeCommand(execStr);
		if (StringUtils.isNotBlank(er)&& er.indexOf(key) != -1) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断注册文件是否存在
	 */
	public static boolean registerFileExist() {

		boolean flag = false;

		String path = RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr())
				.toUpperCase()
				+ "\\"
				+ RegisterUtil.EncoderByMd5("SOHERO").toUpperCase();
		File file = new File("C:\\Windows\\" + path + ".ini");
		if (file.exists()) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 获取注册表存放路径集合 HKCR： HKEY_CLASSES_ROOT HKCU： HKEY_CURRENT_USER HKLM：
	 * HKEY_LOCAL_MACHINE HKU： HKEY_USERS HKCC： HKEY_CURRENT_CONFIG
	 */
	public static List<String> registerTablePathList() {

		List<String> resultList = new ArrayList<String>();

		// resultList.add("HKLM\\HARDWARE");
		// resultList.add("HKLM\\HARDWARE\\ACPI");
		// resultList.add("HKLM\\HARDWARE\\DESCRIPTION");
		// resultList.add("HKLM\\HARDWARE\\DEVICEMAP");
		// resultList.add("HKLM\\HARDWARE\\RESOURCEMAP");
		resultList.add("HKLM\\SAM");
		// resultList.add("HKLM\\SAM\\SAM");
		resultList.add("HKLM\\SOFTWARE");
		resultList.add("HKLM\\SOFTWARE\\Wow6432Node");
		resultList.add("HKLM\\SYSTEM");
		resultList.add("HKLM\\SYSTEM\\ControlSet001");
		resultList.add("HKLM\\SYSTEM\\ControlSet002");
		resultList.add("HKLM\\SYSTEM\\CurrentControlSet");
		resultList.add("HKLM\\SYSTEM\\Setup");
		// resultList.add("HKLM\\SYSTEM\\WPA");

		return resultList;
	}

	/**
	 * 利用MD5进行加密
	 */
	public static String EncoderByMd5(String str) throws UtilException {
		MessageDigest md5;
		String newstr = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			md5 = MessageDigest.getInstance("MD5");
			newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
			newstr = newstr.replaceAll("/", "").replaceAll("=", "");// 过滤掉"/"、"="符号
		} catch (NoSuchAlgorithmException e) {
			return newstr;
		} catch (UnsupportedEncodingException e) {
			return newstr;
		}
		return newstr;
	}

	/** DES加密解密代码-start ************************************************************************/
	public static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	public static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	// 加密
	public static String encryptStr(String plaintext, String key)
			throws Exception {
		return toHexString(
				encrypt(java.net.URLEncoder.encode(plaintext, "utf-8")
						.toLowerCase(), key)).toUpperCase();
	}

	// 解密
	public static String decryptStr(String ciphertext, String key)
			throws Exception {
		return java.net.URLDecoder.decode(decrypt(ciphertext, key), "utf-8");
	}

	/** DES加密解密代码-end ************************************************************************/

	/** 更新ini文件代码-end ************************************************************************/
	public static boolean setProfileString(String file, String section,
			String variable, String value) throws IOException {

		String fileContent, allLine, strLine, newLine, getValue;
		FileReader fr = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fr);
		boolean isInSection = false;
		fileContent = "";
		int sum = 0;

		try {
			while ((allLine = bufferedReader.readLine()) != null) {
				sum++;	
				allLine = allLine.trim();
				if("".equals(allLine)){
					continue;
				}
				strLine = allLine;
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[" + section + "\\]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[" + section + "\\]");
					m = p.matcher(strLine);
					if (m.matches()) {
						isInSection = true;
					} else {
						isInSection = false;
					}
				}
				if (isInSection == true) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					if (getValue.equalsIgnoreCase(variable)) {
						newLine = getValue + "=" + value;
						fileContent += newLine + "\r\n";
						while ((allLine = bufferedReader.readLine()) != null) {
							fileContent += allLine + "\r\n";
						}
						bufferedReader.close();
						FileWriter fw = new FileWriter(file, false);
						BufferedWriter bufferedWriter = new BufferedWriter(
								fw);
						bufferedWriter.write(fileContent);
						bufferedWriter.flush();
						bufferedWriter.close();
						fw.close();
						return true;
					}else{
//						System.out.println("???????");
//						System.out.println("".equals(registerFileValueByPath(file, "UrlInfo")));
//						System.out.println("".equals(registerFileValueByPath(file, outsideProductSerialNumber)));
//						System.out.println(!(!"".equals(registerFileValueByPath(file, "UrlInfo"))&&!"".equals(registerFileValueByPath(file, outsideProductSerialNumber))));
//						System.out.println("???????");
						if(!(!"".equals(registerFileValueByPath(file, "UrlInfo"))&&!"".equals(registerFileValueByPath(file, outsideProductSerialNumber)))){
							fileContent = "[" + section + "]" + "\r\n";
							fileContent += variable + "=" + value + "\r\n";
							while ((allLine = bufferedReader.readLine()) != null) {
								fileContent += allLine + "\r\n";
							}
							bufferedReader.close();
							FileWriter fw = new FileWriter(file, false);
							BufferedWriter bufferedWriter = new BufferedWriter(
									fw);
							bufferedWriter.write(fileContent);
							bufferedWriter.flush();
							bufferedWriter.close();
							fw.close();
							return true;
						}
					}
				}
				
				fileContent += allLine + "\r\n";
			}
			if(sum==0){
				fileContent = "[" + section + "]" + "\r\n";
				fileContent += variable + "=" + value + "\r\n";
				FileWriter fw = new FileWriter(file, false);
				BufferedWriter bufferedWriter = new BufferedWriter(
						fw);
				bufferedWriter.write(fileContent);
				bufferedWriter.flush();
				bufferedWriter.close();
				fw.close();
				return true;
			}
			if(sum == 1){
				fileContent = "[" + section + "]" + "\r\n";
				fileContent += variable + "=" + value + "\r\n";
				FileWriter fw = new FileWriter(file, false);
				BufferedWriter bufferedWriter = new BufferedWriter(
						fw);
				bufferedWriter.write(fileContent);
				bufferedWriter.flush();
				bufferedWriter.close();
				fw.close();
				return true;
			}
			
			
		} catch (IOException ex) {
			throw ex;
		} finally {
			bufferedReader.close();
			fr.close();
		}
		return false;
	}

	/** 更新ini文件代码-end ************************************************************************/

	public static String myReplace(String str) {

		return str.replace("A", "1").replace("B", "2").replace("C", "3")
				.replace("D", "4").replace("E", "5").replace("F", "6");

	}

	/**
	 * 判断注册表是否存在
	 */
	public static boolean registerTableExist(String key, ServletContext sc) {
		boolean flag = false;
		String pathStr = (String) sc.getAttribute("pathStr");// 获取application中注册表路径值

//		try {
//			if (sc.getAttribute("regPath") != null
//					&& !"".equals(sc.getAttribute("regPath").toString())) {
//				String execStr = "REG QUERY "
//						+ sc.getAttribute("regPath").toString() + " /v " + key;
//
//				Process ps = Runtime.getRuntime().exec(execStr);
//				BufferedReader br = new BufferedReader(new InputStreamReader(
//						ps.getInputStream()));
//				if (br.readLine() != null) {// 判断是否存在
//					String line;
//					StringBuffer sb = new StringBuffer();
//					while ((line = br.readLine()) != null) {
//						sb.append(line);
//					}
//					if (sb.indexOf(key) != -1) {
//						flag = true;
//						// break;// 跳出循环
//					}
//				}
//			} else {
//				// for (Integer i = 0; i < 13; i++) {
//				// String regPath = RegisterUtil.registerTablePathList().get(i)
//				String regPath = pathStr
//						+ "\\"
//						+ RegisterUtil.EncoderByMd5(
//								RegisterUtil.ipsnDecryptStr()).toUpperCase();
//				String execStr = "REG QUERY " + regPath + " /v " + key;
//
//				Process ps = Runtime.getRuntime().exec(execStr);
//				BufferedReader br = new BufferedReader(new InputStreamReader(
//						ps.getInputStream()));
//				if (br.readLine() != null) {// 判断是否存在
//					sc.setAttribute("regPath", regPath);
//					String line;
//					StringBuffer sb = new StringBuffer();
//					while ((line = br.readLine()) != null) {
//						sb.append(line);
//					}
//					if (sb.indexOf(key) != -1) {
//						flag = true;
//						// break;// 跳出循环
//					}
//				}
//				// }
//			}
//		} catch (IOException e) {
//			return flag;
//		}
//
//		return flag;
		LocalCommandExecutor lce=new LocalCommandExecutor();
		String execStr;
		String er;
		if (sc.getAttribute("regPath") != null
				&& !"".equals(sc.getAttribute("regPath").toString())) {
			 execStr = "REG QUERY " + sc.getAttribute("regPath").toString() + " /v " + key;
			 
//			 System.out.println(execStr+"----------------------------------------------if-------");
			 
			 er=lce.executeCommand(execStr);
			if (StringUtils.isNotBlank(er)&&er.indexOf(key) != -1) {
					flag = true;
			}
		} else {
			String regPath = pathStr
					+ "\\"
					+ RegisterUtil.EncoderByMd5(
							RegisterUtil.ipsnDecryptStr()).toUpperCase();
			execStr = "REG QUERY " + regPath + " /v " + key;
			
//			System.out.println(execStr+"----------------------------------else-------------------");
			 er=lce.executeCommand(execStr);
			if (StringUtils.isNotBlank(er)){
				sc.setAttribute("regPath", regPath);
				if(er.indexOf(key)!= -1){
					flag = true;
				}
			}
		}

		return flag;
	}

	/**
	 * 验证是否正式注册
	 */
	public static boolean isZs(ServletContext sc) {

		boolean flag = false;

		if (RegisterUtil.registerTableExist("uniquezs", sc)
				&& RegisterUtil.registerTableExist("RegInfozs", sc)) {// 判断正式注册是否存在
			flag = true;
		}

		return flag;
	}
}