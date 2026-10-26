package net.eventos_facu.eventos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@DisplayName("EventosController")
public class EventosControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    void shouldExceptionWhenInvalidFields(){

    }

    // Roda contra o MySQL de dev: o MATCH ... AGAINST é específico do MySQL
    // e depende dos seeds de db/seed (evento "congresso-ciencia-computacao").
    @Test
    @DisplayName("deve buscar eventos sem diferenciar acentos")
    void shouldSearchEventos() throws Exception {
        mockMvc.perform(get("/api/eventos/search").param("q", "ciencia computacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].slug").value("congresso-ciencia-computacao"));
    }

    @Test
    @DisplayName("deve retornar página vazia quando o termo não tem palavra pesquisável")
    void shouldReturnEmptyPageWhenNothingSearchable() throws Exception {
        mockMvc.perform(get("/api/eventos/search").param("q", "de"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    @Test
    @DisplayName("deve retornar 400 quando o parâmetro q não é enviado")
    void shouldReturnBadRequestWhenSearchTermIsMissing() throws Exception {
        mockMvc.perform(get("/api/eventos/search"))
                .andExpect(status().isBadRequest());
    }
}
