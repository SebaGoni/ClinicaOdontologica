package com.dh.clinicaDental.login.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String nombre;
  private String email;
  private String username;
  private String password;
  @Enumerated
  private Rol rol;

  public Usuario(String nombre, String email, String username, String password, Rol rol) {
    this.nombre = nombre;
    this.email = email;
    this.username = username;
    this.password = password;
    this.rol = rol;
  }
}
