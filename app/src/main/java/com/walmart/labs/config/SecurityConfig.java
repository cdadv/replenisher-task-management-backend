package com.walmart.labs.config;

import com.walmart.labs.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * TODO: Build whole security structure in the future. This class is for disabling spring boot 2
 * default security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  //  @Override
  //  protected void configure(HttpSecurity http) throws Exception {
  //    http.csrf().disable().authorizeRequests().anyRequest().permitAll();
  //  }

  @Autowired private ApplicationUserDetailsService applicationUserDetailsService;

  @SuppressWarnings("deprecation")
  @Bean
  public static NoOpPasswordEncoder passwordEncoder() {
    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(applicationUserDetailsService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }

  //    @Bean
  //    @Override
  //    protected ApplicationUserDetailsService applicationUserDetailsService(){
  //        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
  //
  // manager.createUser(User.withUsername("user_1").password("123456").authorities("USER").build());
  //
  // manager.createUser(User.withUsername("user_2").password("123456").authorities("USER").build());
  //        return manager;
  //    }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    AuthenticationManager manager = super.authenticationManagerBean();
    return manager;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.requestMatchers()
        .anyRequest()
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/*")
        .permitAll();
    // @formatter:on
  }
}
