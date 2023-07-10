package com.dh.clinicaDental.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TurnoDto {

    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private String nombrePaciente;
    private String apellidoPaciente;
    private String dniPaciente;
    private String nombreOdontologo;
    private String apellidoOdontologo;

    public TurnoDto(Integer id, LocalDate fecha, LocalTime hora, PacienteDto paciente, OdontologoDto odontologo) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.nombrePaciente = paciente.getNombre();
        this.apellidoPaciente = paciente.getApellido();
        this.dniPaciente = paciente.getDni();
        this.nombreOdontologo = odontologo.getNombre();
        this.apellidoOdontologo = odontologo.getApellido();
    }
}
