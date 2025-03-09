package com.hql.sys.user.service;

import com.hql.db.aop.anno.DataSourceType;
import com.hql.sys.user.entity.SysUser;

import java.util.List;

/**
 * @author hql
 * @date 2025年03月07日 下午6:43
 */
public interface SystemUserService {

    List<SysUser> findAll();

    SysUser findByUserName(String userName);

    int save(SysUser sysUser);

    int update(SysUser sysUser);

    int delete(SysUser sysUser);

}
