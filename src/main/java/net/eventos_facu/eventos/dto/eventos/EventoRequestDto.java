package net.eventos_facu.eventos.dto.eventos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.text.Normalizer;
import java.time.LocalDateTime;

public record EventoRequestDto(@NotBlank(message = "Evento é obrigatório")
                               String evento,
                               @NotBlank(message = "Descrição é obrigatória")
                               String descricao,
                               @NotNull(message = "Data de início é obrigatória")
                               LocalDateTime dtInicio,
                               @NotNull(message = "Data final é obrigatória")
                               LocalDateTime dtFinal,
                               String createdBy, String updatedBy, LocalDateTime created, LocalDateTime updated) {

    public String slug() {
        return Normalizer.normalize(this.evento, Normalizer.Form.NFD).toLowerCase().replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").trim().replaceAll("[^a-z0-9\\s-]", "").replaceAll("[\\s]+", "-");
    }
}
