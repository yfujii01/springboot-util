package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

/** 認可 */
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    /** コンストラクタ */
    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /** 認可実装:JWTtokenの解析 */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
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
            // tokenから件名(ユーザー名)を取得
            String user = Jwts.parser()
                    // 鍵情報
                    .setSigningKey(SecurityConstants.SECRET.getBytes())
                    // tokenセット
                    .parseClaimsJws(noPrefixToken)
                    // 件名(ユーザー名)取得
                    .getBody().getSubject();

            // 取得したユーザー名をユーザー情報クラスに設定
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
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
