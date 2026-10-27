package net.eventos_facu.eventos.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AcademicoController")
public class AcademicoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Roda contra o MySQL de dev: o MATCH ... AGAINST é específico do MySQL
    // e depende dos seeds de db/seed (acadêmica "Ana Beatriz Souza", CPF 111.111.111-11).
    @Test
    @DisplayName("deve buscar acadêmicos pelo nome")
    void shouldSearchAcademicosByName() throws Exception {
        mockMvc.perform(get("/api/academico/search").param("q", "Ana Beatriz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].academico").value("Ana Beatriz Souza"));
    }

    @Test
    @DisplayName("deve buscar acadêmicos pelo CPF formatado")
    void shouldSearchAcademicosByCpf() throws Exception {
        mockMvc.perform(get("/api/academico/search").param("q", "111.111.111-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cpf").value("111.111.111-11"));
    }

    @Test
    @DisplayName("deve retornar página vazia quando o termo não tem palavra pesquisável")
    void shouldReturnEmptyPageWhenNothingSearchable() throws Exception {
        mockMvc.perform(get("/api/academico/search").param("q", "de"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    @Test
    @DisplayName("deve retornar 400 quando o parâmetro q não é enviado")
    void shouldReturnBadRequestWhenSearchTermIsMissing() throws Exception {
        mockMvc.perform(get("/api/academico/search"))
                .andExpect(status().isBadRequest());
    }
}
