/*
 * SystemInfoUtils.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package utils;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;



public class SystemInfoUtils {
    public static String retrieveCurrentUserName() {
        final Subject currentUser = SecurityUtils.getSubject();
        final String curUser = (String) currentUser.getPrincipal();
        return curUser;
    }
}
