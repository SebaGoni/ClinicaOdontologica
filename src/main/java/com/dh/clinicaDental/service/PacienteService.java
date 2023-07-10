package com.dh.clinicaDental.service;

import com.dh.clinicaDental.dto.PacienteDto;
import com.dh.clinicaDental.entity.Domicilio;
import com.dh.clinicaDental.entity.Paciente;
import com.dh.clinicaDental.entity.Turno;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.repository.DomicilioRepository;
import com.dh.clinicaDental.repository.PacienteRepository;
import com.dh.clinicaDental.repository.TurnoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ObjectMapper mapper;
    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private DomicilioRepository domicilioRepository;
    public PacienteService(PacienteRepository pacienteRepository, ObjectMapper mapper) {
        this.pacienteRepository = pacienteRepository;
        this.mapper = mapper;
    }

    //funcion guardarPaciente() la vamos a usar en agregar y modificar.
    private Paciente guardarPaciente(Paciente paciente) throws BadRequestException {
        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()) {
            throw new BadRequestException("1002", "El nombre es obligatorio");
        }
        if (paciente.getNombre().length() < 3 || paciente.getNombre().length() > 50) {
            throw new BadRequestException("1014", "El nombre debe tener entre 3 y 50 letras");
        }
        if (!Pattern.compile("^[a-zA-ZñÑ]+$").matcher(paciente.getNombre()).matches()) {
            throw new BadRequestException("1015", "El nombre no puede contener números ni caracteres especiales");
        }
        if (paciente.getApellido() == null || paciente.getApellido().isEmpty()) {
            throw new BadRequestException("1005", "El apellido es obligatorio");
        }
        if (paciente.getApellido().length() < 3 || paciente.getApellido().length() > 50) {
            throw new BadRequestException("1016", "El apellido debe tener entre 3 y 50 letras");
        }
        if (!Pattern.compile("^[a-zA-ZñÑ]+$").matcher(paciente.getApellido()).matches()) {
            throw new BadRequestException("1017", "El apellido no puede contener números ni caracteres especiales");
        }
        if (paciente.getDni() == null || paciente.getDni().isEmpty()) {
            throw new BadRequestException("1018", "El DNI es obligatorio");
        }
        if (!paciente.getDni().matches("[0-9]+")) {
            throw new BadRequestException("1019", "El campo dni debe contener solo números");
        }
        if (paciente.getDni().length() != 8) {
            throw new BadRequestException("1020", "El campo dni debe contener 8 cifras");
        }
        if (Integer.parseInt(paciente.getDni()) < 0) {
            throw new BadRequestException("1021", "El campo dni no puede ser negativo");
        }
        if (paciente.getDomicilio().getCalle() == null || paciente.getDomicilio().getCalle().isEmpty()) {
            throw new BadRequestException("1022", "La calle es obligatoria");
        }
        if (paciente.getDomicilio().getNro() == null || paciente.getDomicilio().getNro().isEmpty()) {
            throw new BadRequestException("1023", "El nro de calle es obligatorio");
        }
        if (paciente.getDomicilio().getLocalidad() == null || paciente.getDomicilio().getLocalidad().isEmpty()) {
            throw new BadRequestException("1024", "La localidad es obligatoria");
        }
        if (paciente.getDomicilio().getProvincia() == null || paciente.getDomicilio().getProvincia().isEmpty()) {
            throw new BadRequestException("1025", "La provincia es obligatoria");
        }
        if (paciente.getFechaDeAlta() == null) {
            throw new BadRequestException("1026", "La fecha de alta es obligatoria");
        }
        if ((paciente.getFechaDeAlta().isBefore(LocalDate.now()))) {
            throw new BadRequestException("1027", "La fecha de alta debe ser igual o posterior a la fecha actual");
        }

        return pacienteRepository.save(paciente);
    }
    //----------------------------------------------------------------------------------------------------

    public PacienteDto agregar(Paciente paciente) throws BadRequestException {
        if (paciente.getId() != null) {
            throw new BadRequestException("1008", "No puede incluir el id. Se genera automaticamente");
        }
        List<Paciente> listaPacientes = pacienteRepository.findAll();
        for (Paciente pacienteExistente : listaPacientes) {
            if (Objects.equals(pacienteExistente.getDni(), paciente.getDni())) {
                throw new BadRequestException("1028", "Ya existe un paciente con el mismo dni");
            }
        }
        Paciente nuevoPaciente = guardarPaciente(paciente);
        return mapper.convertValue(nuevoPaciente, PacienteDto.class);
    }

    public PacienteDto obtener(Integer id) throws ResourceNotFoundException, BadRequestException {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (paciente.isPresent()) {
            return mapper.convertValue(paciente.get(), PacienteDto.class);
        } else {
            throw new ResourceNotFoundException("1029", "Paciente no encontrado");
        }
    }


    public void eliminar(Integer id) throws ResourceNotFoundException, BadRequestException {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (paciente.isPresent()) {
            List<Turno> listaTurnos = turnoRepository.findAll();
            for (Turno turnoExistente : listaTurnos) {
                if (turnoExistente.getPaciente().getId().equals(paciente.get().getId())) {
                    throw new BadRequestException("1030", "No se puede eliminar un paciente con turno asignado");
                }
            }
            pacienteRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("1029", "Paciente no encontrado");
        }
    }

    public PacienteDto modificar(Paciente paciente) throws BadRequestException, ResourceNotFoundException {
        if (paciente.getId() == null) {
            throw new BadRequestException("1031", "El ID del paciente es obligatorio");
        }
        if (paciente.getDomicilio().getId() == null) {
            throw new BadRequestException("1032", "El ID del domicilio es obligatorio");
        }
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(paciente.getId());

        if (pacienteExistente.isEmpty()) {
            throw new ResourceNotFoundException("1029", "Paciente no encontrado");
        }
        Optional<Domicilio> domicilioExistente = domicilioRepository.findById(paciente.getDomicilio().getId());

        if (domicilioExistente.isEmpty()) {
            throw new ResourceNotFoundException("1033", "Domicilio no encontrado");
        }

        boolean idExistente = false;
        List<Paciente> listaPacientes = pacienteRepository.findAll();

        for (Paciente pacienteActual : listaPacientes) {
            if (pacienteActual.getId().equals(paciente.getId())) {
                idExistente = true;
                break;
            }
        }

        if (idExistente) {
            for (Paciente pacienteActual : listaPacientes) {
                if (!pacienteActual.getId().equals(paciente.getId()) && pacienteActual.getDni().equals(paciente.getDni())) {
                    throw new BadRequestException("1034", "Ya existe un paciente con el mismo DNI");
                }
            }
        } else {
            for (Paciente pacienteActual : listaPacientes) {
                if (pacienteActual.getDni().equals(paciente.getDni())) {
                    throw new BadRequestException("1034", "Ya existe un paciente con el mismo DNI");
                }
            }
        }

        Paciente nuevoPaciente = guardarPaciente(paciente);
        return mapper.convertValue(nuevoPaciente, PacienteDto.class);
    }


    public List<PacienteDto> listar() {
        List<Paciente> listaPacientes = pacienteRepository.findAll();
        return listaPacientes
                .stream()
                .map(paciente -> mapper.convertValue(paciente, PacienteDto.class))
                .collect(Collectors.toList());
    }

    public PacienteDto findByNombreApellidoDni(String nombre, String apellido, String dni) throws ResourceNotFoundException {
        Paciente pacienteEncontrado = pacienteRepository.findByNombreApellidoDni(nombre, apellido, dni);
        if (pacienteEncontrado != null) {
            return mapper.convertValue(pacienteEncontrado, PacienteDto.class);
        } else {
            throw new ResourceNotFoundException("1029", "Paciente no encontrado");
        }
    }

}
