package com.helpdeskturmaa.helpdesk.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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

/**
 * Classe de configuração de segurança da aplicação, utilizando Spring Security e
 * JSON Web Tokens (JWT) para autenticação e autorização.
 * * Configura o acesso a diferentes endpoints baseados em perfis de usuário
 * (Admin, Cliente, Técnico) e define a política de sessão como STATELESS.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Array de endpoints públicos que não exigem autenticação.
     */
    private static final String[] PUBLIC_MATCHES = { "/h2-console/**" , "/login/**" };

    /**
     * Array de endpoints públicos acessíveis apenas via método GET.
     * Atualmente vazio.
     */
    private static final String[] PUBLIC_MATCHES_GET = { }; 

    /**
     * Injeção do ambiente Spring para verificar o perfil ativo (ex: "test").
     */
    @Autowired
    private Environment env;

    /**
     * Injeção do utilitário JWT para manipulação de tokens.
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Serviço para carregar dados do usuário no contexto de segurança.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configura as regras de autorização de acesso HTTP e os filtros de segurança.
     * * 1. Habilita o console H2 em perfil de teste.
     * 2. Habilita CORS e desabilita CSRF.
     * 3. Define regras de autorização para os endpoints.
     * 4. Adiciona os filtros de Autenticação (Login) e Autorização (JWT).
     * 5. Define a política de sessão como STATELESS.
     *
     * @param http o objeto HttpSecurity para configuração.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        http.cors().and().csrf().disable();
        
        http.authorizeRequests()
            .antMatchers(PUBLIC_MATCHES).permitAll()

            // Apenas ADMIN pode criar, atualizar ou deletar clientes.
            .antMatchers(HttpMethod.POST, "/clientes/**").hasAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/clientes/**").hasAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/clientes/**").hasAuthority("ROLE_ADMIN")
            
            // Apenas ADMIN pode criar, atualizar ou deletar técnicos.
            .antMatchers(HttpMethod.POST, "/tecnicos/**").hasAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.PUT, "/tecnicos/**").hasAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.DELETE, "/tecnicos/**").hasAuthority("ROLE_ADMIN")

            // REGRAS PARA CHAMADOS
            .antMatchers(HttpMethod.POST, "/chamados").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TECNICO")
            .antMatchers(HttpMethod.GET, "/chamados/**").hasAnyAuthority("ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TECNICO")
            .antMatchers(HttpMethod.PUT, "/chamados/**").hasAnyAuthority("ROLE_TECNICO", "ROLE_ADMIN") 
            .antMatchers(HttpMethod.DELETE, "/chamados/**").hasAuthority("ROLE_ADMIN") 

            .anyRequest().authenticated();

        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Configura o provedor de autenticação, definindo o UserDetailsService
     * e o codificador de senha a serem utilizados.
     *
     * @param auth o objeto AuthenticationManagerBuilder para configuração.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * Define a configuração de CORS (Cross-Origin Resource Sharing), permitindo
     * requisições de diferentes origens e métodos HTTP (POST, GET, PUT, DELETE, OPTIONS).
     *
     * @return a fonte de configuração CORS para ser usada pelo Spring Security.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Cria e expõe o bean BCryptPasswordEncoder para codificação de senhas.
     *
     * @return uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}