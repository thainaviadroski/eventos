package net.eventos_facu.eventos.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AcademicoCertificadoController")
public class AcademicoCertificadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Roda contra o MySQL de dev e depende dos seeds de db/seed:
    // a acadêmica de CPF 111.111.111-11 tem 2 certificados (V1004).
    @Test
    @DisplayName("deve listar os certificados do acadêmico")
    void shouldListCertificadosDoAcademico() throws Exception {
        String academico = mockMvc.perform(get("/api/academico/by-cpf/111.111.111-11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int academicoId = JsonPath.read(academico, "$.id");

        mockMvc.perform(get("/api/academico-certificado/academico/{academicoId}", academicoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.content[*].academicoId", everyItem(is(academicoId))));
    }

    @Test
    @DisplayName("deve retornar 404 quando o acadêmico não existe")
    void shouldReturnNotFoundWhenAcademicoDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/academico-certificado/academico/{academicoId}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }
}
