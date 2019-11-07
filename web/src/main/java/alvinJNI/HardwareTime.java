package alvinJNI;

import alvinJNI.util.LocalCommandExecutor;
import com.jeesite.common.lang.StringUtils;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HardwareTime {
	
	private static int started = 0;

	public ServletContext sc;
	
	public ServletContext getSc() {
		return sc;
	}

	public void setSc(ServletContext sc) {
		this.sc = sc;
		
	}

	public HardwareTime(ServletContext sc) {
		this.sc = sc;
		Timer timer = new Timer();
		Timmer1 timmer = new Timmer1(timer,sc);
		timer.schedule(timmer, 1000, 30000);// ?\开始\间隔
	}
	
	/**
	 * 获取加密狗硬件ID值
	 */
	public static String hardwareId(ServletContext sc) {
		
		EncryJavaDll dll = new EncryJavaDll();
		int[] devserial = new int[2];
		devserial[0]=0;
		String[] Serial = new String[2];
		try {
			Serial = RegisterUtil.decryptStr(RegisterUtil.registerTableValue("Serial", sc), RegisterUtil.ipsnDecryptStr().substring(0, 8)).split(";");
		} catch (Exception e) {}
//		System.out.println("devserial="+devserial);
//		System.out.println("RegisterUtil.DevID="+RegisterUtil.DevID);
//		System.out.println("RegisterUtil.ProID="+RegisterUtil.ProID);
//		System.out.println("Serial[0]="+Serial[0]);
//		System.out.println("Serial[0]="+Serial[0]);
		int v3=dll.GetDeviceInfo(devserial, RegisterUtil.DevID, RegisterUtil.ProID, 0,Integer.parseInt(Serial[0]),Integer.parseInt(Serial[1]));
		String softdogSerial="";
		if(v3==1)
		{
			String beforeStr=Integer.toHexString(devserial[0]).toUpperCase();
			String lastStr=Integer.toHexString(devserial[1]).toUpperCase();
			int beforeStrLen=beforeStr.length();
			for (int i = 0; i < 8-beforeStrLen; i++) {
				beforeStr="0"+beforeStr;
			}
			int lastStrLen=lastStr.length();
			for (int i = 0; i < 8-lastStrLen; i++) {
				lastStr ="0" +lastStr;
			}
			softdogSerial=swapStr(beforeStr)+"-"+swapStr(lastStr);
		}
		return softdogSerial;
	}
	
	public static String swapStr(String str){
		String s1 = str.substring(0, 4);
		String s2 = str.substring(4, 8);
		return s2.substring(2, 4)+s2.substring(0, 2)+s1.substring(2, 4)+s1.substring(0, 2);
	}
	
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
//			String[] regInfo = RegisterUtil.regInfoDecryptStr("zs").split("_");
//			//System.out.println(regInfo);
//			//System.out.println("-----1:"+regInfo[6]);
//			String hwKeyStr = HardwareTime.hardwareKeyStr(regInfo[6]);
//			//System.out.println("-----2:"+hwKeyStr);
		}
	}
	
	/**
	 * 获取加密狗字符串(密钥)值
	 */
	public static String hardwareKeyStr(String regCode,ServletContext sc) {
		String resultStr = "";
		try {
			EncryJavaDll dll = new EncryJavaDll();
			regCode = regCode.substring(4, regCode.length() - 4);
			if (regCode.length() > 16) {
				regCode = regCode.substring(0, 16);
			}
			byte[] byte1 = regCode.getBytes();
			String[] Serial = RegisterUtil.decryptStr(RegisterUtil.registerTableValue("Serial", sc), RegisterUtil.ipsnDecryptStr().substring(0, 8)).split(";");
//			System.out.println("regCode="+regCode);
//			System.out.println("byte1="+byte1.toString());
//			System.out.println("RegisterUtil.DevID="+RegisterUtil.DevID);
//			System.out.println("RegisterUtil.ProID="+RegisterUtil.ProID);
//			System.out.println("RegisterUtil.userpwd="+RegisterUtil.userpwd);
//			System.out.println("RegisterUtil.filename="+RegisterUtil.filename);
//			System.out.println("RegisterUtil.devindex="+RegisterUtil.devindex);
//			System.out.println("Serial[0]="+Serial[0]);
//			System.out.println("Serial[1]="+Serial[1]);
			
			
			int v2 = dll.EncryFunc(byte1, RegisterUtil.DevID, RegisterUtil.ProID, RegisterUtil.userpwd, RegisterUtil.filename, RegisterUtil.devindex,Integer.parseInt(Serial[0]),Integer.parseInt(Serial[1]));
			//System.out.println("v2="+v2);
			if (v2 == 1) {
				for (int i = 0; i < byte1.length; i++) {
					resultStr += Integer.toHexString(byte1[i] & 0XFF)
							.toUpperCase();
				}
			}
			if (!"".equals(resultStr)) {
				resultStr = resultStr.substring(0, 8);
			}
		} catch (Exception e) {
		}
		return resultStr;
	}
	
	public static void runFun(ServletContext sc){
//		System.out.println("runFun");
		try {
			String pathStr = "";
			LocalCommandExecutor lce=new LocalCommandExecutor();
			String er;
			String execStr;
			if(sc.getAttribute("pathStr")==null|| StringUtils.isBlank(sc.getAttribute("pathStr").toString())){
				for (Integer i = 0; i < RegisterUtil.registerTablePathList().size(); i++) {
					pathStr = RegisterUtil.registerTablePathList().get(i);
					execStr = "REG QUERY "+pathStr+"\\"
					+ RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr()).toUpperCase();
					er=lce.executeCommand(execStr);
					if (StringUtils.isNotBlank(er)){
						break;
					}
				}
				sc.setAttribute("pathStr", pathStr);
			}
			boolean bb = RegisterUtil.registerTableExist("UrlInfo", sc);
			if(!RegisterUtil.isZs(sc)){
				String sy_key = RegisterUtil.regInfoKeyStr("sy", sc);
//				System.out.println(sy_key);
				sc.setAttribute("sy_key", sy_key);
				
				String sy_urlInfo = RegisterUtil.registerFileValue("SOHERO", "UrlInfo");
//				System.out.println("HardwareTime 里 runFun 里的 sy_urlInfo = "+sy_urlInfo);
//				System.out.println(sy_urlInfo);
				sc.setAttribute("sy_urlInfo", sy_urlInfo);
				if(!bb){
					RegisterUtil.updateRegTableValue("UrlInfo", sy_urlInfo, sc);
				}
				String regTableStr = RegisterUtil.registerTableValue("RegInfo", sc);
				String regFileStr = RegisterUtil.registerFileValue("SOHERO", RegisterUtil.outsideProductSerialNumber);
				
				sc.setAttribute("sy_regTableStr", regTableStr);
				
				sc.setAttribute("sy_regFileStr", regFileStr);
				
				String regInfo_str = RegisterUtil.regInfoDecryptStr("sy", sc);
				sc.setAttribute("sy_regInfo_str", regInfo_str);
				if( HardwareTime.started > 0){ // 这样才写注册表
					String[] regInfo = regInfo_str.split("_");
					Date regDate = new SimpleDateFormat("yyyy/MM/dd")
							.parse(regInfo[0]);// 注册日期
					String regSite = regInfo[1];// 站点数
//					Date useDate = new SimpleDateFormat("yyyy/MM/dd")
//							.parse(regInfo[2]);// 最近使用时间
					Date overDate = new SimpleDateFormat("yyyy/MM/dd")
							.parse(regInfo[3]);// 试用截止时间
					String useNum = regInfo[4];// 已使用次数
					String maxNum = regInfo[5];// 最多使用次数
					String regCode = regInfo[6];// 注册码
					Date sdate = new SimpleDateFormat("yyyy/MM/dd")
					.parse((new SimpleDateFormat("yyyy/MM/dd"))
							.format(new Date()));// 获取当前系统日期
					Integer sumNum = RegisterUtil.sum;
//					System.out.println("RegisterUtil.sum="+RegisterUtil.sum);
					Integer useNumInt = Integer.parseInt(useNum) + sumNum;
					String useNumStr = useNumInt.toString();
					String regPlaintext = new SimpleDateFormat(
							"yyyy/MM/dd").format(regDate)
							+ "_"
							+ regSite
							+ "_"
							+ new SimpleDateFormat(
									"yyyy/MM/dd")
									.format(sdate)
							+ "_"
							+ new SimpleDateFormat(
									"yyyy/MM/dd")
									.format(overDate)
							+ "_"
							+ useNumStr
							+ "_"
							+ maxNum
							+ "_" + regCode;
					
					String regCiphertext = RegisterUtil.regInfoEncryptStr(regPlaintext, sc);// 加密注册信息
					RegisterUtil.updateRegTable(regCiphertext, sc);// 更新注册表
					RegisterUtil.updateRegFile(regCiphertext, false);// 更新注册文件
					RegisterUtil.sum = 0;
				}
				HardwareTime.started = 1;
			}else{
				String zsReg = RegisterUtil.regInfoDecryptStr("zs", sc);
				if(StringUtils.isNotBlank(zsReg)){
					String regInfo_str = RegisterUtil.regInfoDecryptStr("zs", sc);
					sc.setAttribute("zs_regInfo_str", regInfo_str);
					
					String[] regInfo = regInfo_str.split("_");
					String hwKeyStr = HardwareTime.hardwareKeyStr(regInfo[6],sc);
					String hwIdStr = HardwareTime.hardwareId(sc);
					sc.setAttribute("hwKeyStr", hwKeyStr);
					sc.setAttribute("hwIdStr", hwIdStr);					
					
					String zs_urlInfo = RegisterUtil.registerFileValue(hwIdStr, "UrlInfo");
//					System.out.println(sy_urlInfo);
					sc.setAttribute("zs_urlInfo", zs_urlInfo);
					if(!bb){
						RegisterUtil.updateRegTableValue("UrlInfo", zs_urlInfo, sc);
					}
					String regTableStr = RegisterUtil.registerTableValue("RegInfozs", sc);
					String regFileStr = RegisterUtil.registerFileValue(hwIdStr, RegisterUtil.outsideProductSerialNumber);
					
					sc.setAttribute("zs_regTableStr", regTableStr);
					
					sc.setAttribute("zs_regFileStr", regFileStr);
				}
			}
		} catch (Exception e) {
			sc.setAttribute("hwKeyStr", null);
			sc.setAttribute("hwIdStr", null);
		}
//		System.out.println("加密密钥："+sc.getAttribute("hwKeyStr"));
//		System.out.println("硬件ID："+sc.getAttribute("hwIdStr"));
	}
}

class Timmer1 extends TimerTask {

	private Timer tt;
	private ServletContext sc;

	public Timer getTt() {
		return tt;
	}

	public void setTt(Timer tt) {
		this.tt = tt;
	}

	public ServletContext getSc() {
		return sc;
	}

	public void setSc(ServletContext sc) {
		this.sc = sc;
	}

	public Timmer1(Timer t,ServletContext sc) {
		this.tt = t;
		this.sc = sc;
	}

	public void run() {
		HardwareTime.runFun(sc);
	}
}
