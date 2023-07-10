package com.dh.clinicaDental.repository;

import com.dh.clinicaDental.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    @Query("SELECT p FROM Paciente p WHERE p.nombre = :nombre AND p.apellido = :apellido AND p.dni = :dni")
    Paciente findByNombreApellidoDni(@Param("nombre") String nombre, @Param("apellido") String apellido, @Param("dni") String dni);
}
