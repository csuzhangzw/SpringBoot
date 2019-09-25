package com.hand.userservice.mapper;

import com.hand.userservice.entity.IamUser;

import java.util.List;

public interface IamUserMapper {

    List<IamUser> query(IamUser iamUser);
}
