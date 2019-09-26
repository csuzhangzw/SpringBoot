package com.hand.userservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hand.userservice.entity.IamUser;
import com.hand.userservice.mapper.IamUserMapper;
import com.hand.userservice.service.IIamUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "1.0", timeout = 3000)
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
