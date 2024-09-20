package jp.te4a.spring.boot.teamc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jp.te4a.spring.boot.teamc.service.LoginUserDetailsService;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private  LoginUserDetailsService  LoginUserDetailsService; 

    @Autowired
    public void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(LoginUserDetailsService).passwordEncoder(passwordEncoder());
    }

    // パスワード暗号化
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 全ユーザアクセス可能パス
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/webjars/**", "/css/**").permitAll()
                .requestMatchers("/loginForm").permitAll()
                .requestMatchers("/users").permitAll()
                //.requestMatchers("/users/create").permitAll()
                //.requestMatchers("/admin/**").hasRole("ADMIN") // ADMINロールのみアクセス可
                //.requestMatchers("/user/**").hasRole("USER") // USERロールのみアクセス可
                //toolsの権限を付与して
                .requestMatchers("/tools").permitAll()
                .requestMatchers("/tools/search").permitAll()
                .requestMatchers("/tools/create").permitAll()
                .requestMatchers("/tools/delete").permitAll()
                .requestMatchers("/tools/edit").permitAll()
                .requestMatchers("/tools/search/search").permitAll()
                .requestMatchers("/tools/create/create").permitAll()
                .requestMatchers("/tools/delete/delete").permitAll()
                .requestMatchers("/tools/edit/edit").permitAll()
                //.requestMatchers(HttpMethod.POST, "/tools/create").hasRole("ADMIN") // POSTメソッドの/tools/createに対してADMINロールを要求
                //.requestMatchers(HttpMethod.POST, "/tools/delete").hasRole("ADMIN") // POSTメソッドの/tools/deleteに対してADMINロールを要求
                //.requestMatchers(HttpMethod.POST, "/tools/edit").hasRole("ADMIN") // POSTメソッドの/tools/editに対してADMINロールを要求
                .anyRequest().authenticated() // 上記以外は認証が必要
            )
            .formLogin((login) -> login
                // ログイン処理URL
                .loginProcessingUrl("/login")
                // ログインページURL
                .loginPage("/loginForm")
                // ログイン失敗時URL
                .failureUrl("/loginForm?error")
                // ログイン成功時URL
                .defaultSuccessUrl("/tools", true)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            // ログアウト時
            .logout((logout) -> logout
                .logoutSuccessUrl("/loginForm")
            );

        return http.build();
    }
}
