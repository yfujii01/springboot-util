package com.example.demo.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** 認証 */
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  /** ダミー実装:認証を通すためのユーザーとパスワードの組み合わせ */
  Map<String, String> userPasswordMap = new HashMap<String, String>() {
    private static final long serialVersionUID = 1L;
    {
      put("hoge", "fuga");
      put("user", "password");
    }
  };

  /** 認証：ユーザー名とパスワードのチェック */
  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    System.out.println("debug : CustomUserDetailsAuthenticationProvider : retrieveUser");

    // パスワードの取得
    String password = (String) authentication.getCredentials();

    // =======================================================
    // ダミー実装:本来はここでDBなどから値をとってきてチェックする
    // =======================================================
    // ユーザIDとパスワードをチェック
    boolean isValid = false;
    if (password.equals(userPasswordMap.get(username)))
      isValid = true;
    if (!isValid) {
      throw new UsernameNotFoundException(username);
    }
    // 有効であるか
    boolean enabled = true;
    // アカウント有効期限が切れていないか
    boolean accountNonExpired = true;
    // 資格情報有効期限が切れていないか
    boolean credentialsNonExpired = true;
    // アカウントがロックされていないか
    boolean accountNonLocked = true;
    // 権限
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER");

    // ユーザーオブジェクトを返却
    // この後、enable,accountNonExpired,credentialsNonExpired,accountNonLockedの検証を行う
    return new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
  }

  /** 追加の認証チェック:retrieveUser成功後に呼ばれる */
  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    System.out.println("debug : CustomUserDetailsAuthenticationProvider : additionalAuthenticationChecks");
  }
}
