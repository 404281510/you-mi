package com.hql.login.service;

import com.hql.login.entity.LoginVo;

import java.util.Map;

public interface LoginService {

    Map<String, Object> login(LoginVo req);
}
