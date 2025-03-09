package com.hql.security.service;


import com.hql.security.entity.CustomUser;
import com.hql.sys.user.entity.SysUser;
import com.hql.sys.user.service.SystemUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    SystemUserService systemUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = systemUserService.findByUserName(username);
        sysUser.setStatus(1);
        if (Objects.isNull(sysUser)){
            throw new RuntimeException("用户名不存在！");
        }

        if(sysUser.getStatus() == 0) {
            throw new RuntimeException("账号已停用");
        }
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
