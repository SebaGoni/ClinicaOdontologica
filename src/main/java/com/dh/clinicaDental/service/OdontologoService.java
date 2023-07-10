package com.dh.clinicaDental.service;

import com.dh.clinicaDental.dto.OdontologoDto;
import com.dh.clinicaDental.entity.Odontologo;
import com.dh.clinicaDental.entity.Turno;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.repository.OdontologoRepository;
import com.dh.clinicaDental.repository.TurnoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OdontologoService {
  @Autowired
  private OdontologoRepository odontologoRepository;
  @Autowired
  ObjectMapper mapper;
  @Autowired
  private TurnoRepository turnoRepository;

  public OdontologoService(OdontologoRepository odontologoRepository, TurnoRepository turnoRepository) {
    this.odontologoRepository = odontologoRepository;
    this.turnoRepository = turnoRepository;
    this.mapper = new ObjectMapper();
  }

  //Funcion guardarOdontologo() la vamos a usar en agregar y modificar.
  private Odontologo guardarOdontologo(Odontologo odontologo) throws BadRequestException {
    if (odontologo.getMatricula() < 0 || odontologo.getMatricula() < 1) {
      throw new BadRequestException("1001", "La matrícula no puede ser 0 o negativa");
    }
    if (odontologo.getNombre() == null || odontologo.getNombre().isEmpty()) {
      throw new BadRequestException("1002", "El nombre es obligatorio");
    }
    if (odontologo.getNombre().length() < 3 || odontologo.getNombre().length() > 50) {
      throw new BadRequestException("1003", "El nombre debe tener entre 3 y 50 letras");
    }
    if (!Pattern.compile("^[a-zA-ZñÑ]+$").matcher(odontologo.getNombre()).matches()) {
      throw new BadRequestException("1004", "El nombre no puede contener números ni caracteres especiales");
    }
    if (odontologo.getApellido() == null || odontologo.getApellido().isEmpty()) {
      throw new BadRequestException("1005", "El apellido es obligatorio");
    }
    if (odontologo.getApellido().length() < 3 || odontologo.getApellido().length() > 50) {
      throw new BadRequestException("1006", "El apellido debe tener entre 3 y 50 letras");
    }
    if (!Pattern.compile("^[a-zA-ZñÑ]+$").matcher(odontologo.getApellido()).matches()) {
      throw new BadRequestException("1007", "El apellido no puede contener números ni caracteres especiales");
    }
    return odontologoRepository.save(odontologo);
  }
//----------------------------------------------------------------------------------------------------

  public OdontologoDto agregar(Odontologo odontologo) throws BadRequestException {
    if (odontologo.getId() != null) {
      throw new BadRequestException("1008", "No puede incluir el id. Se genera automaticamente");
    }
    if (odontologo.getMatricula() == null) {
      throw new BadRequestException("1009", "La matricula es obligatoria");
    }
    List<Odontologo> listaOdontologos = odontologoRepository.findAll();
    for (Odontologo odontologoExistente : listaOdontologos) {
      if (odontologoExistente.getMatricula().equals(odontologo.getMatricula())) {
        throw new BadRequestException("1010", "Ya existe un odontólogo con la misma matrícula");
      }
    }
    Odontologo nuevoOdontologo = guardarOdontologo(odontologo);
    return mapper.convertValue(nuevoOdontologo, OdontologoDto.class);
  }

  public OdontologoDto obtener(Integer id) throws ResourceNotFoundException, BadRequestException {
    Optional<Odontologo> odontologo = odontologoRepository.findById(id);
    if (odontologo.isPresent()) {
      return mapper.convertValue(odontologo.get(), OdontologoDto.class);
    } else {
      throw new ResourceNotFoundException("1011", "Odontologo no encontrado");
    }
  }

  public void eliminar(Integer id) throws ResourceNotFoundException, BadRequestException {
    Optional<Odontologo> odontologo = odontologoRepository.findById(id);
    if (odontologo.isPresent()) {
      List<Turno> listaTurnos = turnoRepository.findAll();
      for (Turno turnoExistente : listaTurnos) {
        if (turnoExistente.getOdontologo().getId().equals(odontologo.get().getId())) {
          throw new BadRequestException("1012", "No se puede eliminar un odontologo con turno asignado");
        }
      }
      odontologoRepository.deleteById(id);
    } else {
      throw new ResourceNotFoundException("1011", "Odontologo no encontrado");
    }
  }


  public OdontologoDto modificar(Odontologo odontologo) throws BadRequestException, ResourceNotFoundException {
    if (odontologo.getId() == null) {
      throw new BadRequestException("1013", "El ID es obligatorio");
    }
    Optional<Odontologo> odontologoExistente = odontologoRepository.findById(odontologo.getId());

    if (odontologoExistente.isEmpty()) {
      throw new ResourceNotFoundException("1011", "Odontólogo no encontrado");
    }
    List<Odontologo> listaOdontologos = odontologoRepository.findAll();

    for (Odontologo odontologoActual : listaOdontologos) {
      if (odontologoActual.getId().equals(odontologo.getId())) {
        continue;
      }

      if (odontologoActual.getMatricula().equals(odontologo.getMatricula())) {
        throw new BadRequestException("1010", "Ya existe un odontólogo con la misma matrícula");
      }
    }
    Odontologo nuevoOdontologo = guardarOdontologo(odontologo);
    return mapper.convertValue(nuevoOdontologo, OdontologoDto.class);
  }


  public List<OdontologoDto> listar() {
    List<Odontologo> listaOdontologos = odontologoRepository.findAll();
    return listaOdontologos
            .stream()
            .map(odontologo -> mapper.convertValue(odontologo, OdontologoDto.class))
            .collect(Collectors.toList());
  }

  public OdontologoDto findByNombreAndApellido(String nombre, String apellido) throws ResourceNotFoundException {
    Odontologo odontologoEncontrado = odontologoRepository.findByNombreAndApellido(nombre, apellido);
    if (odontologoEncontrado != null) {
      return mapper.convertValue(odontologoEncontrado, OdontologoDto.class);
    } else {
      throw new ResourceNotFoundException("1011", "Odontologo no encontrado");
    }
  }

}
