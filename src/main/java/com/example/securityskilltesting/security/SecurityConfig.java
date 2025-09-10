package com.example.securityskilltesting.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor

public class SecurityConfig {

    private  JwtAuthEntryPoint authEntryPoint;

    //@Bean
//    public  AuthenticationSuccessHandler authenticationSuccessHandler(){
//        return new authenticationSucessHandler();
// }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws  Exception{
        http
                .cors(cors->{})
                .csrf(csrf->csrf.disable())
                .exceptionHandling(exception->exception
                                .authenticationEntryPoint(authEntryPoint)
                        )
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/product/getAll").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/product/upload").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/product/update/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/product/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/cart/**").hasAuthority("USER")
                        .anyRequest().authenticated())
               // .formLogin(form->form
                       // .loginPage("/api/auth/login")
                        //.successHandler(authenticationSuccessHandler())
                      //  .permitAll())
                .addFilterBefore(jwtAuthentificationFilter(), UsernamePasswordAuthenticationFilter.class);

     return http.build();

    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return   authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthentificationFilter jwtAuthentificationFilter(){
        return new JWTAuthentificationFilter();
    }




}
