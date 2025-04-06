package com.hql.security.config;
import com.hql.security.filter.JwtAuthenticationTokenFilter;
import com.hql.security.service.AuthenticationEntryPointImpl;
import com.hql.security.service.LogoutSuccessHandlerImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import static org.springframework.security.config.Customizer.withDefaults;


/**
 * 类描述 -> Spring Security 配置类
 *
 * @Author: ywz
 * @Date: 2024/09/27
 */
@Configuration
@EnableWebSecurity // 是开启SpringSecurity的默认行为
@EnableMethodSecurity // 开启方法级别的权限注解，Security6新增
public class SecurityConfig {
//    @Resource
//    private AccessDeniedHandlerImpl accessDeniedHandler;
//    @Resource
//    private AuthenticationEntryPointImpl authenticationEntryPoint;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    //退出处理类
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    // 放行接口（登录、注册、获取验证码、忘记密码、下载图片）
    private static final String[] IGNORE_URI = {"/system/login", "/system/register", "/system/captcha", "/system/forgetPassword", "/image/downloadImage", "/file/downloadFile"};
    // swagger静态资源放行
    private static final String[] SWAGGER_URIS = {"/swagger-resources/**", "/swagger-ui/**", "/v3/**", "/error"};

    /**
     * 配置PasswordEncoder（密码编码器）
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置AuthenticationManager（认证管理器）
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置AuthenticationProvider
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
                            // 放行接口
                            authorize.requestMatchers(IGNORE_URI).permitAll();
                            authorize.requestMatchers(SWAGGER_URIS).permitAll();
                            // 其他接口需要认证
                            authorize.anyRequest().authenticated();
                        }
                ).csrf(AbstractHttpConfigurer::disable) // 基于token，不需要csrf
                // 添加Logout filter
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
                // 保证jwt在认证前执行
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 基于token，不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 认证失败处理类
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .httpBasic(withDefaults())
                .formLogin(withDefaults());
        //允许跨域
        http.cors((cors) -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");  //设置允许跨域请求的域名
        config.addAllowedMethod("*");  //设置允许的请求方式
        config.addAllowedHeader("*");  //设置允许的header属性
        config.setAllowCredentials(true);  //是否允许cookie
        config.setMaxAge(3600L);  //跨域允许时间

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  //设置允许跨域的路径
        return source;
    }


}