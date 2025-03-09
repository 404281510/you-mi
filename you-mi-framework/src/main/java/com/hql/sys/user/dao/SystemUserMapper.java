package com.hql.sys.user.dao;

import com.hql.sys.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hql
 * @date 2025年03月07日 下午6:45
 */
@Mapper
public interface SystemUserMapper {


    SysUser selectByUserName(String userName);
}
