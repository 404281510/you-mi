package com.hql.security.filter;



import com.hql.exception.BizException;
import com.hql.security.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 类描述 -> Jwt认证过滤器
 *
 * @Author: ywz
 * @Date: 2024/07/28
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
         String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            // 说明本次请求不需要token，放行
            filterChain.doFilter(request, response);
            return;
        }

        //解析token，获取用户id
        String userId;
        String authorities;
        try {
            Claims claims1 = JWTUtils.parseToken(token);
            userId = claims1.get("userId", String.class);
            authorities = claims1.get("authorities", String.class);
        } catch (Exception e) {
            log.error("token非法");
            throw new RuntimeException("token非法");
        }

        // 根据id查询redis中是否存在token，如果不存在，说明token过期了
        String redisKey = "login:" + userId;
        String jwt = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(jwt)) {
            throw new BizException("用户未登录");
        }
        //每次请求刷新token存活时间
        redisTemplate.expire(redisKey,30, TimeUnit.MINUTES);
        // 存入SecurityContextHolder
        Collection<GrantedAuthority> authorityList = new ArrayList<>();
        for (String authority : authorities.split(",")) {
            authorityList.add(new SimpleGrantedAuthority(authority));
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(token, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
