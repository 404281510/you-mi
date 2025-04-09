package com.hql;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hql
 * @date 2025年03月30日 下午3:11
 */
@RestController
@RequestMapping("/test")
public class TestPermissionController {

    /*测试@PreAuthorize注解
     * 作用：使用在类或方法上，拥有指定的权限才能访问（在方法运行前进行校验）
     * String类型的参数：语法是Spring的EL表达式
     * 有权限：test3权限
     * hasRole：会去匹配authorities，但是会在hasRole的参数前加上一个ROLE_前缀，
     * 所以在定义权限的时候需要加上ROLE_前缀
     * role和authorities的关系是：role是一种复杂的写法，有ROLE_前缀，authorities是role的简化写法
     * 如果使用
     * hasAnyRole：则匹配的权限是在authorities加上前缀ROLE_
     * 推荐使用
     * hasAnyAuthority：匹配authorities，但是不用在authorities的参数前加上ROLE_前缀
     * */
    @PreAuthorize("@ss.hasAuthority('拥有所有权限')")
    @GetMapping("/test3")
    public String test3(){
        System.out.println("一个请求");
        return "一个test3请求";
    }

    /*
     @PostAuthorize：在方法返回时进行校验。
     可以还是校验权限、或者校验一些其他的东西（接下来我们校验返回值的长度）
    *返回结果的长度大于3、则认为是合法的
    returnObject：固定写法，代指返回对象
    * */
    @PostAuthorize("returnObject.length()>4")
    @GetMapping("/test4")
    public String test4(){
        System.out.println("一个test4请求");

        return "龙傲天之飞龙在天";
    }

    /*
     * @PreFilter：过滤符合条件的数据进入到接口
     * */
    @PostFilter("filterObject.length()>3")
    @GetMapping("/test5")
    public List<String> test5(){
        System.out.println("一个test4请求");
        List<String> list = new ArrayList<>();
        list.add("张三3");
        list.add("王麻子2");
        list.add("狗叫什么1");
        list.add("王麻子1");
        return list;
    }

    /*
     * @PreFilter：过滤符合条件的数据返回，数据必须是Collection、map、Array【数组】
     * */
    @PreFilter("filterObject.length()>5")
    @PostMapping("/test6")
    public List<String> test6(@RequestBody List<String> list){
        return list;
    }

}
