package com.trilogyed.adminapi;

import org.springframework.beans.factory.annotation.Autowired;
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
                .mvcMatchers(HttpMethod.POST, "/admin/**").hasAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/admin/**").hasAuthority("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/admin/**").hasAuthority("ADMIN")
                .mvcMatchers(HttpMethod.DELETE,"/admin/**").hasAuthority("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/admin/product").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.GET,"/admin/product/{productId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.GET,"/admin/product/invoice/{invoiceId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.GET,"/admin/product/{inventoryId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.PUT,"/admin/inventory/{inventoryId}").hasAnyAuthority("EMPLOYEE","TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.PUT,"/admin/product/{productId}").hasAnyAuthority("TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.POST,"/admin/customer").hasAnyAuthority("TEAM LEAD","MANAGER","ADMIN")
                .mvcMatchers(HttpMethod.POST,"/admin/product").hasAnyAuthority("MANAGER","ADMIN")
                .anyRequest().permitAll();
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
