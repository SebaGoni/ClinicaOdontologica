package com.dh.clinicaDental.service;

import com.dh.clinicaDental.dto.DomicilioDto;
import com.dh.clinicaDental.entity.Domicilio;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.repository.DomicilioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DomicilioService{
    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private ObjectMapper mapper;

    public DomicilioDto agregar(Domicilio domicilio) {
        Domicilio nuevoDomicilio = domicilioRepository.save(domicilio);
        return mapper.convertValue(nuevoDomicilio, DomicilioDto.class);
    }

    public DomicilioDto obtener(Integer id) throws ResourceNotFoundException {
        Optional<Domicilio> domicilio = domicilioRepository.findById(id);
        if (domicilio.isPresent()) {
            return mapper.convertValue(domicilio.get(), DomicilioDto.class);
        } else {
            throw new ResourceNotFoundException("1049", "Domicilio no encontrado");
        }
    }

    public void eliminar(Integer id) throws ResourceNotFoundException{
        Optional<Domicilio> domicilio = domicilioRepository.findById(id);
        if (domicilio.isPresent()) {
            domicilioRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("1049", "Domicilio no encontrado");
        }
    }

    public DomicilioDto modificar(Domicilio domicilio) {
        Domicilio nuevodomicilio = domicilioRepository.save(domicilio);
        return mapper.convertValue(nuevodomicilio, DomicilioDto.class);
    }

    public List<DomicilioDto> listar() {
        List<Domicilio> listaDomicilios = domicilioRepository.findAll();
        return listaDomicilios
                .stream()
                .map(domicilio -> mapper.convertValue(domicilio, DomicilioDto.class))
                .collect(Collectors.toList());
    }
}
