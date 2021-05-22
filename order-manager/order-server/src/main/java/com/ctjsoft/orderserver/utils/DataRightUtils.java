package com.ctjsoft.orderserver.utils;

import org.datagear.management.domain.Role;
import org.datagear.management.domain.User;

import java.util.Set;

public class DataRightUtils {

    public static Boolean getUserDataType(User user) {

        Boolean flag = false;
        Set<Role> roles = user.getRoles();
        //     Role role =
        for (Role role : roles) {
            if (role.getDataType() != null && role.getDataType().equals("2")) { // 2 为所有权限
                flag = true;
            }
        }
        return flag;
    }


    public static Integer getUserFlowType(User user) {

        //Boolean flag = false;
        Set<Role> roles = user.getRoles();
        //     Role role =
        Integer flowType = 0;
        for (Role role : roles) {
            if (role.getFlowType() != null && Integer.parseInt(role.getFlowType()) > flowType) {
                flowType = Integer.parseInt(role.getFlowType());
            }
        }
        return flowType;
    }
}
