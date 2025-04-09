package com.hql.util;

import com.hql.constant.HttpStatus;
import lombok.Data;

/**
 * @author hql
 * @date 2025年04月08日 下午9:24
 */
@Data
public class R <T>{
    
    private int code;
    private String msg;
    
    private T data;


    public  static <T> R<T> success(T data) {
        R<T> resultData = new R<>();
        resultData.setCode(HttpStatus.SUCCESS);
        resultData.setMsg("");
        resultData.setData(data);
        return resultData;
    }

    public  static <T> R<T> fail(String msg) {
        R<T> resultData = new R<>();
        resultData.setCode(HttpStatus.SUCCESS);
        resultData.setMsg( msg);
        resultData.setData(null);
        return resultData;

    }
}
