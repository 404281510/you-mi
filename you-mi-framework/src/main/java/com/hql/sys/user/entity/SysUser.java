package com.hql.sys.user.entity;

import com.hql.sys.role.entity.SysRole;
import lombok.Data;

import java.util.List;

@Data
public class SysUser {

    private Long id;
    private String userName;
    private String password;

    private List<SysRole> sysRoleList;
    private int status;
}
