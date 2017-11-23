package io.redkite.music.analyzer.controller;

import static io.redkite.music.analyzer.common.WebConstants.JWT_TOKEN_HEADER_PARAM;

import com.redkite.plantcare.common.dto.TokensDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.redkite.music.analyzer.JwtExpiredTokenException;
import io.redkite.music.analyzer.model.User;
import io.redkite.music.analyzer.security.UserContext;
import io.redkite.music.analyzer.security.token.tools.AccessJwtToken;
import io.redkite.music.analyzer.security.token.tools.JwtTokenExtractor;
import io.redkite.music.analyzer.security.token.tools.JwtTokenFactory;
import io.redkite.music.analyzer.security.token.tools.JwtTokenParser;
import io.redkite.music.analyzer.security.token.tools.TokenVerifier;
import io.redkite.music.analyzer.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private TokenVerifier tokenVerifier;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenFactory tokenFactory;

  @Autowired
  private JwtTokenParser tokenParser;

  @Autowired
  private JwtTokenExtractor tokenExtractor;

//  @Autowired
//  private TokenInvalidationService tokenInvalidationService;


  @RequestMapping(value = "/token", method = RequestMethod.GET)
  public AccessJwtToken refreshToken(@RequestHeader(JWT_TOKEN_HEADER_PARAM) String token) throws JwtExpiredTokenException {
    Jws<Claims> refreshToken = tokenParser.parseClaims(tokenExtractor.extract(token));

    if (refreshToken == null || refreshToken.getBody() == null) {
      throw new AuthenticationServiceException("Token is invalid");
    }

    if (!(boolean) refreshToken.getBody().get("isRefresh")) {
      throw new AuthenticationServiceException("It is not refresh token");
    }

    if (!tokenVerifier.verify(refreshToken)) {
      throw new AuthenticationServiceException("Token is invalid");
    }

    Long subject = Long.parseLong(refreshToken.getBody().getSubject());
    User user = userService.getFullUser(subject);

    if (user.getRole() == null) {
      throw new InsufficientAuthenticationException("User has no roles assigned");
    }

    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().getName()));

    UserContext userContext = UserContext.create(user.getId(), authorities);

    return tokenFactory.createAccessJwtToken(userContext);
  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public void invalidateToken(@RequestBody TokensDto tokensDto) throws JwtExpiredTokenException {
    Jws<Claims> accessToken = tokenParser.parseClaims(tokensDto.getAccessToken());
//    Jws<Claims> refreshToken = tokenParser.parseClaims(tokensDto.getRefreshToken());
//    tokenInvalidationService.invalidateToken(accessToken.getBody().getId());
//    tokenInvalidationService.invalidateToken(refreshToken.getBody().getId());
    log.info("Token {} was invalidated", accessToken.getBody().getId());
  }

}
