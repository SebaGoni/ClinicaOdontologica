package com.dh.clinicaDental.login.service;

import com.dh.clinicaDental.login.entity.Usuario;
import com.dh.clinicaDental.login.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Transactional
@Service
public class UsuarioService implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Usuario user = usuarioRepository.findByUsername(username);

    UserDetails userDetails = new User(username, user.getPassword(),
        Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRol().name())));

    return userDetails;
  }
}
