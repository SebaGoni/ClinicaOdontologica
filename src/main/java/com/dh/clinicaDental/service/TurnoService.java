package com.dh.clinicaDental.service;

import com.dh.clinicaDental.dto.TurnoDto;
import com.dh.clinicaDental.entity.Odontologo;
import com.dh.clinicaDental.entity.Paciente;
import com.dh.clinicaDental.entity.Turno;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.repository.OdontologoRepository;
import com.dh.clinicaDental.repository.PacienteRepository;
import com.dh.clinicaDental.repository.TurnoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TurnoService {

    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final TurnoRepository turnoRepository;
    private final ObjectMapper mapper;
    public TurnoService(PacienteRepository pacienteRepository, OdontologoRepository odontologoRepository, TurnoRepository turnoRepository, ObjectMapper mapper) {
        this.pacienteRepository = pacienteRepository;
        this.odontologoRepository = odontologoRepository;
        this.turnoRepository = turnoRepository;
        this.mapper = mapper;
    }

    //Funcion guardarTurno() la vamos a usar en agregar y modificar.

    private Turno guardarTurno(Turno turno) throws BadRequestException {
         if (turno.getFecha() == null) {
            throw new BadRequestException("1035", "La fecha del turno es obligatoria");
        }if (turno.getHora() == null) {
            throw new BadRequestException("1036", "La hora del turno es obligatoria");
        }if (turno.getPaciente().getId() == null) {
            throw new BadRequestException("1037", "El ID del paciente es obligatorio");
        }if (turno.getOdontologo().getId() == null) {
            throw new BadRequestException("1038", "El ID del odontólogo es obligatorio");
        }if (turno.getFecha().isBefore(LocalDate.now())) {
            throw new BadRequestException("1039", "La fecha del turno no puede ser anterior a la fecha actual");
        }if (turno.getFecha().getDayOfWeek() == DayOfWeek.SATURDAY || turno.getFecha().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BadRequestException("1040", "No se puede sacar un turno para el fin de semana");
        }if (turno.getFecha().isEqual(LocalDate.now())) {
            throw new BadRequestException("1041", "No se puede sacar un turno para el mismo dia");
        } if (turno.getHora().isBefore(LocalTime.of(8, 0)) || turno.getHora().isAfter(LocalTime.of(17, 0))) {
            throw new BadRequestException("1042", "Solo se pueden sacar turnos entre las 8am y las 17pm");
        }
        return turnoRepository.save(turno);
    }


    public TurnoDto agregar(Turno turno) throws ResourceNotFoundException, BadRequestException {
        if (turno.getId() != null) {
            throw new BadRequestException("1008", "No puede incluir el ID. Se genera automáticamente");
        }

        Optional<Paciente> pacienteExistente = pacienteRepository.findById(turno.getPaciente().getId());
        if (pacienteExistente.isEmpty()) {
            throw new ResourceNotFoundException("1043", "No se encontró un paciente con el ID: " + turno.getPaciente().getId());
        }

        Optional<Odontologo> odontologoExistente = odontologoRepository.findById(turno.getOdontologo().getId());
        if (odontologoExistente.isEmpty()) {
            throw new ResourceNotFoundException("1044", "No se encontró un odontólogo con el ID: " + turno.getOdontologo().getId());
        }
        List<Turno> listaTurnos = turnoRepository.findAll();

        for (Turno turnoExistente : listaTurnos) {
            if (turnoExistente.getOdontologo().getId().equals(turno.getOdontologo().getId()) &&
                    turnoExistente.getFecha().equals(turno.getFecha()) &&
                    turnoExistente.getHora().equals(turno.getHora())) {
                throw new BadRequestException("1045", "Ya existe un turno asignado a este odontólogo el mismo día a la misma hora");
            }
            if (turnoExistente.getPaciente().getId().equals(turno.getPaciente().getId()) &&
                    turnoExistente.getFecha().equals(turno.getFecha())){
                throw new BadRequestException("1046", "Ya existe un turno asignado para el " + turno.getFecha() +
                        ". El paciente no puede sacar dos turnos para el mismo dia");
            }
        }

        Turno nuevoTurno = guardarTurno(turno);
        return mapper.convertValue(nuevoTurno, TurnoDto.class);
    }


    public TurnoDto obtener(Integer id) throws ResourceNotFoundException {
        Optional<Turno> turno = turnoRepository.findById(id);
        if (turno.isPresent()) {
            return mapper.convertValue(turno.get(), TurnoDto.class);
        } else {
            throw new ResourceNotFoundException("1047", "No se encontro turno con id " + id);
        }
    }

    public void eliminar(Integer id) throws ResourceNotFoundException {
        Optional<Turno> turno = turnoRepository.findById(id);
        if (turno.isPresent()) {
            turnoRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("1047", "No se encontro turno con id " + id);
        }
    }


    public TurnoDto modificarTurno(Turno turno) throws ResourceNotFoundException, BadRequestException {
        if (turno.getId() == null) {
            throw new BadRequestException("1048", "El ID del turno es obligatorio");
        }

        Optional<Turno> turnoExistente = turnoRepository.findById(turno.getId());
        if (turnoExistente.isEmpty()) {
            throw new ResourceNotFoundException("1047", "No se encontro turno con id " + turno.getId());
        }
    //Esta que sigue parece la misma regla de negocio que en agregar() pero no lo es porque al ser un PUT
    //primero tiene que comparar si es el mismo ID para no lanzar la excepcion.

        List<Turno> listaTurnos = turnoRepository.findAll();
        for (Turno turnosExistentes : listaTurnos) {
            if (!turnosExistentes.getId().equals(turno.getId())) {
                if (turnosExistentes.getHora().equals(turno.getHora()) &&
                        turnosExistentes.getFecha().equals(turno.getFecha()) &&
                        turnosExistentes.getOdontologo().getId().equals(turno.getOdontologo().getId())) {
                    throw new BadRequestException("1045", "Ya existe un turno asignado a este odontólogo el mismo día a la misma hora");
                }
                if (turnosExistentes.getFecha().equals(turno.getFecha())) {
                throw new BadRequestException("1046", "Ya existe un turno asignado para el " + turno.getFecha() +
                        ". El paciente no puede sacar dos turnos para el mismo dia");
            }
        }
        }

        Optional<Paciente> pacienteExistente = pacienteRepository.findById(turno.getPaciente().getId());
        if (pacienteExistente.isEmpty()) {
            throw new ResourceNotFoundException("1029", "Paciente no encontrado");
        }

        Optional<Odontologo> odontologoExistente = odontologoRepository.findById(turno.getOdontologo().getId());
        if (odontologoExistente.isEmpty()) {
            throw new ResourceNotFoundException("1011", "Odontologo no encontrado");
        }

        Turno nuevoTurno = guardarTurno(turno);
        return mapper.convertValue(nuevoTurno, TurnoDto.class);
    }


    public List<TurnoDto> listar() {
        List<Turno> listaTurnos = turnoRepository.findAll();
        List<TurnoDto> listaTurnosDto = listaTurnos
                .stream()
                .map(turno -> mapper.convertValue(turno, TurnoDto.class))
                .collect(Collectors.toList());
        return listaTurnosDto;
    }
}
