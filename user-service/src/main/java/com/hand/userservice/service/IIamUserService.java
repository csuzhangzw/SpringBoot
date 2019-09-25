package com.hand.userservice.service;

import com.hand.userservice.entity.IamUser;

import java.util.List;

public interface IIamUserService {

    List<IamUser> query(IamUser iamUser);

    List<IamUser> query(String loginName, String password);
}
