package com.example.demo.security;

public class SecurityConstants {
  /** JWTのtoken作成用のパスワード鍵 */
  // public static final String SECRET = "hogehogefugafuga";
  public static final String SECRET = "special_secret_password_" + System.getenv("security_password");

  /** JWT tokenの有効期限 */
  public static final long EXPIRATION_TIME = 28_800_000; // 8hours
  // public static final long EXPIRATION_TIME = 28; // 8hours

  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
}
