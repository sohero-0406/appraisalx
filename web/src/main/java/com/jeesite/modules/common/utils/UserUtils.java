package com.jeesite.modules.common.utils;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.jwt.JwtUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

/**
 * 获得前台、后台登录用户对象工具类
 *
 * @author lvchangwei
 * @version 2019-07-01
 */
public class UserUtils {
    public static ExamUser getExamUser() {
        HttpServletRequest request = ServletUtils.getRequest();
        String token = request.getHeader(JwtUtils.getHeader());
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(JwtUtils.getHeader());
        }
        if (StringUtils.isEmpty(token)) {
//            //学生
//            ExamUser examUser = new ExamUser();
//            examUser.setId("1183555282950410240");
//            examUser.setUserId("1182532557589159936");
//            examUser.setExamId("1183555113399865344");
//            //教师
//            ExamUser examUser = new ExamUser();
//            examUser.setPaperId("1184710167327510528");
//            examUser.setUserId("1166165115761696768");
//            return examUser;

            //todo
            return null;
        }
        Claims claims = JwtUtils.getClaimByToken(token);
        if (null == claims) {
            return null;
        }
        String userId = claims.getSubject();
        return CacheUtils.get("examUser", userId);
    }

}
