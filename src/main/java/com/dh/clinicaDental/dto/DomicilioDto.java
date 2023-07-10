package com.dh.clinicaDental.dto;

import lombok.Data;

@Data
public class DomicilioDto {
    private Integer id;
    private String calle;
    private String nro;
    private String localidad;
    private String provincia;
}
