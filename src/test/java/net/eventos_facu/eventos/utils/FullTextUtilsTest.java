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
            "semana                        | semana",
            "Semana eng                    | semana eng",
            "  semana    engenharia  \t    | semana engenharia",
            "Ciência                       | ciência",
            "hackathon 2026                | hackathon 2026",
            "IA aplicada                   | ia aplicada",
            "semana semana                 | semana",
            "foo_bar                       | foo_bar"
    })
    @DisplayName("Deve normalizar as palavras separando-as por um espaço")
    void shouldBuildNaturalLanguageQuery(String termo, String expected) {
        assertThat(FullTextUtils.toNaturalLanguageQuery(termo)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "+semana -eng                  | semana eng",
            "\"semana acadêmica\"          | semana acadêmica",
            "eng* (software) ~web <a >b @3 | eng software web",
            "e-commerce                    | commerce"
    })
    @DisplayName("Deve remover pontuação e operadores digitados pelo usuário")
    void shouldStripPunctuationAndOperators(String termo, String expected) {
        assertThat(FullTextUtils.toNaturalLanguageQuery(termo)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "palestras com especialistas   | palestras especialistas",
            "Congresso de Ciência          | congresso ciência",
            "THE hackathon                 | hackathon"
    })
    @DisplayName("Deve ignorar stopwords do InnoDB e palavras de uma letra")
    void shouldIgnoreStopwordsAndShortWords(String termo, String expected) {
        assertThat(FullTextUtils.toNaturalLanguageQuery(termo)).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "a", "de", "com", "+-*()", "de com la"})
    @DisplayName("Deve retornar vazio quando não sobra palavra pesquisável")
    void shouldReturnEmptyWhenNothingSearchable(String termo) {
        assertThat(FullTextUtils.toNaturalLanguageQuery(termo)).isEmpty();
    }
}
