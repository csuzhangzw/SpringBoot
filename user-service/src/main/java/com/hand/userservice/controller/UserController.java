package com.hand.userservice.controller;

import com.hand.userservice.entity.IamUser;
import com.hand.userservice.entity.ResponseData;
import com.hand.userservice.service.IIamUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IIamUserService iIamUserService;

    /**
     * 用户登陆验证
     *
     * @param loginName 用户名
     * @param password  密码
     * @return 响应结果
     */
    @GetMapping("/api/user/login")
    @ResponseBody
    public ResponseData login(@RequestParam(name = "loginName") String loginName,
                                @RequestParam(name = "password") String password) {

        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMessage("用户名或密码不能为空！");
        }

        List<IamUser> iamUserList = iIamUserService.query(loginName, password);
        if (CollectionUtils.isEmpty(iamUserList)) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMessage("用户名或密码不正确！");
        }

        return new ResponseData(iamUserList);
    }
}
