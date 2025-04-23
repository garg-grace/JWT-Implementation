package com.example.jwtdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
// for form based authentication
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http.authorizeHttpRequests(auth->auth
//                .requestMatchers("/user/register").permitAll()
//                .anyRequest().authenticated())
//                .headers(headers -> headers
//                        .frameOptions(frame -> frame.sameOrigin())  // Allow framing from same origin
//                )
//                .formLogin(Customizer.withDefaults())
//                .csrf(csrf->csrf.disable());
//
//        return http.build();
//    }

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JWTUtil jwtUtil,UserDetailsService userDetailsService){
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,JWTUtil jwtUtil) throws Exception{
        JWTAuthenticationFilter jwtAuthFilter = new JWTAuthenticationFilter(authenticationManager,jwtUtil);

        http.authorizeHttpRequests(auth->auth
                .requestMatchers("/user/register").permitAll()
                .anyRequest().authenticated())
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Arrays.asList(
                daoAuthenticationProvider()
        ));
    }



}
