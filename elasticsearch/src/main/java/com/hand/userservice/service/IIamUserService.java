package com.hand.userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hand.userservice.entity.IamUser;

import java.util.List;

@Service(version = "1.0", timeout = 6000, interfaceClass = IIamUserService.class)
public interface IIamUserService {

    List<IamUser> query(IamUser iamUser);

    List<IamUser> query(String loginName, String password);
}
