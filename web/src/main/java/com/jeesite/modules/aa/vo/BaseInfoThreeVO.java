package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.Valid;

@Data
public class BaseInfoThreeVO {

    @Valid
    private DelegateUserTwoVO delegateUser;
    @Valid
    private CarInfoThreeVO carInfo;
}
