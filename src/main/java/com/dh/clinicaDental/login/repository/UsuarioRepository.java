package com.dh.clinicaDental.login.repository;

import com.dh.clinicaDental.login.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  public Usuario findByUsername(String username);


}
