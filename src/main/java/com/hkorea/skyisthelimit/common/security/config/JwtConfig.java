package com.hkorea.skyisthelimit.common.security.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

  private static final String PUBLIC_KEY_B64 =
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsqlV8QG6bGlRYFrFcRX9" +
          "1KKStGd7vBCsM0PXdDpUtxIiJw4eQIFo+rYZdbevO1p0wOabKHSFhk96Tc1oZt5X" +
          "E/8QJ5ej6x4XOXtsZ3gfODyaSnmjPUx37eDywNFS1w+21ctLf+aFBd0t64DcDeNm" +
          "fBgOmjmMou5okTy6BZbOULmgRbjIPWdcpALCXLDSKy2Xy7ASQzqY2btNrtsQyXL3" +
          "DYT4GsTiRrnJ8mKVs9TupILxuLx2V4hYtxjanaIkwQdMOdE9Pyw1SVuDAFWjGDwC" +
          "9PxNmtwukV0yHj4G89Tj3B64AdOgj58xMdttLW5qVENwXBy9eVGjzuC2KQaw0NYr" +
          "9QIDAQAB";

  private static final String PRIVATE_KEY_B64 =
      "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyqVXxAbpsaVFg" +
          "WsVxFf3UopK0Z3u8EKwzQ9d0OlS3EiInDh5AgWj6thl1t687WnTA5psodIWGT3pN" +
          "zWhm3lcT/xAnl6PrHhc5e2xneB84PJpKeaM9THft4PLA0VLXD7bVy0t/5oUF3S3r" +
          "gNwN42Z8GA6aOYyi7miRPLoFls5QuaBFuMg9Z1ykAsJcsNIrLZfLsBJDOpjZu02u" +
          "2xDJcvcNhPgaxOJGucnyYpWz1O6kgvG4vHZXiFi3GNqdoiTBB0w50T0/LDVJW4MA" +
          "VaMYPAL0/E2a3C6RXTIePgbz1OPcHrgB06CPnzEx220tbmpUQ3BcHL15UaPO4LYp" +
          "BrDQ1iv1AgMBAAECggEAEk3HdIDPEt7KW8MI8eBFKFT8jzd/5YFWxwzHBM0aE0Rs" +
          "E9C+ODEZ6n+gBZ+rN/s5NW8ay87+RAed+d0Wul1CkhWiV9ARFtw3GGhE76fOmQIa" +
          "c5FZMEVGjywp6qe3Yzmkq2wfwMuC6tohY3yI7vJSdFm5m0LUnferZl/QlhSAcCQc" +
          "LfxofayXi5Ec1KBnewa43ksN7DlOI6wXuBneAS5P/xX/G/JlKig9Bd+HtseUI3Vl" +
          "Pf4CPrhkju1rVdhyYbrbFnj/tAi/92ETFRzzozcqVaxrOT8HynuCYVWFsyJYUuqK" +
          "qnOdXBeXtgzJMshczqG/JAW0p7rGE4AsXyvYlza4GQKBgQDzplqF0sjqbsno8szj" +
          "ZAipTtQIxFwGSDoan4Wyglp6GZlR4k2H5qZNim7Peaei6+Ifj1pjBFuX2Qav+fQj" +
          "Nni9te702td+9vhk8SRqxW3pTVrr/twCwx7mHZ4EFfSa1FSov3G1iIrTBHwuJJsv" +
          "b96OHyyj912t+IOUoQksPY5vSQKBgQC7t65PTPFYx7QLV6zkHUICE0lSh0cHbl7c" +
          "I9o9Zm8nzsFxoBaKyUFp5Ace8tGrChi8Jaf0br7bt2GuhekADpqJKizfjG8FOPa/" +
          "/aIRpVmR7P6PZ3hIMPuqBa5CnEtjVpXtGNTcmRA9Dsi6Hj26LqfTbLVbXc96tkd6" +
          "rhmvMzIbTQKBgBMc6Jr41wP03ufkLAhmb3uAkIeDh8iCX/R4sQ84xIoxeeJ/gx93" +
          "C3KnuluSKoFQSbO6FhgQXnMB9lFVpT4h5sTTAcS5ahAupXudowKxLBcSF2DN5Epf" +
          "LgxVJHEjm8WNhHqcUfowlNtKzcb83iORff7eGS1fmTytcvz0yPhsd8DpAoGAQ8Zn" +
          "yv3uza0c1lOLerkNkg5AlOl+vbId0cGoFC8dxvOhaFLykJ1lXsQTxhrJlMWKdLPr" +
          "1hb+Ffo57HcwzvJ23Ts7BGOB2hLdFCiREWnv0v4MUt560SXOV27Bog8oMO+cJgSn" +
          "JhalhEYINxqgEs6xjWZcfGZ8eMPDPwoI//auCqkCgYAKF4I7V9x4IizNrN0Gi0aQ" +
          "O/vkuPIqolWZ5RYQDdcVhc7VJOlj0S9ZrZWY4g5lCv/ei8PAlDs6SYqiMjevAj4N" +
          "7AIrqRmCgXCvxufKXVgd7YVcwkfTAdI/A4n/QDtPFiHISAIccEvQhIkk5eVNleZL" +
          "qY/ls4RYkup1VgxTTrfmSg==";

  @Bean
  public KeyPair rsaKeyPair() {

    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec privateSpec =
          new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY_B64));
      X509EncodedKeySpec publicSpec =
          new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY_B64));

      RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(privateSpec);
      RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(publicSpec);

      return new KeyPair(publicKey, privateKey);

    } catch (Exception e) {
      throw new IllegalStateException("Failed to generate RSA KeyPair", e);
    }
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey)
        .keyID(UUID.randomUUID().toString()).build();
    JWKSet jwkSet = new JWKSet(rsaKey);

    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    try {
      // 모든 키 선택
      JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().build());
      List<JWK> keys = jwkSource.get(jwkSelector, null);

      if (keys.isEmpty()) {
        throw new IllegalStateException("No JWK keys available for JwtDecoder");
      }

      JWK jwk = keys.get(0);

      if (!(jwk instanceof RSAKey rsaKey)) {
        throw new IllegalStateException("Expected RSAKey for JwtDecoder");
      }

      return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to create JwtDecoder from JWKSource", e);
    }
  }


}
