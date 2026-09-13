package net.eventos_facu.eventos.mapper;

import net.eventos_facu.eventos.dto.academicos.AcademicoCertificadoResponseDto;
import net.eventos_facu.eventos.entities.AcademicosCertificados;
import org.springframework.stereotype.Component;

@Component
public class AcademicoCertificadoMapper {

    public AcademicoCertificadoResponseDto toDtoResponse(AcademicosCertificados entity) {
        return new AcademicoCertificadoResponseDto(
                entity.getId(),
                entity.getAcademico().getId(),
                entity.getAcademico().getAcademico(),
                entity.getCertificado().getId(),
                entity.getCh(),
                entity.getCreated()
        );
    }
}
