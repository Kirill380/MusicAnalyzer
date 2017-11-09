package io.redkite.music.analyzer.security.token.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import org.springframework.stereotype.Component;

@Component
public class BloomFilterTokenVerifier implements TokenVerifier {

  @Override
  public boolean verify(Jws<Claims> tokenClaims) {
    return true;
  }
}