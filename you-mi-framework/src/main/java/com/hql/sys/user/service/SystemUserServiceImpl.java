package com.hql.sys.user.service;

import com.hql.db.aop.anno.DataSourceType;
import com.hql.sys.user.dao.SystemUserMapper;
import com.hql.sys.user.entity.SysUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hql
 * @date 2025年03月07日 下午6:44
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {
    @Resource
    SystemUserMapper systemUserMapper;

    @Override
    public List<SysUser> findAll() {
        return List.of();
    }

    @Override
    @DataSourceType("second")
    public SysUser findByUserName(String userName) {
        SysUser sysUser = systemUserMapper.selectByUserName(userName);
        return sysUser;
    }

    @Override
    public int save(SysUser sysUser) {
        return 0;
    }

    @Override
    public int update(SysUser sysUser) {
        return 0;
    }

    @Override
    public int delete(SysUser sysUser) {
        return 0;
    }
}
