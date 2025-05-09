package com.hql.login.service;

import com.hql.login.entity.LoginVo;
import com.hql.security.JWTUtils;
import com.hql.security.entity.CustomUser;
import com.hql.sys.user.entity.SysUser;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public Map<String, Object> login(LoginVo req) {
        // 将表单数据封装到 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
        // authenticate方法会调用loadUserByUsername
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 校验成功，强转对象
        CustomUser customUser = (CustomUser) authenticate.getPrincipal();
        SysUser sysUser = customUser.getSysUser();
        // 校验通过返回token
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userId", sysUser.getId().toString());
        resultMap.put("userName", sysUser.getUserName());
        resultMap.put("authorities", customUser.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.joining(",")));
        String token = JWTUtils.generateToken(resultMap,null);

        boolean loginFlag = redisTemplate.opsForValue().setIfAbsent("login:"+ sysUser.getId().toString(), token, Duration.ofMinutes(2));
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
