package com.dh.clinicaDental.dto;

import com.dh.clinicaDental.entity.Domicilio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PacienteDto {
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private String fechaDeAlta;
    private Domicilio domicilio;
}
