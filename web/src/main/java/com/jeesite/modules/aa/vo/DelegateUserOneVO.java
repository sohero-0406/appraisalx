package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DelegateUserOneVO {

    private String id;
    @NotBlank
    private String name;        // 委托方姓名
    @NotBlank
    private String idNum;        // 身份证号
    @NotBlank
    private String address;        // 委托方地址
    @NotBlank
    private String contact;        // 联系人
    @NotBlank
    private String phone;        // 电话
    @NotBlank
    private String entrustType;        // 委托书类型
    @NotBlank
    private String completeDate;        // 完成日期
}