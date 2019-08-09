package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

/** 認証 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;

    // ログインURLを変更する場合は以下を設定
    // setRequiresAuthenticationRequestMatcher(new
    // AntPathRequestMatcher(SecurityConstants.LOGIN_URL, "POST"));
  }

  // 認証の受付
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    try {
      System.out.println("debug : CustomAuthenticationFilter : attemptAuthentication");

      // リクエストボディを展開
      RequestUser userForm = new ObjectMapper().readValue(request.getInputStream(), RequestUser.class);

      // リクエストされたユーザ情報を設定
      UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(userForm.getUsername(),
          userForm.getPassword(), new ArrayList<>());

      // 認証開始
      return authenticationManager.authenticate(u);
    } catch (IOException e) {
      System.out.println("debug : error : " + e);
      throw new UsernameNotFoundException(e.toString());
    }
  }

  /** 認証成功時処理 */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    System.out.println("debug : CustomAuthenticationFilter : successfulAuthentication");

    // JWTの件名(ユーザー名を指定)
    String username = ((User) authResult.getPrincipal()).getUsername();
    // JWTの有効期限
    Date exp = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);

    // token
    String token = Jwts.builder().setSubject(username).setExpiration(exp)
        // 指定されたアルゴリズムを使用して指定されたキーで構築されたJWTに署名し、JWSを生成
        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
        // コンパクトでURLセーフな文字列にシリアル化
        .compact();

    // tokenをヘッダにセット
    response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
  }

  /** 認証失敗時処理 */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {
    System.out.println("debug : CustomAuthenticationFilter : unsuccessfulAuthentication");
    System.out.println(failed);
    super.unsuccessfulAuthentication(request, response, failed);
  }

  /** 認証データ展開用クラス */
  @Data
  public static class RequestUser {
    String username;
    String password;
  }
}
