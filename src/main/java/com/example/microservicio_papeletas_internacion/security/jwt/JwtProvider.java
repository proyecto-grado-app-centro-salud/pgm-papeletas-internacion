package com.example.microservicio_papeletas_internacion.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
public class JwtProvider {
    Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value(value = "${aws.cognito.identifyPoolUrl}")
    private String identityPoolUrl;

    @Value(value = "${aws.cognito.region}")
    private String region;

    @Value(value = "${aws.cognito.issuer}")
    private String issuer;


    @Value(value =  "${aws.cognito.jwk}")
    private String jwkUrl;

    private static final String USERNAME = "username";

    public DecodedJWT getDecodedJwt(String token) {
        String tokenWithoutBearer = token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
        RSAKeyProvider keyProvider = new AwsCognitoRSAKeyProvider(region, identityPoolUrl,jwkUrl);
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        verifier.verify(tokenWithoutBearer);
        return verifier.verify(tokenWithoutBearer);
    }


    public String getUserNameFromToken(String token) {
        DecodedJWT jwt = getDecodedJwt(token);
        String userName = jwt.getClaim(USERNAME).toString();
        return userName.replace("\"","");
    }

    public boolean validateToken(String token) {
        try {
            getDecodedJwt(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.error("Validate token failed: " + exception.getMessage());
        }
        return false;
    }


    public List<String> getRolesFromToken(String token) {
        final String keyRoles="cognito:groups";
        DecodedJWT jwt = getDecodedJwt(token);
        return jwt.getClaim(keyRoles).asList(String.class); 
    }


}