package com.hand.elasticsearch.service.impl;

import com.hand.elasticsearch.entity.ResponseData;
import com.hand.elasticsearch.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseData validateUser(String loginName, String password) {
        String url = "http://10.211.55.29:8081/api/user/login?loginName=" + loginName + "&password=" + password;
        return this.restTemplate.getForObject(url, ResponseData.class);
    }
}
