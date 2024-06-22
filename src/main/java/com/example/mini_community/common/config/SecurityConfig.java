package com.example.mini_community.common.config;

import com.example.mini_community.service.member.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) ->
            web.ignoring()
                    .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/auth/join"),
                                new AntPathRequestMatcher("/auth/login"),
                                new AntPathRequestMatcher("/auth/token"),
                                new AntPathRequestMatcher("/user"),
                                new AntPathRequestMatcher("/boards"),
                                new AntPathRequestMatcher("/boards/*"),
                                new AntPathRequestMatcher("/boards/*/like"),
                                new AntPathRequestMatcher("/boards/*/comments"),
                                new AntPathRequestMatcher("/boards/*/comments/*"),
                                new AntPathRequestMatcher("/members/*")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailService userDetailsService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
