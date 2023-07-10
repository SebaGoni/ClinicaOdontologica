package com.dh.clinicaDental.controller;

import com.dh.clinicaDental.dto.PacienteDto;
import com.dh.clinicaDental.entity.Paciente;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
    @RestController
    @RequestMapping("/pacientes")
    public class PacienteController {

        @Autowired
        private PacienteService service;

        @GetMapping
        public List<PacienteDto> listar() {
            return service.listar();
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> buscarPorId(@PathVariable("id") Integer id) throws ResourceNotFoundException, BadRequestException {
            PacienteDto pacienteDto = service.obtener(id);
            return ResponseEntity.status(HttpStatus.OK).body(pacienteDto);
        }

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> agregar(@RequestBody Paciente paciente) throws BadRequestException {
            return ResponseEntity.ok(service.agregar(paciente));
        }

        @PutMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> modificar(@RequestBody Paciente paciente) throws BadRequestException, ResourceNotFoundException {
            return ResponseEntity.ok(service.modificar(paciente));
        }

        //pacientes/{id}
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public void eliminar(@PathVariable("id") Integer id) throws ResourceNotFoundException, BadRequestException {
            service.eliminar(id);
        }

        @GetMapping("/nombre_Apellido_dni")
        public ResponseEntity<Integer> obtenerIdPaciente(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String dni) throws ResourceNotFoundException {
            PacienteDto pacienteDto = service.findByNombreApellidoDni(nombre, apellido, dni);
            return ResponseEntity.ok(pacienteDto.getId());
        }

    }


