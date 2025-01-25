package com.example.microservicio_papeletas_internacion.security.jwt;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

public class AwsCognitoRSAKeyProvider implements RSAKeyProvider {
    private final URL awsStoreUrl;
    private final JwkProvider provider;
    Logger logger = LoggerFactory.getLogger(AwsCognitoRSAKeyProvider.class);
    
    public AwsCognitoRSAKeyProvider(String awsCognitoRegion, String identityPoolUrl,String jwkUrl) {
        logger.info("AwsCognitoRSAKeyProvider jwkUrl="+jwkUrl);
        String url = String.format(jwkUrl, awsCognitoRegion, identityPoolUrl);
        try {
            awsStoreUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Invalid URL provided, URL=%s", url));
        }
        provider = new JwkProviderBuilder(awsStoreUrl).build();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        try {
            return (RSAPublicKey) provider.get(kid).getPublicKey();
        } catch (JwkException e) {
            throw new RuntimeException(String.format("Failed to get JWT kid=%s from aws_kid_store_url=%s", kid, awsStoreUrl));
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}