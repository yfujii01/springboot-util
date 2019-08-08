package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/** 認証・認可設定 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CORS
                .cors()
                // 認証設定
                .and().authorizeRequests()
                // 認証無しでアクセス許可するURL
                .antMatchers("/login", "/public").permitAll()
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
}
