package com.dh.clinicaDental.repository;

import com.dh.clinicaDental.dto.OdontologoDto;
import com.dh.clinicaDental.entity.Odontologo;
import com.dh.clinicaDental.service.OdontologoService;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {

    @Query("SELECT o FROM Odontologo o WHERE o.nombre = :nombre AND o.apellido = :apellido")
    Odontologo findByNombreAndApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

}
