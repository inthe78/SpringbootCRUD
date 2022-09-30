package com.inthe.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.inthe.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan(value= {"com.inthe.services"})
public class WebSecurityConfig {
	
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
        .antMatchers("/images/**","/js/**","/css/**").permitAll()
        .antMatchers("/registration","/register","/login","/error").permitAll()
        .antMatchers("/add_employee","/delete_employee","/save").hasAnyAuthority("admin")
        .antMatchers("/","/edit_employee").hasAnyAuthority("employee","admin")
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login")
        .and().logout().invalidateHttpSession(true)
        .clearAuthentication(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login");
        
        http.csrf().disable();
        
        return http.build();
    }

}
