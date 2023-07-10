package com.dh.clinicaDental.exceptions;

import lombok.Getter;
@Getter
public class ResourceNotFoundException extends Exception{
    private String code;

    public ResourceNotFoundException(String code, String mensaje) {
        super(mensaje);
        this.code = code;
    }
}
