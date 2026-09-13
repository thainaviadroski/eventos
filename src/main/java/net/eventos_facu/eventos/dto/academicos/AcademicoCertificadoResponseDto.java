package net.eventos_facu.eventos.dto.academicos;

import java.time.Instant;
import java.util.UUID;

public record AcademicoCertificadoResponseDto(
        UUID id,
        Long academicoId,
        String academico,
        Long certificadoId,
        Integer ch,
        Instant created
) {
}
