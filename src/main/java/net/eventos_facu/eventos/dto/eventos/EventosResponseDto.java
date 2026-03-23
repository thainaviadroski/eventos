package net.eventos_facu.eventos.dto.eventos;

import java.time.LocalDateTime;

public record EventosResponseDto(Long id, String evento, String descricao, String slug, LocalDateTime dtInicio,
                                 LocalDateTime dtFinal, String createdBy, String updatedBy,
                                 LocalDateTime created, LocalDateTime updated) {
}
