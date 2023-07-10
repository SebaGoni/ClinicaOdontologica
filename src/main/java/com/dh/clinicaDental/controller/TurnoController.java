package com.dh.clinicaDental.controller;

import com.dh.clinicaDental.dto.TurnoDto;
import com.dh.clinicaDental.entity.Turno;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
    @RestController
    @RequestMapping("/turnos")
    public class TurnoController {

        @Autowired
        private TurnoService service;

        @GetMapping
        public List<?> listar() {
            return service.listar();
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> buscarPorId(@PathVariable("id") Integer id) throws ResourceNotFoundException {
            TurnoDto turnoDto = service.obtener(id);
            return ResponseEntity.status(HttpStatus.OK).body(turnoDto);
        }
        @PostMapping
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<?> guardar(@RequestBody Turno turno) throws ResourceNotFoundException, BadRequestException {
            TurnoDto turnoDto = service.agregar(turno);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        @PutMapping()
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<?> modificar(@RequestBody Turno turno) throws ResourceNotFoundException, BadRequestException {
            TurnoDto turnoDto = service.modificarTurno(turno);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        public ResponseEntity<?> eliminarPorId(@PathVariable("id") Integer id) throws ResourceNotFoundException {
            service.eliminar(id);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }
