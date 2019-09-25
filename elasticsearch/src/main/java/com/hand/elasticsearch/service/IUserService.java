package com.hand.elasticsearch.service;

import com.hand.elasticsearch.entity.ResponseData;

public interface IUserService {

    ResponseData validateUser(String loginName, String password);
}
