package com.dh.clinicaDental.service;

import com.dh.clinicaDental.dto.OdontologoDto;
import com.dh.clinicaDental.entity.Domicilio;
import com.dh.clinicaDental.entity.Odontologo;
import com.dh.clinicaDental.entity.Paciente;
import com.dh.clinicaDental.entity.Turno;
import com.dh.clinicaDental.exceptions.BadRequestException;
import com.dh.clinicaDental.exceptions.ResourceNotFoundException;
import com.dh.clinicaDental.repository.OdontologoRepository;
import com.dh.clinicaDental.repository.TurnoRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class OdontologoServiceTest {
    @InjectMocks
    OdontologoService odontologoService;
    @Mock
    OdontologoRepository odontologoRepository;
    @Mock
    private TurnoRepository turnoRepository;


    //----------------TESTS PARA EL METODO obtener()-----------------------------------------

    @Test
    void buscar_odontologo_existente_por_id() throws BadRequestException, ResourceNotFoundException {
        //ARRANGE
        Odontologo odontologo = new Odontologo(1, "Seba", "Goñi", 325);
        when(odontologoRepository.findById(any())).thenReturn(Optional.of(odontologo));

        //ACT
        OdontologoDto odontologoDto = odontologoService.obtener(1);

        //ASSERT
        Assertions.assertEquals("Seba", odontologoDto.getNombre());

    }

    @Test
    void buscar_odontologo_no_existente_por_id() {
        // ARRANGE
        when(odontologoRepository.findById(any())).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.obtener(1);
        });

        // ASSERT
        Assertions.assertEquals("Odontologo no encontrado", exception.getMessage());
    }


    //----------------TESTS PARA EL METODO agregar()----------------------------------------

    @Test
    void agregar_odontologo_nuevo() throws BadRequestException, ResourceNotFoundException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Seba");
        odontologo.setApellido("Goni");
        odontologo.setMatricula(325);
        when(odontologoRepository.save(any())).thenReturn(odontologo);

        // ACT
        OdontologoDto odontologoDto = odontologoService.agregar(odontologo);

        // ASSERT
        Assertions.assertEquals("Seba", odontologoDto.getNombre());
    }


    @Test
    void agregar_odontologo_Misma_Matricula() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo1 = new Odontologo();
        odontologo1.setNombre("Juan");
        odontologo1.setApellido("Perez");
        odontologo1.setMatricula(1234);

        Odontologo odontologo2 = new Odontologo();
        odontologo2.setNombre("Maria");
        odontologo2.setApellido("Lopez");
        odontologo2.setMatricula(1234);

        List<Odontologo> odontologosExistentes = new ArrayList<>();
        odontologosExistentes.add(odontologo1);
        when(odontologoRepository.findAll()).thenReturn(odontologosExistentes);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo2);
        });

        // ASSERT
        assertEquals("Ya existe un odontólogo con la misma matrícula", exception.getMessage());
    }

    @Test
    void agregar_odontologo_con_id() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setId(1);
        odontologo.setNombre("Maria");
        odontologo.setApellido("Lopez");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("No puede incluir el id. Se genera automaticamente", exception.getMessage());
    }

    @Test
    void agregar_dontologo_matricula_negativa() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Maria");
        odontologo.setApellido("Lopez");
        odontologo.setMatricula(-1000);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("La matrícula no puede ser 0 o negativa", exception.getMessage());
    }


    @Test
    void agregar_odontologo_sin_nombre() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setApellido("Lopez");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }


    @Test
    void agregar_dontologo_sin_apellido() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Maria");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("El apellido es obligatorio", exception.getMessage());
    }

    @Test
    void agregar_dontologo_sin_matricula() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Maria");
        odontologo.setNombre("Lopez");

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("La matricula es obligatoria", exception.getMessage());
    }

    @Test
    void agregar_dontologo_nombre_menos_de_tres_letras() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Ma");
        odontologo.setApellido("Lopez");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("El nombre debe tener entre 3 y 50 letras", exception.getMessage());
    }

    @Test
    void agregar_dontologo_apellido_menos_de_tres_letras() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Maria");
        odontologo.setApellido("Lo");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("El apellido debe tener entre 3 y 50 letras", exception.getMessage());
    }

    @Test
    void agregar_dontologo_nombre_con_caracteres_especiales() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Ma$");
        odontologo.setApellido("Lopez");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("El nombre no puede contener números ni caracteres especiales", exception.getMessage());
    }

    @Test
    void agregar_dontologo_apellido_con_caracteres_especiales() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Maria");
        odontologo.setApellido("Lop$$");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("El apellido no puede contener números ni caracteres especiales", exception.getMessage());
    }

    @Test
    void agregar_dontologo_con_id() throws BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo();
        odontologo.setId(1);
        odontologo.setNombre("Maria");
        odontologo.setApellido("Lopez");
        odontologo.setMatricula(173);

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.agregar(odontologo);
        });

        // ASSERT
        assertEquals("No puede incluir el id. Se genera automaticamente", exception.getMessage());
    }


    //----------------TESTS PARA EL METODO eliminar()------------------------------------

    @Test
    void eliminar_odontologo_existente() throws BadRequestException, ResourceNotFoundException {
        // ARRANGE
        Odontologo odontologo = new Odontologo(1, "Pablo", "Vidal", 325);
        when(odontologoRepository.findById(1)).thenReturn(Optional.of(odontologo));
        when(turnoRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        odontologoService.eliminar(1);

        // ASSERT
        verify(odontologoRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_odontologo_no_existente() throws ResourceNotFoundException {
        // ARRANGE
        when(odontologoRepository.findById(1)).thenReturn(Optional.empty());

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.eliminar(1);
        });

        // ASSERT
        Assertions.assertEquals("Odontologo no encontrado", exception.getMessage());
    }

    @Test
    void eliminar_odontologo_con_turno_asignado() throws ResourceNotFoundException, BadRequestException {
        // ARRANGE
        Odontologo odontologo = new Odontologo(1, "Pablo", "Vidal", 325);
        Domicilio domicilio = new Domicilio();
        domicilio.setId(1);
        domicilio.setCalle("Pampa");
        domicilio.setNro("1235");
        domicilio.setLocalidad("Mar del Plata");
        domicilio.setProvincia("Buenos Aires");
        Paciente paciente = new Paciente(1, "Seba", "Goni", "23659878", LocalDate.of(2023, 7, 3), domicilio);
        Turno turno = new Turno();
        turno.setOdontologo(odontologo);
        turno.setPaciente(paciente);

        when(odontologoRepository.findById(1)).thenReturn(Optional.of(odontologo));
        when(turnoRepository.findAll()).thenReturn(Collections.singletonList(turno));

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.eliminar(1);
        });

        // ASSERT
        assertEquals("No se puede eliminar un odontologo con turno asignado", exception.getMessage());

        verify(odontologoRepository, never()).deleteById(1);
    }


    //----------------TESTS PARA EL METODO modificar()------------------------------------


    @Test
    void modificar_odontologo_existente() throws ResourceNotFoundException, BadRequestException {
        // ARRANGE
        Odontologo odontologoExistente = new Odontologo();
        odontologoExistente.setId(1);
        odontologoExistente.setNombre("Juan");
        odontologoExistente.setApellido("Perez");
        odontologoExistente.setMatricula(1234);

        Odontologo odontologoModificado = new Odontologo();
        odontologoModificado.setId(1);
        odontologoModificado.setNombre("Seba");
        odontologoModificado.setApellido("Goni");
        odontologoModificado.setMatricula(5679);

        when(odontologoRepository.findById(1)).thenReturn(Optional.of(odontologoExistente));
        when(odontologoRepository.save(odontologoModificado)).thenReturn(odontologoModificado);

        // ACT
        OdontologoDto odontologoActualizado = odontologoService.modificar(odontologoModificado);

        // ASSERT
        assertNotNull(odontologoActualizado);
        assertEquals("Seba", odontologoActualizado.getNombre());
        assertEquals("Goni", odontologoActualizado.getApellido());
        assertEquals(5679, odontologoActualizado.getMatricula());
    }

    @Test
    void modificar_odontologo_no_existente() throws ResourceNotFoundException {
        // ARRANGE
        Odontologo odontologoNoExistente = new Odontologo();
        odontologoNoExistente.setId(1);
        odontologoNoExistente.setNombre("Juan");
        odontologoNoExistente.setApellido("Perez");
        odontologoNoExistente.setMatricula(1234);

        when(odontologoRepository.findById(1)).thenReturn(Optional.empty());

        //ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.modificar(odontologoNoExistente);
        });

        // ASSERT
        Assertions.assertEquals("Odontólogo no encontrado", exception.getMessage());
    }

    @Test
    void modificar_odontologo_Misma_Matricula_que_Otro() throws BadRequestException, ResourceNotFoundException {
        // ARRANGE
        Odontologo odontologoExistente = new Odontologo();
        odontologoExistente.setId(1);
        odontologoExistente.setNombre("Juan");
        odontologoExistente.setApellido("Perez");
        odontologoExistente.setMatricula(1234);

        Odontologo odontologoExistente1 = new Odontologo();
        odontologoExistente1.setId(2);
        odontologoExistente1.setNombre("Maria");
        odontologoExistente1.setApellido("Lopez");
        odontologoExistente1.setMatricula(5678);

        Odontologo odontologoModificado = new Odontologo();
        odontologoModificado.setId(2);
        odontologoModificado.setNombre("Maria");
        odontologoModificado.setApellido("Lopez");
        odontologoModificado.setMatricula(1234);

        List<Odontologo> odontologosExistentes = new ArrayList<>();
        odontologosExistentes.add(odontologoExistente);
        odontologosExistentes.add(odontologoExistente1);
        when(odontologoRepository.findAll()).thenReturn(odontologosExistentes);
        when(odontologoRepository.findById(2)).thenReturn(Optional.of(odontologoModificado));

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.modificar(odontologoModificado);
        });

        // ASSERT
        assertEquals("Ya existe un odontólogo con la misma matrícula", exception.getMessage());
        verify(odontologoRepository, never()).save(odontologoModificado);

    }

    @Test
    void modificar_odontologo_sin_id() throws BadRequestException, ResourceNotFoundException {
        // ARRANGE
        Odontologo odontologoExistente = new Odontologo();
        odontologoExistente.setId(1);
        odontologoExistente.setNombre("Juan");
        odontologoExistente.setApellido("Perez");
        odontologoExistente.setMatricula(1234);

        Odontologo odontologoExistente1 = new Odontologo();
        odontologoExistente1.setId(2);
        odontologoExistente1.setNombre("Maria");
        odontologoExistente1.setApellido("Lopez");
        odontologoExistente1.setMatricula(5678);

        Odontologo odontologoModificado = new Odontologo();
        odontologoModificado.setNombre("Maria");
        odontologoModificado.setApellido("Lopez");
        odontologoModificado.setMatricula(5235);

        List<Odontologo> odontologosExistentes = new ArrayList<>();
        odontologosExistentes.add(odontologoExistente);
        odontologosExistentes.add(odontologoExistente1);
        when(odontologoRepository.findAll()).thenReturn(odontologosExistentes);
        when(odontologoRepository.findById(2)).thenReturn(Optional.of(odontologoModificado));

        // ACT
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            odontologoService.modificar(odontologoModificado);
        });

        // ASSERT
        assertEquals("El ID es obligatorio", exception.getMessage());
        verify(odontologoRepository, never()).save(odontologoModificado);

    }


    //----------------TEST PARA EL METODO listar()------------------------------

    @Test
    void listar_todos_los_odontologos() {
        // ARRANGE
        List<Odontologo> odontologos = new ArrayList<>();
        Odontologo odontologo1 = new Odontologo();
        odontologo1.setId(1);
        odontologo1.setNombre("Pepito");
        odontologo1.setApellido("Perez");
        odontologo1.setMatricula(1234);
        odontologos.add(odontologo1);

        Odontologo odontologo2 = new Odontologo();
        odontologo2.setId(2);
        odontologo2.setNombre("Maria");
        odontologo2.setApellido("Sanchez");
        odontologo2.setMatricula(5678);
        odontologos.add(odontologo2);

        when(odontologoRepository.findAll()).thenReturn(odontologos);

        // ACT
        List<OdontologoDto> odontologosDto = odontologoService.listar();

        // ASSERT
        assertNotNull(odontologosDto);
        assertEquals(2, odontologosDto.size());

        OdontologoDto odontologoDto1 = odontologosDto.get(0);
        assertEquals(1, odontologoDto1.getId());
        assertEquals("Pepito", odontologoDto1.getNombre());
        assertEquals("Perez", odontologoDto1.getApellido());
        assertEquals(1234, odontologoDto1.getMatricula());

        OdontologoDto odontologoDto2 = odontologosDto.get(1);
        assertEquals(2, odontologoDto2.getId());
        assertEquals("Maria", odontologoDto2.getNombre());
        assertEquals("Sanchez", odontologoDto2.getApellido());
        assertEquals(5678, odontologoDto2.getMatricula());
    }

    //----------------TEST PARA EL METODO findByNombreAndApellido()---------------------------

    @Test
    void findByNombreAndApellido_odontologo_encontrado() throws ResourceNotFoundException {
        // ARRANGE
        String nombre = "Seba";
        String apellido = "Goni";

        Odontologo odontologoEncontrado = new Odontologo();
        odontologoEncontrado.setId(1);
        odontologoEncontrado.setNombre(nombre);
        odontologoEncontrado.setApellido(apellido);
        odontologoEncontrado.setMatricula(1234);

        OdontologoDto odontologoDtoEsperado = new OdontologoDto();
        odontologoDtoEsperado.setId(1);
        odontologoDtoEsperado.setNombre(nombre);
        odontologoDtoEsperado.setApellido(apellido);
        odontologoDtoEsperado.setMatricula(1234);

        when(odontologoRepository.findByNombreAndApellido(nombre, apellido)).thenReturn(odontologoEncontrado);

        // ACT
        OdontologoDto odontologoDto = odontologoService.findByNombreAndApellido(nombre, apellido);

        // ASSERT
        assertNotNull(odontologoDto);
        assertEquals(odontologoDtoEsperado.getId(), odontologoDto.getId());
        assertEquals(odontologoDtoEsperado.getNombre(), odontologoDto.getNombre());
        assertEquals(odontologoDtoEsperado.getApellido(), odontologoDto.getApellido());
        assertEquals(odontologoDtoEsperado.getMatricula(), odontologoDto.getMatricula());
    }

    @Test
    void findByNombreAndApellido_odontologo_no_encontrado() {
        // ARRANGE
        String nombre = "Seba";
        String apellido = "Goni";

        when(odontologoRepository.findByNombreAndApellido(nombre, apellido)).thenReturn(null);

        // ACT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            odontologoService.findByNombreAndApellido(nombre, apellido);
        });

        // ASSERT
        Assertions.assertEquals("Odontologo no encontrado", exception.getMessage());
    }


}






