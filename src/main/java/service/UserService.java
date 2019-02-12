/*
 * UserService.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package service;


import bean.User;
import com.google.common.base.Strings;
import exception.ObjectNotFoundException;
import mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.SystemInfoUtils;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    public User getCurrentUser() throws ObjectNotFoundException {
        String userName = SystemInfoUtils.retrieveCurrentUserName();
        if(Strings.isNullOrEmpty(userName)) {
            logger.error("No user login");
            throw new ObjectNotFoundException("No such user.", "You need to login");
        }
        User curUser = userMapper.selectSingleUserByName(userName);
        if(null == curUser) {
            throw new ObjectNotFoundException("No such user", curUser.getUserTypeName());
        }
        return curUser;
    }
}
