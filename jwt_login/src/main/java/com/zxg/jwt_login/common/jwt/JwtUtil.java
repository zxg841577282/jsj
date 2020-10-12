package com.zxg.jwt_login.common.jwt;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtUtil {

    /**
     * JWT时间默认盐值 666666
     */
    private String calendarSalt = "SALT-JSJ";
    /**
     * JWT时间默认单位 分钟
     */
    private static final int CALENDAR_FIELD = Calendar.MINUTE;
    /**
     * JWT时间默认长度
     */
    private int calendarInterval = 1000;
    /**
     * JWT时间默认名称
     */
    private String jwtUserName = "userId";

    /**
     * JWT生成Token.<br/>
     * <p>
     * JWT构成: header, payload, signature
     *
     * @param userId 登录成功后用户user_id, 参数user_id不可传空
     */
    public String createToken(Integer userId) {
        try {
            if (ObjectUtil.isEmpty(userId)) {
                log.error("username");
                return null;
            }
            log.debug("userId:{}", userId);
            //设置过期时间 1000分钟
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(CALENDAR_FIELD, calendarInterval);
            Date expiresDate = nowTime.getTime();
            log.debug("expireDate:{}", expiresDate);
            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(calendarSalt);
            return JWT.create()
                    .withClaim(jwtUserName, userId)
                    .withIssuedAt(DateTime.now())
                    .withExpiresAt(expiresDate)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("generateToken exception", e);
        }
        return null;
    }

    public boolean verifyToken(String token, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 解析token，获取token数据
     */
    public DecodedJWT getJwtInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * 获取过期时间
     */
    public Date getExpireDate(String token) {
        DecodedJWT decodedJWT = getJwtInfo(token);
        return decodedJWT.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     */
    public boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(DateTime.now());
    }

    public Integer getUserId(String token) {
        if (ObjectUtil.isEmpty(token)) {
            return null;
        }
        DecodedJWT decodedJWT = getJwtInfo(token);
        return decodedJWT.getClaim(jwtUserName).asInt();
    }
}
