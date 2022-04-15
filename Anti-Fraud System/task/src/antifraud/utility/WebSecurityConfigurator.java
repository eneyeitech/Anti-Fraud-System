package antifraud.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userinfoService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userinfoService) // user store 1
                .passwordEncoder(getEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(getEntryPoint()) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers("/api/antifraud/transaction/**").hasRole(Role.MERCHANT.name())
                .antMatchers(HttpMethod.POST,"/api/auth/user/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole(Role.ADMINISTRATOR.name())
                .antMatchers("/api/antifraud/access/**", "/api/auth/access/**", "/api/auth/role/**").hasRole(Role.ADMINISTRATOR.name())
                .antMatchers("/api/auth/list/**").hasAnyRole(Role.ADMINISTRATOR.name(), Role.SUPPORT.name())
                .antMatchers("/actuator/shutdown").permitAll() // needs to run test
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint getEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
}
