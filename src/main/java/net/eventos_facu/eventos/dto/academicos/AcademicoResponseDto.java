package net.eventos_facu.eventos.dto.academicos;

import java.time.Instant;

public record AcademicoResponseDto(
        Long id,
        String academico,
        String email,
        String cpf,
        String ra,
        String createdBy,
        String updatedBy,
        Instant created,
        Instant updated
) {
}
