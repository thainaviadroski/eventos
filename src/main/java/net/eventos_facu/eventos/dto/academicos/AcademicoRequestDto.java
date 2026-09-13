package net.eventos_facu.eventos.dto.academicos;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record AcademicoRequestDto(
        @NotBlank(message = "Nome do acadêmico é obrigatório")
        String academico,
        @NotBlank(message = "Email é obrigatório")
        String email,
        @NotBlank(message = "CPF é obrigatório")
        String cpf,
        @NotBlank(message = "RA é obrigatório")
        String ra,
        String createdBy,
        Instant created
) {
}
