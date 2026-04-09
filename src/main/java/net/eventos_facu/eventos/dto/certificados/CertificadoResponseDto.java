package net.eventos_facu.eventos.dto.certificados;

import jakarta.validation.constraints.NotNull;
import net.eventos_facu.eventos.dto.eventos.EventosResponseDto;
import net.eventos_facu.eventos.entities.TiposCertificados;

import java.time.Instant;
import java.time.LocalDateTime;

public record CertificadoResponseDto(
        Long id,
        Long eventoId,
        String cabecalho,
        String corpo,
        String rodape,
        TiposCertificados tipoCertifica,
        String createdBy,
        Instant created
) {

}
