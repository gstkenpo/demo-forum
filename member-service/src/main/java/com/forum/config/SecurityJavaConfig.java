package com.forum.config;

import com.forum.security.filter.JWTAuthenticationFilter;
import com.forum.security.filter.JWTAuthorizationFilter;
import com.forum.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
	//private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private MemberService memberService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//    	return NoOpPasswordEncoder.getInstance();
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception { 
    	http.csrf().disable()
    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    	.authorizeRequests()
    	.antMatchers(HttpMethod.POST, "/rest/member").permitAll()
    	// .antMatchers("/admin/**").hasAnyAuthority("Admin")
    	// .antMatchers("/auth").permitAll()
    	.antMatchers("/v2/api-docs/**", "/configuration/ui/**", "/swagger-resources/**", "/configuration/security/**", "/swagger-ui.html", "/webjars/**").permitAll()
		.anyRequest().authenticated()
        .and()
        .addFilter(new JWTAuthenticationFilter(authenticationManager()))
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), memberService))
        //.addFilter(jWTAuthorizationFilter)
		// .headers()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    // @Bean
    // public SimpleUrlAuthenticationFailureHandler myFailureHandler(){
    //     return new SimpleUrlAuthenticationFailureHandler();
    // }
}