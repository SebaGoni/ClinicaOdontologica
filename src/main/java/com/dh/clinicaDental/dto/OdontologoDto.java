package com.dh.clinicaDental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OdontologoDto {
  private Integer id;
  private String nombre;
  private String apellido;
  private Integer matricula;

}
