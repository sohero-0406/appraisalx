package com.jeesite.common.utils;

import java.math.BigDecimal;

/**
 * 数学工具类
 *
 * @author lvchangwei
 * @version 2019-07-17
 */
public class MathUtils {

    /**
     * 将带百分号的数字，转换为BigDecimal类型返回
     *
     * @return
     */
    public static BigDecimal removePercent(String num) {
        String newNum = num.split("%")[0];
        return new BigDecimal(newNum).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 将数字添加%返回
     * @param num
     * @return
     */
    public static String addPercent(BigDecimal num) {
        return num.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
    }
}
