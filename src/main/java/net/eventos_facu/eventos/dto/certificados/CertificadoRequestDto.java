package net.eventos_facu.eventos.dto.certificados;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

public record CertificadoRequestDto(
        @NotNull Long eventoId,
        @NotNull String cabecalho,
        @NotNull String corpo,
        @NotNull String rodape,
        @NotNull String tipoCertifica,
        String createdBy,
        LocalDateTime created
) {
}
