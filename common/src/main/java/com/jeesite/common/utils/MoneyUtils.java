package com.jeesite.common.utils;

/**
 * 将数字金额转换为大写字符工具类
 */

public class MoneyUtils {

    public static String change(double v) {

        String UNIT = "万仟佰拾亿仟佰拾万仟佰拾元角分";
        String DIGIT = "零壹贰叁肆伍陆柒捌玖";
        double MAX_VALUE = 9999999999999.99D;
        if (v < 0 || v > MAX_VALUE){
            return "参数非法!";
        }
        long l = Math.round(v * 100);
        if (l == 0){
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }

}
