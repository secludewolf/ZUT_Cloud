package com.ztu.cloud.cloud.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jager
 * @description Token工具
 * @date 2020/06/23-10:36
 **/
@Component
public class TokenUtil {
    /**
     * Header属性名称
     */
    public static final String TOKEN_HEADER = "Authorization";
    /**
     * Header内容前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Token校验方式
     */
    private final static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    /**
     * Token密匙
     */
    private static String SECRET;
    /**
     * Token发行人
     */
    private static String ISSUER;
    /**
     * 默认有效期
     */
    private static long EXPIRATION;
    /**
     * 最长有效期
     */
    private static long EXPIRATION_REMEMBER;

    /**
     * 创建Token
     *
     * @param role         角色
     * @param id           ID
     * @param isRememberMe 是否选择记住我
     * @return Token
     */
    public static String createToken(String role, int id, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        return Jwts.builder()
                .setSubject(role + "@" + id)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET.getBytes())
                .compact();
    }

    /**
     * 创建用户Token
     *
     * @param id           ID
     * @param isRememberMe 是否选择记住我
     * @return Token
     */
    public static String createUserToken(int id, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        return Jwts.builder()
                .setSubject("user" + "@" + id)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET.getBytes())
                .compact();
    }

    /**
     * 创建管理员Token
     *
     * @param id           ID
     * @param isRememberMe 是否选择记住我
     * @return Token
     */
    public static String createAdminToken(int id, boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        return Jwts.builder()
                .setSubject("admin" + "@" + id)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET.getBytes())
                .compact();
    }

    /**
     * 刷新Token
     * 如果Token有效期小于一天则刷新,否则正常返回
     *
     * @param token Token
     * @return Token
     */
    public static String refreshToken(String token) {
        if (isExpiring(token)) {
            String role = getRole(token);
            int id = getId(token);
            return createToken(role, id, false);
        } else {
            return token;
        }
    }


    /**
     * 获取ID
     *
     * @param token 用户Token
     * @return ID
     */
    public static int getId(String token) {
        String subject = getSubject(token);
        String sid = subject.split("@")[1];
        int id = Integer.parseInt(sid);
        return id;
    }

    /**
     * 获取角色
     *
     * @param token 用户Token
     * @return 角色
     */
    public static String getRole(String token) {
        String subject = getSubject(token);
        String role = subject.split("@")[0];
        return role;
    }

    /**
     * 获取有效期
     *
     * @param token 用户Token
     * @return 角色
     */
    public static long getExpiration(String token) {
        Claims body = getBody(token);
        return body.getExpiration().getTime();
    }

    /**
     * 判断角色是否为用户
     *
     * @param token Token
     * @return 判断结果
     */
    public static boolean isUser(String token) {
        return "user".equals(getRole(token));
    }

    /**
     * 判断角色是否为管理员
     *
     * @param token Token
     * @return 判断结果
     */
    public static boolean isAdmin(String token) {
        return "admin".equals(getRole(token));
    }

    /**
     * 判断Token是否会在一天内过期
     *
     * @param token Token
     * @return 判断结果
     */
    public static boolean isExpiring(String token) {
        return getExpiration(token) - (System.currentTimeMillis() + 24 * 60 * 60 * 1000) < 0;
    }

    private static Claims getBody(String token) {
        token = token.replace(TOKEN_PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
    }


    private static String getSubject(String token) {
        return getBody(token).getSubject();
    }

    @Value("${token.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${token.issuer}")
    public void setIssuer(String issuer) {
        ISSUER = issuer;
    }

    @Value("${token.expiration}")
    public void setExpiration(long expiration) {
        EXPIRATION = expiration;
    }

    @Value("${token.expiration.remember}")
    public void setExpirationRemember(long expirationRemember) {
        EXPIRATION_REMEMBER = expirationRemember;
    }
}
