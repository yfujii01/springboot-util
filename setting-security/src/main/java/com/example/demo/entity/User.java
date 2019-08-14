package com.example.demo.entity;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
  /** ユーザー名 */
  @NotNull private String username;

  /** パスワード */
  @NotNull private String password;

  /** 有効状態 */
  @NotNull private Boolean enabled;

  /** (関連)権限 */
  private List<Authority> authorities;

  public User(String username) {
    this.username = username;
  }
}
