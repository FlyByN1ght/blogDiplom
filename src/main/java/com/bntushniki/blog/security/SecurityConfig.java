package com.bntushniki.blog.security;

import com.bntushniki.blog.security.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomUserDetailService customUserDetailService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(new AntPathRequestMatcher("/registration", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/registration", "POST")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/registration/success", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/registration/error", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login", "GET")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/login", "POST")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/main", "GET")).hasAnyRole("Student", "Teacher", "Admin")
                                .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("loginOrEmail")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/main", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .userDetailsService(customUserDetailService)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
