package com.dh.clinicaDental.repository;

import com.dh.clinicaDental.entity.Paciente;
import com.dh.clinicaDental.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
}
