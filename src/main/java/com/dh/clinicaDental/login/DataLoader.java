package com.dh.clinicaDental.login;

import com.dh.clinicaDental.login.entity.Rol;
import com.dh.clinicaDental.login.entity.Usuario;
import com.dh.clinicaDental.login.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
  @Autowired
  UsuarioRepository repository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String passSeba = passwordEncoder.encode("seba");
    repository.save(new Usuario("Seba", "seba@gmail.com", "seba", passSeba, Rol.USER));
    String passAdmin = passwordEncoder.encode("admin");
    repository.save(new Usuario("Admin", "admin@gmail.com", "admin", passAdmin, Rol.ADMIN));

  }
}
