package net.eventos_facu.eventos.dto.eventos;

import jakarta.validation.constraints.NotNull;

import java.text.Normalizer;
import java.time.LocalDateTime;

public record EventoRequestDto(@NotNull String evento, @NotNull String descricao, @NotNull LocalDateTime dtInicio,
                               @NotNull LocalDateTime dtFinal, String createdBy,
                               String updatedBy, LocalDateTime created, LocalDateTime updated) {

    public String slug() {
        return Normalizer.normalize(this.evento, Normalizer.Form.NFD).toLowerCase()
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-");
    }
}
