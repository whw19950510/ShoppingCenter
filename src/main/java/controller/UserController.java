/*
 * UserController.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package controller;


import bean.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import enums.UserType;
import exception.DisplayInformationException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/users")
public class UserController {
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody final User curUser)
            throws DisplayInformationException {
        final Subject currentUser = SecurityUtils.getSubject();
        logger.warn("principle {}", currentUser.getPrincipal());
        logger.warn("login {}", curUser.getUserName());
        if (!currentUser.isAuthenticated()) {
            final UsernamePasswordToken upToken = new UsernamePasswordToken(curUser.getUserName(),
                    curUser.getPassword());
            upToken.setRememberMe(true);
            try {
                currentUser.login(upToken);
                //load authentication info
                List<String> validRole = new ArrayList<String>();
                validRole.add(UserType.BUYER.getValue());
                validRole.add(UserType.SELLER.getValue());
//                currentUser.checkRoles(validRole);
                return ImmutableMap.of("username", (Object)curUser.getUserName());
            } catch (final AuthenticationException e) {
//                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                logger.warn("server error here");
                throw new DisplayInformationException("用户名或密码错误");
            }
        } else {
            logger.info("current user {} is already authenticated", curUser.getUserName());
            return ImmutableMap.of("username", (Object)curUser.getUserName());
        }
    }

    @RequestMapping(value = "/auth", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> logout() throws
            JsonProcessingException {
        final Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() || currentUser.isRemembered()) {
            currentUser.logout();
            return ImmutableMap.of("status", (Object)"success");
        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ImmutableMap.of("status", (Object)"invalid");
        }
    }
}
