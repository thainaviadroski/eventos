package net.eventos_facu.eventos.dto.eventos;

import java.text.Normalizer;
import java.time.LocalDateTime;

public record EventosUpdateDto(Long id, String evento, String descricao, LocalDateTime dtInicio,
                               LocalDateTime dtFinal, String updatedBy, LocalDateTime updated
) {
    public String slug() {
        return Normalizer.normalize(this.evento, Normalizer.Form.NFD).toLowerCase()
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .trim().replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-");
    }
}
