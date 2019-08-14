package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/** 認可 */
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

  /** コンストラクタ */
  public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  /** 認可実装:JWTtokenの解析 */
  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    System.out.println("debug : CustomAuthorizationFilter : doFilterInternal");

    // 認証情報をヘッダから取得
    String token = req.getHeader(SecurityConstants.HEADER_STRING);

    // 認証情報が空 もしくは Bearerから始まっていない場合
    if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
      // 次のフィルタ呼び出し
      chain.doFilter(req, res);
      return;
    }

    // JWTtokenからユーザー情報に展開する
    UsernamePasswordAuthenticationToken authentication = getAuthentication(token);

    // ユーザー情報セット
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 次のフィルタ呼び出し
    chain.doFilter(req, res);
  }

  /** JWTtokenからユーザー情報に展開する */
  private UsernamePasswordAuthenticationToken getAuthentication(String token) {
    try {
      String noPrefixToken = token.replace(SecurityConstants.TOKEN_PREFIX, "");
      // tokenからクレーム取得
      Claims body =
          Jwts.parser()
              .setSigningKey(SecurityConstants.SECRET.getBytes())
              .parseClaimsJws(noPrefixToken)
              .getBody();

      // クレームからユーザー名
      String user = body.getSubject();

      // 取得したユーザー名をユーザー情報クラスに設定
      if (user != null) {
        // クレームから権限を取得
        @SuppressWarnings("unchecked")
        List<GrantedAuthority> authorities =
            AuthorityUtils.createAuthorityList(
                //  クレームから権限リスト(List<String>を取得)
                ((List<String>) body.get("authorities"))
                    // List<String>をString[]に変換
                    .toArray(new String[0]));
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
      }
      return null;
    } catch (SignatureException e) {
      // token不正
      throw e;
    } catch (ExpiredJwtException e) {
      // token期限切れ
      throw e;
    }
  }
}
