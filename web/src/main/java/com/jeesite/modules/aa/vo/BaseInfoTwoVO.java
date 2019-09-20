package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.Valid;

@Data
public class BaseInfoTwoVO {

    @Valid
    private DelegateUserTwoVO delegateUser;
    @Valid
    private CarInfoTwoVO carInfo;
}
