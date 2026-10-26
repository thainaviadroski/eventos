package net.eventos_facu.eventos.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CpfUtils")
public class CpfUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"52998224725", "529.982.247-25", "11144477735", "111.444.777-35"})
    @DisplayName("Deve aceitar CPFs válidos, com ou sem formatação")
    void shouldAcceptValidCpf(String cpf) {
        assertThat(CpfUtils.isValid(cpf)).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "52998224724",      // dígito verificador errado
            "529.982.247-26",
            "11111111111",      // sequência repetida
            "000.000.000-00",
            "5299822472",       // 10 dígitos
            "529982247250",     // 12 dígitos
            "529.982.24725",    // formatação incompleta
            "529a982b247c25",
            " 52998224725"
    })
    @DisplayName("Deve rejeitar CPFs inválidos")
    void shouldRejectInvalidCpf(String cpf) {
        assertThat(CpfUtils.isValid(cpf)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "529.982.247-25, 52998224725",
            "52998224725, 52998224725",
            "529982247-25, 52998224725"
    })
    @DisplayName("Deve remover pontos e traço do CPF")
    void shouldSanitizeCpf(String cpf, String expected) {
        assertThat(CpfUtils.sanitize(cpf)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Deve retornar null ao sanitizar CPF null")
    void shouldReturnNullWhenSanitizingNull() {
        assertThat(CpfUtils.sanitize(null)).isNull();
    }
}
