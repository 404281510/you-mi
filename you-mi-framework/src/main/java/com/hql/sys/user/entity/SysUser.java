package com.hql.sys.user.entity;

import lombok.Data;

@Data
public class SysUser {

    private Long id;
    private String userName;
    private String password;
    private int status;
}
