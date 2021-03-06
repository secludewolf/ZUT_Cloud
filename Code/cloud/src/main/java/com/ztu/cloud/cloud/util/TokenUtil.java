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
        return Jwts.builder().setSubject(role + "@" + id + "@" + (isRememberMe ? "1" : "0")).setIssuer(ISSUER)
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET.getBytes()).compact();
    }

    /**
     * 创建用户Token
     *
     * @param id           ID
     * @param isRememberMe 是否选择记住我
     * @return Token
     */
    public static String createUserToken(int id, boolean isRememberMe) {
        return createToken("user", id, isRememberMe);
    }

    /**
     * 创建管理员Token
     *
     * @param id           ID
     * @param isRememberMe 是否选择记住我
     * @return Token
     */
    public static String createAdminToken(int id, boolean isRememberMe) {
        return createToken("admin", id, isRememberMe);
    }

    /**
     * 刷新Token 如果Token有效期过半则刷新,否则正常返回
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
     * 判断是否是长期有效
     *
     * @param token Token
     * @return 结果
     */
    public static boolean getIsRememberMe(String token) {
        String subject = getSubject(token);
        String isRememberMe = subject.split("@")[2];
        return "1".equals(isRememberMe);
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
        return Integer.parseInt(sid);
    }

    /**
     * 获取角色
     *
     * @param token 用户Token
     * @return 角色
     */
    public static String getRole(String token) {
        String subject = getSubject(token);
        return subject.split("@")[0];
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
     * 判断Token有效期是否过半
     *
     * @param token Token
     * @return 判断结果
     */
    public static boolean isExpiring(String token) {
        boolean isRememberMe = getIsRememberMe(token);
        if (isRememberMe) {
            return getExpiration(token) - (System.currentTimeMillis() + (EXPIRATION_REMEMBER / 2)) < 0;
        } else {
            return getExpiration(token) - (System.currentTimeMillis() + (EXPIRATION / 2)) < 0;
        }
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
