package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.Data;

/** 認証・認可設定 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  /** WebSecurityの設定 */
  @Override
  public void configure(WebSecurity web) throws Exception {
    // 静的リソース(images、css、javascript)とH2DBのコンソールに対するアクセスはセキュリティ設定を無視する
    web.ignoring().antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/h2-console/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // CORS
        .cors().configurationSource(corsConfigurationSource())
        // 認証設定
        .and().authorizeRequests()
        // 認証無しでアクセス許可するURL
        .antMatchers("/login", "/public").permitAll()
        // ADMINのみがアクセス可能
        .antMatchers("/adminonly", "/users/**").hasAuthority("ADMIN")
        // USERのみがアクセス可能
        .antMatchers("/useronly").hasAuthority("USER")
        // ADMINとUSERがアクセス可能
        .antMatchers("/userandadminonly").hasAnyAuthority("ADMIN", "USER")
        // その他のURLは全て認証を必要とさせる
        .anyRequest().authenticated()
        // CSRF
        .and().csrf().disable()
        // カスタム認証用のフィルターを追加
        .addFilter(new CustomAuthenticationFilter(authenticationManager()))
        // カスタム認可用のフィルターを追加
        .addFilter(new CustomAuthorizationFilter(authenticationManager()))
        // HttpSessionを使用しない
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  /** 認証プロバイダのコア機能を実装 */
  @Bean
  public AuthenticationProvider getAuthenticationProvider() {
    return new CustomUserDetailsAuthenticationProvider();
  }

  /**
   * CORS設定
   *
   * @return CORS設定
   */
  private CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
    corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);

    // この設定が無いとフロント側でAuthorizationにアクセスできない
    // http://hnak.hatenablog.jp/entry/2018/07/08/125216
    corsConfiguration.addExposedHeader("Authorization");

    // 環境変数からCORSを許可するURLを設定する
    // corsConfiguration.addAllowedOrigin(System.getenv("FRONT_URL"));
    
    // プロパティファイルからCORSを許可するURLを設定する
    param().getAllowedOrigins().forEach(p->{
      corsConfiguration.addAllowedOrigin(p);
    });

    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
    corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return corsConfigurationSource;
  }

  /** プロパティファイルから設定を読み込むクラス */
  @ConfigurationProperties(prefix = "custom.security.param")
  @Data
  private class SecurityPropertiesParameter {
    private List<String> allowedOrigins = new ArrayList<>();
  }

  /** プロパティファイル設定 */
  @Bean
  public SecurityPropertiesParameter param() {
    return new SecurityPropertiesParameter();
  }

}
