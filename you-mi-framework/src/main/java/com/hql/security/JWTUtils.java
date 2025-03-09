package com.hql.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTUtils {

    private static final String SIGN_KEY = "mijiu";// 加密密钥

    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000; //到期时间，12小时，单位毫秒
    private static final byte[] SECRET_KEY = SIGN_KEY.getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName","hql001");
        claims.put("userName1","hql001");
        String token = generateToken(claims,null);
        System.out.println(token);
        Claims claims1 = parseToken(token);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String sd = bCryptPasswordEncoder.encode("1");
        System.out.println(claims1);
    }

    /**
     * 生成token令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @param subject 主题(用户类型)
     * @return token
     */
    public static String generateToken(Map<String,Object> claims, String subject) {
        return Jwts.builder()
                .setId(Claims.ID)//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为令牌的唯一标识。
                .setSubject("mijiu")//设置主题,一般为用户类型
                .setIssuedAt(new Date())//设置签发时间
                .addClaims(claims)//设置负载
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)//设置签名算法
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))//设置令牌过期时间
                .compact();//生成令牌
    }

    /**
     * 解析token令牌
     * @param token token令牌
     * @return 负载
     */
    public static Claims parseToken(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     *  验证token令牌
     * @param token 令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新Token
     * @param token 旧的Token令牌
     * @return 新的Token令牌
     */
    public static String refreshToken(String token) {
        try {
            // 解析旧的Token，获取负载信息
            Claims claims = parseToken(token);
            // 生成新的Token，设置过期时间和签名算法等参数
            return generateToken(claims, claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException("无法刷新令牌！", e);
        }
    }


    /**
     * 从令牌中获取主题信息
     * @param token 令牌
     * @return 主题信息(用户类型)
     */
    public static String getSubjectFromToken(String token) {
        try {
            Claims claims = parseToken(token); // 解析令牌，获取负载信息
            return claims.getSubject(); // 返回主题信息
        } catch (Exception e) {
            throw new RuntimeException("无法从令牌中获取主题。", e);
        }
    }

}
