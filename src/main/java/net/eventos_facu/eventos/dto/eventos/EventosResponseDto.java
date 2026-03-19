package net.eventos_facu.eventos.dto.eventos;

import java.time.LocalDateTime;

public record EventosResponseDto(Long id, String evento, String descricao, LocalDateTime dtInicio,
                                 LocalDateTime dtFinal,String slug,
                                 String createdBy, String updatedBy, LocalDateTime created, LocalDateTime updated) {
}
