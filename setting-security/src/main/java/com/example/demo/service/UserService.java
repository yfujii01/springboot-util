package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.NoDataException;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** ユーザーサービス */
@Component
public class UserService {

  @Autowired private UserRepository userRepository;

  /** パスワードエンコーダー */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /** パスワードエンコーダー */
  @Autowired PasswordEncoder passwordEncoder;

  public User findByUsername(User user) throws NoDataException{
    return userRepository.findById(user);
  }

  public void insert(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // ユーザー登録
    userRepository.insert(user);
  }

  /** エンコード前後のパスワードが一致するかチェックする */
  public boolean passwordCheck(String password, String encodePassword) {
    return passwordEncoder.matches(password, encodePassword);
  }

  public String encode(String password) {
    return passwordEncoder.encode(password);
  }
}
