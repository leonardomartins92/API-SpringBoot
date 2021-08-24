package com.voluptaria.vlpt.config;

import com.voluptaria.vlpt.model.Usuario;
import com.voluptaria.vlpt.security.JwtAuthFilter;
import com.voluptaria.vlpt.security.JwtService;
import com.voluptaria.vlpt.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UsuarioService usuarioService;

        @Autowired
        private JwtService jwtService;


        @Bean
        public OncePerRequestFilter jwtFilter(){
                return new JwtAuthFilter(jwtService, usuarioService);
        }

        @Bean
        public PasswordEncoder getPasswordEncoder() {return new BCryptPasswordEncoder();}

        private static final String[] USER = {
                "/", "/api/v1/clientes/**", "/api/v1/destinos/**", "/api/v1/funcionarios/**"
        };

        private static final String[] ADMIN = {
                "/api/v1/empresas/**", "/api/v1/pacotes/**", "/api/v1/passagens/**"
        };

        @Override
        protected void configure(HttpSecurity http) throws Exception {
           http
               .csrf().disable()
               .authorizeRequests().antMatchers(USER).hasAnyRole("ADMIN", "USER")
               .antMatchers(ADMIN).hasRole("ADMIN")
               .antMatchers(HttpMethod.POST, "/api/v1/usuarios/**").permitAll()
               .anyRequest().authenticated()
               .and()
               .sessionManagement()
                   .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
                   .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(usuarioService)
            .passwordEncoder(getPasswordEncoder());
        }

        @Override
        public void configure(WebSecurity webSecurity) throws Exception{
                webSecurity.ignoring().antMatchers(
                        "/v2/api-docs","/configuration/ui","/swagger-resources/**",
                        "/configuration/security", "/swagger-ui.html", "/webjars/**");
        }
}
