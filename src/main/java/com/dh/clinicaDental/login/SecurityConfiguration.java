package com.dh.clinicaDental.login;

import com.dh.clinicaDental.login.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UsuarioService usuarioService;


  public void configure(AuthenticationManagerBuilder auth) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(bCryptPasswordEncoder);
    provider.setUserDetailsService(usuarioService);
    auth.authenticationProvider(provider);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            .requestMatchers(HttpMethod.GET, "/pacientes", "/odontologos", "/turnos").permitAll()
            .requestMatchers(HttpMethod.POST, "/pacientes", "/odontologos").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/pacientes", "/odontologos").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/pacientes/**", "/odontologos/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/turnos").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/turnos").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/turnos/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/turnos.html", "/turnoList.html", "/turnoAlta.html").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/odontologos.html", "/odontologoAlta.html", "/odontologoList.html").hasRole("ADMIN")
            .requestMatchers("/pacientes.html", "/pacienteAlta.html", "/pacienteList.html").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .and()
            .logout()
            .and()
            .httpBasic();

    http.httpBasic();
    return http.build();
  }


}
