package com.hand.userservice.service.impl;

import com.hand.userservice.entity.IamUser;
import com.hand.userservice.mapper.IamUserMapper;
import com.hand.userservice.service.IIamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IamUserServiceImpl implements IIamUserService {

    @Autowired
    private IamUserMapper iamUserMapper;

    @Override
    public List<IamUser> query(IamUser iamUser) {
        return iamUserMapper.query(iamUser);
    }

    @Override
    public List<IamUser> query(String loginName, String password) {
        IamUser iamUser = new IamUser();
        iamUser.setLoginName(loginName);
        iamUser.setPassword(password);
        return iamUserMapper.query(iamUser);
    }
}
