package com.hql.security.entity;
import com.alibaba.fastjson.annotation.JSONField;
import com.hql.sys.role.entity.SysRole;
import com.hql.sys.user.entity.SysUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

/**
 * 类描述 -> 自定义用户
 *
 * @Author: hql
 * @Date: 2024/07/28
 */
@Setter
@Getter
public class CustomUser extends User {

    private SysUser sysUser;


    @JSONField(serialize = false, deserialize = false)
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        System.err.println("进入权限的获取方法");

        Set<SysRole> roles = new HashSet<>(sysUser.getSysRoleList());
        List<GrantedAuthority> authorities = new ArrayList<>(); // 授权信息列表
        // 将角色名称添加到授权信息列表中
        roles.forEach(role->
                authorities.add(new SimpleGrantedAuthority(role.getName())));
        // 将权限名称添加到授权信息列表中
        authorities.add(new SimpleGrantedAuthority("拥有所有权限"));
        return authorities; // 返回授权信息列表
    }

    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUserName(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }


}
