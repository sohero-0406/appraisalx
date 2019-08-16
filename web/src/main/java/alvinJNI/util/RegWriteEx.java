package alvinJNI.util;

public class RegWriteEx {
    static String LIB_REG_Write = "RegWriteEx.dll";
    static String DLL_MSVCP120 = "msvcp120.dll";
    static String DLL_MSVCR120 = "msvcr120.dll";

    static {
        String basePath = RegWriteEx.class.getResource("/").toString().replaceAll("file:/", "");
        System.load(basePath + DLL_MSVCR120);
        System.load(basePath + DLL_MSVCP120);
        System.load(basePath + LIB_REG_Write);
    }

    public native static String loadRegedit(String regInfoPath, String regKey);

    public native static int writeRegedit(String regInfoPath, String regKey,
                                          String regVal);

    /**
     * 往注册表内写信息
     *
     * @param regInfoPath 注册表内的注册项路径
     * @param regKey      注册项名称
     * @param regVal      注册项值
     * @param rootID      默认传0即可
     * @author zsl
     * @date 2018-9-10 上午11:41:46
     */
    public void writeRegeditDll(String regInfoPath, String regKey,
                                String regVal) {
        if (regInfoPath.indexOf("HKLM\\") != -1) {
            regInfoPath = regInfoPath.replace("HKLM\\", "");
        }
        RegWriteEx.writeRegedit(regInfoPath, regKey, regVal);
    }

}
