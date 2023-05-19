package com.pentspace.accountmgtservice.security;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/pentspace","/account","/account**")
                .permitAll()
//                .antMatchers(HttpMethod.POST, "/user")
//                .permitAll()
          //      .antMatchers("/swagger-ui/","/swagger-ui","/api/**", "/swagger-ui.html", "/swagger-ui/**","/webjars/**", "/v2/**", "/swagger-resources/**").permitAll()
                .anyRequest().permitAll();
    }
}
