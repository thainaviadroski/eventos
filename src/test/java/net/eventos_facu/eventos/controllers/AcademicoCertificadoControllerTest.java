package net.eventos_facu.eventos.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

    // Pega um certificado da acadêmica de CPF 111.111.111-11 (V1004) e confere
    // que ela aparece na listagem de acadêmicos desse certificado.
    @Test
    @DisplayName("deve listar os acadêmicos do certificado")
    void shouldListAcademicosDoCertificado() throws Exception {
        String academico = mockMvc.perform(get("/api/academico/by-cpf/111.111.111-11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int academicoId = JsonPath.read(academico, "$.id");

        String certificados = mockMvc.perform(get("/api/academico-certificado/academico/{academicoId}", academicoId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int certificadoId = JsonPath.read(certificados, "$.content[0].certificadoId");

        mockMvc.perform(get("/api/academico-certificado/certificado/{certificadoId}", certificadoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].certificadoId", everyItem(is(certificadoId))))
                .andExpect(jsonPath("$.content[*].academicoId", hasItem(academicoId)));
    }

    // @Transactional: o rollback no fim do teste desfaz o import, sem sujar o banco de dev.
    @Test
    @Transactional
    @DisplayName("deve ignorar duplicados e vínculos existentes ao importar o arquivo")
    void shouldSkipDuplicatesOnImport() throws Exception {
        String certificados = mockMvc.perform(get("/api/certificados").param("size", "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int certificadoId = JsonPath.read(certificados, "$.content[0].id");

        // Linha 3 repete a 2 com outro separador; linha 4 é o mesmo acadêmico com outra carga horária.
        MockMultipartFile file = new MockMultipartFile("file", "academicos.csv", "text/csv", """
                academico,email,cpf,ra,ch
                Teste Import,teste.import@aluno.facu.edu.br,914.276.305-34,RA-TESTE-IMPORT,10
                Teste Import;teste.import@aluno.facu.edu.br;914.276.305-34;RA-TESTE-IMPORT;10
                Teste Import,teste.import@aluno.facu.edu.br,914.276.305-34,RA-TESTE-IMPORT,30
                """.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/academico-certificado/import").file(file)
                        .param("certificadoId", String.valueOf(certificadoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ch").value(10));

        // Reimportar o mesmo arquivo não cria novos vínculos
        mockMvc.perform(multipart("/api/academico-certificado/import").file(file)
                        .param("certificadoId", String.valueOf(certificadoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // @Transactional: o rollback no fim do teste desfaz a remoção, sem sujar o banco de dev.
    @Test
    @Transactional
    @DisplayName("deve remover o acadêmico do certificado")
    void shouldRemoveAcademicoDoCertificado() throws Exception {
        String academico = mockMvc.perform(get("/api/academico/by-cpf/111.111.111-11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int academicoId = JsonPath.read(academico, "$.id");

        String certificados = mockMvc.perform(get("/api/academico-certificado/academico/{academicoId}", academicoId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int certificadoId = JsonPath.read(certificados, "$.content[0].certificadoId");

        mockMvc.perform(delete("/api/academico-certificado/certificado/{certificadoId}/academico/{academicoId}", certificadoId, academicoId))
                .andExpect(status().isNoContent());

        // O vínculo não existe mais, então remover de novo retorna 404
        mockMvc.perform(delete("/api/academico-certificado/certificado/{certificadoId}/academico/{academicoId}", certificadoId, academicoId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deve retornar 404 quando o certificado não existe")
    void shouldReturnNotFoundWhenCertificadoDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/academico-certificado/certificado/{certificadoId}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }
}
