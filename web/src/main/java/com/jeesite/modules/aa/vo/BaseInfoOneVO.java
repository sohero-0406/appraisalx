package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.Valid;

@Data
public class BaseInfoOneVO {

    @Valid
    private DelegateUserOneVO delegateUser;
    @Valid
    private CarInfoOneVO carInfo;
}
