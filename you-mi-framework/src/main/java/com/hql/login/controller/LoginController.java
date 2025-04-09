package com.hql.login.controller;

import com.hql.login.entity.LoginVo;
import com.hql.login.service.LoginService;
import com.hql.util.R;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequestMapping("/system")
public class LoginController {
    @Resource
    private LoginService loginService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public R<Map<String,Object>> login(@RequestBody LoginVo req) {
        Map<String,Object> map = loginService.login(req);
        return R.success(map);
    }
}