package net.eventos_facu.eventos.dto;

import java.text.Normalizer;
import java.time.LocalDateTime;

public record EventoDto(String evento, String descricao, LocalDateTime dtInicio, LocalDateTime dtFinal, String createdBy,
                        String updatedBy, LocalDateTime created, LocalDateTime updated) {

    public String slug() {
        return Normalizer.normalize(this.evento, Normalizer.Form.NFD).toLowerCase()
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-");
    }
}
