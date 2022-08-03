package com.paymentsystem.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired private CustomeSuccessHandler successHandler;
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServicesImp();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/student").permitAll()
                .antMatchers("/student/register").permitAll()
                .antMatchers("/home","/").permitAll()
                .antMatchers("/accountant").hasAnyAuthority("ADMIN","ACCOUNTANT")
                .antMatchers("/accountant/**").hasAnyAuthority("ADMIN","ACCOUNTANT")
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .loginPage("/login")
                .usernameParameter("email")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .and()
                .logout().permitAll()
                .logoutUrl("/**/logout")
                .and().csrf().disable()
                .exceptionHandling().accessDeniedPage("/403");

    }
}
