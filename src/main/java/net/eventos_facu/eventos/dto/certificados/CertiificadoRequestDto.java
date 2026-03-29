package net.eventos_facu.eventos.dto.certificados;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CertiificadoRequestDto(
        @NotNull Long eventoId,
        @NotNull String cabecalho,
        @NotNull String corpo,
        @NotNull String rodape,
        @NotNull String tipoCertifica,
        String createdBy,
        Instant created
) {
}
