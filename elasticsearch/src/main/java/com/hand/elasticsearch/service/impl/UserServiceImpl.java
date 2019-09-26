package com.hand.elasticsearch.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hand.elasticsearch.entity.ResponseData;
import com.hand.elasticsearch.service.IUserService;
import com.hand.userservice.entity.IamUser;
import com.hand.userservice.service.IIamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private RestTemplate restTemplate;

    @Reference(version = "1.0")
    private IIamUserService iamUserService;

    @Override
    public ResponseData validateUser(String loginName, String password) {

        ResponseData responseData = new ResponseData();

        List<IamUser> iamUserList = iamUserService.query(loginName, password);

        if (CollectionUtils.isEmpty(iamUserList)) {
            responseData.setSuccess(false);
            responseData.setCode("ERROR");
            responseData.setMessage("用户名或密码不正确！");
        } else {
            responseData.setRows(iamUserList);
            responseData.setTotal((long) iamUserList.size());
        }

        return responseData;

        //String url = "http://10.211.55.29:8081/api/user/login?loginName=" + loginName + "&password=" + password;
        //return this.restTemplate.getForObject(url, ResponseData.class);
    }
}
