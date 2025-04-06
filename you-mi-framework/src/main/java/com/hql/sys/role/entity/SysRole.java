package com.hql.sys.role.entity;

import lombok.Data;

/**
 * @author hql
 * @date 2025年03月30日 下午2:52
 */
@Data
public class SysRole {
    private int id;
    private String name;
    private String description;
    private int status;
}
