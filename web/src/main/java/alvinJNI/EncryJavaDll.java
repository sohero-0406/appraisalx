package alvinJNI;


public class EncryJavaDll {

    static String DLL_MSVCP120 = "msvcp120.dll";
    static String DLL_MSVCR120 = "msvcr120.dll";
    static String LIB_ENCRYDLLJAVAF = "EncryDLLJavaF.dll";

    static {
        String basePath = EncryJavaDll.class.getResource("/").toString().replaceAll("file:/", "");
        System.load(basePath + DLL_MSVCR120);
        System.load(basePath + DLL_MSVCP120);
        System.load(basePath + LIB_ENCRYDLLJAVAF);
    }

    // 说明:产品ID为0xFFFFFFFF时,代表忽略产品ID;bv与lv为0时,代表忽略序列号;
    // 参数:返回密钥串，密钥串长度，开发商ID，产品ID，登录密码，文件名称（带扩展名），锁顺序号，注册表SERIAL的解密字符串以";"拆分[0]（bv），注册表SERIAL的解密字符串以";"拆分[1]（lv）
    public native int EncryFunc(byte[] str, int DevID, int ProID, String userpwd, String filename, int devindex, int bv, int lv);

    //public native byte[] EncryFunc(String str,int len,int DevID,int ProID,int devindex,int bv,int lv);

    // 参数:加密锁返回序列号，开发商ID，产品ID，锁顺序号，注册表SERIAL的解密字符串以";"拆分[0]（bv），注册表SERIAL的解密字符串以";"拆分[1]（lv）
    public native int GetDeviceInfo(int[] str, int DevID, int ProID, int devindex, int bv, int lv);
	
	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//for (int b = 0; b < 100;b++) {
			
		
		EncryJavaDll dll = new EncryJavaDll();
		String v = "ABCDEFG12345669874562D";
		String userpwdStr = "00000000";
		String filenameStr = "VJL4MV7T.evx";// LIGB2H8U
		if (v.length() > 16) {
			v = v.substring(0, 16);
		}
		byte[] byte1 = v.getBytes();
		byte[] userpwd = userpwdStr.getBytes();
		int[] devserial = new int[2];
		devserial[0] = 0;
		int v3 = dll.GetDeviceInfo(devserial, 1497912136, 0xFFFFFFFF, 0, 0, 0);
		String softdogSerial = "";
		if (v3 == 1) {
			String beforeStr = Integer.toHexString(devserial[0]).toUpperCase();
			String lastStr = Integer.toHexString(devserial[1]).toUpperCase();
			int beforeStrLen = beforeStr.length();
			for (int i = 0; i < 8 - beforeStrLen; i++) {
				beforeStr += "0";
			}
			String lastStr1 = lastStr.substring(0, 4);
			String lastStr2 = lastStr.substring(4);
			String lastStr1_1 = lastStr1.substring(2)
					+ lastStr1.substring(0, 2);
			softdogSerial = beforeStr + "-" + lastStr2 + lastStr1_1;
			softdogSerial += "(" + devserial[0] + "+" + devserial[1] + ")";
		}

		System.out.println("设备序列号=" + softdogSerial);
		//}

	}*/
}
