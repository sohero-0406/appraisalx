package alvinJNI;

import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import alvinJNI.util.LocalCommandExecutor;

public class HardwareTime {

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
        Timmer1 timmer = new Timmer1(timer, sc);
        timer.schedule(timmer, 1000, 10000);// ?\开始\间隔
    }

    /**
     * 获取加密狗硬件ID值
     */
    public static String hardwareId(ServletContext sc) {

        EncryJavaDll dll = new EncryJavaDll();
        int[] devserial = new int[2];
        devserial[0] = 0;
        String[] Serial = new String[2];
        try {
            Serial = RegisterUtil.decryptStr(RegisterUtil.registerTableValue("Serial", sc), RegisterUtil.ipsnDecryptStr().substring(0, 8)).split(";");
        } catch (Exception e) {
        }
        int v3 = dll.GetDeviceInfo(devserial, RegisterUtil.DevID, RegisterUtil.ProID, 0, Integer.parseInt(Serial[0]), Integer.parseInt(Serial[1]));
        String softdogSerial = "";
        if (v3 == 1) {
            String beforeStr = Integer.toHexString(devserial[0]).toUpperCase();
            String lastStr = Integer.toHexString(devserial[1]).toUpperCase();
            int beforeStrLen = beforeStr.length();
            for (int i = 0; i < 8 - beforeStrLen; i++) {
                beforeStr = "0" + beforeStr;
            }
            int lastStrLen = lastStr.length();
            for (int i = 0; i < 8 - lastStrLen; i++) {
                lastStr = "0" + lastStr;
            }
            softdogSerial = swapStr(beforeStr) + "-" + swapStr(lastStr);
        }

        return softdogSerial;
    }

    public static String swapStr(String str) {
        String s1 = str.substring(0, 4);
        String s2 = str.substring(4, 8);
        return s2.substring(2, 4) + s2.substring(0, 2) + s1.substring(2, 4) + s1.substring(0, 2);
    }

    /**
     * 获取加密狗字符串(密钥)值
     */
    public static String hardwareKeyStr(String regCode, ServletContext sc) {

        String resultStr = "";

        try {
            EncryJavaDll dll = new EncryJavaDll();
            regCode = regCode.substring(4, regCode.length() - 4);
            if (regCode.length() > 16) {
                regCode = regCode.substring(0, 16);
            }
            byte[] byte1 = regCode.getBytes();
            String[] Serial = RegisterUtil.decryptStr(RegisterUtil.registerTableValue("Serial", sc), RegisterUtil.ipsnDecryptStr().substring(0, 8)).split(";");
            int v2 = dll.EncryFunc(byte1, RegisterUtil.DevID, RegisterUtil.ProID, RegisterUtil.userpwd, RegisterUtil.filename, RegisterUtil.devindex, Integer.parseInt(Serial[0]), Integer.parseInt(Serial[1]));
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

    /**
     * 定时检索正式注册信息
     *
     * @author zsl
     * @date 2018-9-10 下午03:15:51
     */
    public static void runFun(ServletContext sc) {
        String hwKeyStr = null;
        String hwIdStr = null;
        try {
            String pathStr = "";
            LocalCommandExecutor lce = new LocalCommandExecutor();
            String er;
            String execStr;
            if (sc.getAttribute("pathStr") == null || StringUtils.isBlank(sc.getAttribute("pathStr").toString())) {
                for (Integer i = 0; i < RegisterUtil.registerTablePathList().size(); i++) {
                    pathStr = RegisterUtil.registerTablePathList().get(i);
                    execStr = "REG QUERY " + pathStr + "\\" + RegisterUtil.EncoderByMd5(RegisterUtil.ipsnDecryptStr()).toUpperCase();
                    er = lce.executeCommand(execStr);
                    if (StringUtils.isNotBlank(er)) {
                        break;
                    }
                }
                sc.setAttribute("pathStr", pathStr);
            }
            String zsReg = RegisterUtil.regInfoDecryptStr("zs", sc);
            if (StringUtils.isNotBlank(zsReg)) {
                String[] regInfo = zsReg.split("_");
                hwKeyStr = HardwareTime.hardwareKeyStr(regInfo[6], sc);
                hwIdStr = HardwareTime.hardwareId(sc);
            }
        } catch (Exception e) {
            hwKeyStr = null;
            hwIdStr = null;
        }
        sc.setAttribute("hwKeyStr", hwKeyStr);
        sc.setAttribute("hwIdStr", hwIdStr);
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

    public Timmer1(Timer t, ServletContext sc) {
        this.tt = t;
        this.sc = sc;
    }

    public void run() {
        HardwareTime.runFun(sc);
    }
}
