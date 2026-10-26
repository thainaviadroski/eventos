package net.eventos_facu.eventos.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FullTextUtils")
public class FullTextUtilsTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "semana                        | +semana*",
            "Semana eng                    | +semana* +eng*",
            "  semana    engenharia  \t    | +semana* +engenharia*",
            "Ciência                       | +ciência*",
            "hackathon 2026                | +hackathon* +2026*",
            "semana semana                 | +semana*",
            "foo_bar                       | +foo_bar*"
    })
    @DisplayName("Deve tornar cada palavra obrigatória e com busca por prefixo")
    void shouldBuildBooleanQuery(String termo, String expected) {
        assertThat(FullTextUtils.toBooleanQuery(termo)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "+semana -eng                  | +semana* +eng*",
            "\"semana acadêmica\"          | +semana* +acadêmica*",
            "eng* (software) ~web <a >b @3 | +eng* +software* +web*",
            "e-commerce                    | +commerce*"
    })
    @DisplayName("Deve remover os operadores do BOOLEAN MODE digitados pelo usuário")
    void shouldStripBooleanOperators(String termo, String expected) {
        assertThat(FullTextUtils.toBooleanQuery(termo)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "palestras com especialistas   | +palestras* +especialistas*",
            "Congresso de Ciência          | +congresso* +ciência*",
            "THE hackathon                 | +hackathon*"
    })
    @DisplayName("Deve ignorar stopwords do InnoDB e palavras menores que 3 letras")
    void shouldIgnoreStopwordsAndShortWords(String termo, String expected) {
        assertThat(FullTextUtils.toBooleanQuery(termo)).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "de", "com", "ia", "+-*()", "de com la"})
    @DisplayName("Deve retornar vazio quando não sobra palavra pesquisável")
    void shouldReturnEmptyWhenNothingSearchable(String termo) {
        assertThat(FullTextUtils.toBooleanQuery(termo)).isEmpty();
    }
}
