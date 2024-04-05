package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

/**
 * @author TZH
 */
public class JwtUtil {

    private static final String KEY = "pleasantDream";

    /**
     * 接收业务数据,生成token并返回
     * @param claims 业务数据
     * @return jwt令牌(字符串)
     */
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                //生效时间一天
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(KEY));
    }

    /**
     * 接收token,解析token,并返回业务数据
     * @param token jwt令牌(字符串)
     * @return 业务数据
     */
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
