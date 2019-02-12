/*
 * UserRealm.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package component;


import bean.User;
import com.google.common.base.Strings;
import mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(UserRealm.class);

    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        String currentUserName = (String) super.getAvailablePrincipal(principals);
        User curUser = userMapper.selectSingleUserByName(currentUserName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(!Strings.isNullOrEmpty(curUser.getUserTypeName())) {
            simpleAuthorizationInfo.addRole(curUser.getUserTypeName());
        } else {
            logger.error("No such user");
        }
        return simpleAuthorizationInfo;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        //根据用户名查询密码，有安全管理器负责对比查询出的数据库中的密码和页面输入的密码是否一致
        User user = userMapper.selectSingleUserByName(username);
        if (user == null) {
            return null;
        }
        String passwordFromDB = user.getPassword();

        //最后的比对需要交给安全管理器
        AuthenticationInfo info =
                new SimpleAuthenticationInfo(user.getUserName(), passwordFromDB,
                        this.getClass().getName());
        return info;
    }
}
