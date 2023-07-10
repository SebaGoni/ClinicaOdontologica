package com.dh.clinicaDental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaDeAlta;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "domicilio_id")
    private Domicilio domicilio;
}
