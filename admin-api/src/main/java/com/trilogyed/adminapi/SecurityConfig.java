package com.trilogyed.adminapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        authBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery(
                        "select username,authority from authorities where username =?")
                .passwordEncoder(passwordEncoder);
    }

    public void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.httpBasic();
        httpSecurity
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/product").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.GET,"/product/{productId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.GET,"/product/invoice/{invoiceId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.GET,"/product/inventory").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.PUT,"/inventory/{inventoryId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.PUT,"/product/{productId}").hasAnyAuthority("TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.POST,"/customer").hasAnyAuthority("TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.POST,"/product").hasAnyAuthority("MANAGER","ADMIN")
                .anyRequest().hasAuthority("ADMIN");
        httpSecurity
                .logout()
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//path for user to logout
                .logoutSuccessUrl("/logoutSuccess")//send to this page after logout
                .deleteCookies("JSESSIONID")
                .deleteCookies("XREF-TOKEN")
                .invalidateHttpSession(true);
        httpSecurity
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
