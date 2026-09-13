package net.eventos_facu.eventos.dto.academicos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AcademicoCertificadoManualDto(
        @NotNull(message = "Dados do acadêmico são obrigatórios")
        @Valid
        AcademicoRequestDto academico,
        @NotNull(message = "Certificado é obrigatório")
        Long certificadoId,
        @NotNull(message = "Carga horária é obrigatória")
        Integer ch
) {
}
