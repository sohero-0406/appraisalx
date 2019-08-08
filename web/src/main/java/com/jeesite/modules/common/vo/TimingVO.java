package com.jeesite.modules.common.vo;

public class TimingVO {

    public String examType;     //1-倒计时 2-正计时
    public String timer;     //时间

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
