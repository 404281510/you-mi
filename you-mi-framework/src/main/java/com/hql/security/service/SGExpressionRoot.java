package com.hql.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author hql
 * @date 2025年03月30日 下午4:00
 */
@Component("ss")
public class SGExpressionRoot {
    public boolean hasAuthority(String authority){
        //
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();
        Set<String> permissions =  AuthorityUtils.authorityListToSet(grantedAuthorities);
        return permissions.contains(authority);
    }
}
