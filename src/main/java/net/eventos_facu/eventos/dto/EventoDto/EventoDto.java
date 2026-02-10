package net.eventos_facu.eventos.dto.EventoDto;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

public record EventoDto(String evento, String descricao, LocalDateTime dtIncio, LocalDateTime dtFinal, String createdBy,
                        String updateBy, LocalDateTime created, LocalDateTime updated) {
}
