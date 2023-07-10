package com.dh.clinicaDental.Integration;

import com.dh.clinicaDental.entity.Domicilio;
import com.dh.clinicaDental.entity.Odontologo;
import com.dh.clinicaDental.entity.Paciente;
import com.dh.clinicaDental.repository.OdontologoRepository;
import com.dh.clinicaDental.repository.PacienteRepository;
import com.dh.clinicaDental.service.OdontologoService;
import com.dh.clinicaDental.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class IntegrationPacienteTest {

    @Autowired
    private MockMvc mvc;
    @InjectMocks
    PacienteService pacienteService;
    @Mock
    PacienteRepository pacienteRepository;

    @Test
    void buscarPacientePorID() throws Exception {
        Domicilio domicilio = new Domicilio();
        domicilio.setId(1);
        domicilio.setCalle("Pampa");
        domicilio.setNro("1235");
        domicilio.setLocalidad("Mar del Plata");
        domicilio.setProvincia("Buenos Aires");
        Paciente paciente = new Paciente(1, "Seba", "Goni", "23659878", LocalDate.of(2023, 7, 3), domicilio);
        when(pacienteRepository.findById(any())).thenReturn(Optional.of(paciente));

        mvc.perform(MockMvcRequestBuilders.get("/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
