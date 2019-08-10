package com.example.demo.security;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.User;
import com.example.demo.exception.NoDataException;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** 認証 */
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  UserService userService;

  /** 認証：ユーザー名とパスワードのチェック */
  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    System.out.println("debug : CustomUserDetailsAuthenticationProvider : retrieveUser");

    // パスワードの取得
    String password = (String) authentication.getCredentials();

    // ユーザー情報取得
    User user;
    try {
      user = userService.findByUsername(new User(username));
    } catch (NoDataException e) {
      // ユーザー情報を取得できなければ例外をスロー
      e.printStackTrace();
      throw new UsernameNotFoundException(username);
    }

    // ユーザIDとパスワードをチェック
    boolean isValid = false;

    // パスワードが一致するか
    if (userService.passwordCheck(password, user.getPassword()))
      isValid = true;
    if (!isValid) {
      // パスワードが一致しなければ例外をスロー
      throw new UsernameNotFoundException(username);
    }
    // 有効であるか
    boolean enabled = user.getEnabled();
    // アカウント有効期限が切れていないか
    boolean accountNonExpired = true;
    // 資格情報有効期限が切れていないか
    boolean credentialsNonExpired = true;
    // アカウントがロックされていないか
    boolean accountNonLocked = true;
    // 権限
    String[] auth = user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList())
        .toArray(new String[0]);
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(auth);

    // ユーザーオブジェクトを返却
    // この後、enable,accountNonExpired,credentialsNonExpired,accountNonLockedの検証を行う
    return new org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired,
        credentialsNonExpired, accountNonLocked, authorities);
  }

  /** 追加の認証チェック:retrieveUser成功後に呼ばれる */
  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    System.out.println("debug : CustomUserDetailsAuthenticationProvider : additionalAuthenticationChecks");
  }
}
