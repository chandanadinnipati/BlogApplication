package com.blog.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityCofig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configure -> configure
                        .requestMatchers("/comment/update/**", "/comment/delete/*").hasRole("AUTHOR")
                        .requestMatchers("/createblog", "/create", "/update/**",
                                "/save", "/delete/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers("/login", "/register", "/save-login", "/", "/post/{id}", "/sort",
                                "/filter-search","/filter", "/search", "/comment/{id}", "/savecomment/{id}",
                                "/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());
        return http.build();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager theUserDetailmanager = new JdbcUserDetailsManager(dataSource);
        theUserDetailmanager.setUsersByUsernameQuery(
                "SELECT name, password, true FROM login WHERE name = ?");
        theUserDetailmanager.setAuthoritiesByUsernameQuery(
                "SELECT l.name, r.role FROM login l JOIN roles r ON l.id = r.id WHERE l.name=?");
        return theUserDetailmanager;
    }
}
