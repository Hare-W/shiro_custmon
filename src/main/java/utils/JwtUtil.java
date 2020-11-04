package utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private final static long EXPIRE_TIME = 60 * 1000;
    private final static String TOKEN_SECRET = "hikari";
    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static String sign(String username){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        Map<String,Object> header = new HashMap<String,Object>();
        header.put("algorithm","HMAC256");
        return JWT.create()
                .withHeader(header)
                .withClaim("username",username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            logger.info("token verify success");
            return true;
        }catch (JWTVerificationException e){
        }
        return false;
    }

    public static String getUsername(String token){
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            logger.info("Header:" + decodedJWT.getHeader() + ",Payload:" + decodedJWT.getPayload() + ",Signature:" + decodedJWT.getSignature() + ",Token" + decodedJWT.getToken());
            logger.info("username:" + decodedJWT.getClaim("username").asString());
        }catch (Exception e){

        }
        return null;
    }
}
