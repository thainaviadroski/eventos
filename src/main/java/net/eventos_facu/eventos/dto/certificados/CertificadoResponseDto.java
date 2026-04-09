package net.eventos_facu.eventos.dto.certificados;

import net.eventos_facu.eventos.entities.TiposCertificados;

import java.time.Instant;

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
