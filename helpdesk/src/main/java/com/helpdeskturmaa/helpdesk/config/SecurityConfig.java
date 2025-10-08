package com.helpdeskturmaa.helpdesk.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.helpdeskturmaa.helpdesk.security.JWTAuthenticationFilter;
import com.helpdeskturmaa.helpdesk.security.JWTUtil;
import com.helpdeskturmaa.helpdesk.security.JWTAuthorizationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_MATCHES = { "/h2-console/**" , "/login/**" };
    private static final String[] PUBLIC_MATCHES_GET = { }; 

    @Autowired
    private Environment env;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.cors().and().csrf().disable();
        
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

        http.authorizeRequests()
        // Libera todos os endpoints públicos
        .antMatchers(PUBLIC_MATCHES).permitAll()
        
        // CLIENTE pode CRIAR (POST) chamados
        // Se a sua role for ROLE_CLIENTE, use hasAuthority.
        .antMatchers(HttpMethod.POST, "/chamados").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TECNICO")
        
        // CLIENTE pode VER (GET) seus chamados.
        // O Cliente e o Técnico devem conseguir ver chamados.
        .antMatchers(HttpMethod.GET, "/chamados/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TECNICO")
        
        // TECNICO e ADMIN podem ATUALIZAR (PUT) e DELETAR (DELETE)
        .antMatchers(HttpMethod.PUT, "/chamados/**").hasAnyAuthority("ROLE_TECNICO", "ROLE_ADMIN") 
        .antMatchers(HttpMethod.DELETE, "/chamados/**").hasAuthority("ROLE_ADMIN") 
        .anyRequest().authenticated();

    http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
    http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}