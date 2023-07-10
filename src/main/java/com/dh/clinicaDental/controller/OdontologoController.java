package com.dh.clinicaDental.controller;

import com.dh.clinicaDental.dto.OdontologoDto;
import com.dh.clinicaDental.entity.Odontologo;
import java.util.List;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.service.OdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

  @Autowired
  private OdontologoService service;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> agregar(@RequestBody Odontologo odontologo) throws BadRequestException {
    return ResponseEntity.ok(service.agregar(odontologo));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> buscarPorID(@PathVariable Integer id) throws ResourceNotFoundException, BadRequestException {
    OdontologoDto odontologoDto = service.obtener(id);
    return ResponseEntity.status(HttpStatus.OK).body(odontologoDto);
  }

  @PutMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> modificar(@RequestBody Odontologo odon) throws BadRequestException, ResourceNotFoundException {
    return ResponseEntity.ok(service.modificar(odon));
  }

  @GetMapping
  public List<OdontologoDto> listar() {
    return service.listar();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void eliminar(@PathVariable Integer id) throws ResourceNotFoundException, BadRequestException {
    service.eliminar(id);
  }

  @GetMapping("/idPorNombreyApellido")
  public ResponseEntity<Integer> obtenerIdOdontologo(@RequestParam String nombre, @RequestParam String apellido) throws ResourceNotFoundException {
    OdontologoDto odontologoDto = service.findByNombreAndApellido(nombre, apellido);
    return ResponseEntity.ok(odontologoDto.getId());
  }
}