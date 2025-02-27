package com.hql.pubg.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SystemTenantMapper {

     List<Map<String,Object>> selectTenant();
}
