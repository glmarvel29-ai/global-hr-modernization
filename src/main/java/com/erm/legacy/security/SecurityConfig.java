package com.erm.legacy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ErmUserDetailsService userDetailsService;

    public SecurityConfig(ErmUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/api/health", "/login", "/error", "/webjars/**", "/static/**", "/css/**", "/js/**").permitAll()
                .antMatchers("/api/security/posture").hasAnyRole("ADMIN", "INTERNAL_AUDIT")
                .antMatchers("/api/integrations").hasAnyRole("ADMIN", "RISK_ANALYST", "INTERNAL_AUDIT")
                .antMatchers("/api/**").hasAnyRole("ADMIN", "RISK_ANALYST", "INTERNAL_AUDIT", "VENDOR_ANALYST")
                .antMatchers("/", "/dashboard", "/risks", "/integrations", "/security", "/legacy").authenticated()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", false)
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
                .httpBasic()
            .and()
                .logout()
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
            .and()
                .headers()
                .frameOptions().sameOrigin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
