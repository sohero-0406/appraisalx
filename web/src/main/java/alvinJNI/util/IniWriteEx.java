package alvinJNI.util;

public class IniWriteEx {
    static String LIB_REG_Write = "INIWrite.dll";
    static String DLL_MSVCP120 = "msvcp120.dll";
    static String DLL_MSVCR120 = "msvcr120.dll";

    static {
        String basePath = IniWriteEx.class.getResource("/").toString().replaceAll("file:/", "");
        System.load(basePath + DLL_MSVCR120);
        System.load(basePath + DLL_MSVCP120);
        System.load(basePath + LIB_REG_Write);
    }

    public native static int writeRegIni(String filePath, String regKey, String regVal);
//	

    /**
     * 更新ini文件
     *
     * @author zsl
     * @date 2018-9-11 下午06:01:53
     */
    public void writeIniDll(String filePath, String regKey, String regVal) {
        IniWriteEx.writeRegIni(filePath, regKey, regVal);
    }
}
