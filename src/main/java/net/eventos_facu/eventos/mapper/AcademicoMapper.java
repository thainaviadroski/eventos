package net.eventos_facu.eventos.mapper;

import net.eventos_facu.eventos.dto.academicos.AcademicoRequestDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoResponseDto;
import net.eventos_facu.eventos.dto.academicos.AcademicoUpdateDto;
import net.eventos_facu.eventos.entities.Academicos;
import org.springframework.stereotype.Component;

@Component
public class AcademicoMapper {

    public Academicos toEntity(AcademicoRequestDto dto) {
        Academicos academico = new Academicos();
        academico.setAcademico(dto.academico());
        academico.setEmail(dto.email());
        academico.setCpf(dto.cpf());
        academico.setRa(dto.ra());
        academico.setCreatedBy(dto.createdBy());
        academico.setCreated(dto.created());
        return academico;
    }

    public AcademicoResponseDto toDtoResponse(Academicos academico) {
        return new AcademicoResponseDto(academico.getId(), academico.getAcademico(), academico.getEmail(),
                academico.getCpf(), academico.getRa(), academico.getCreatedBy(), academico.getUpdatedBy(),
                academico.getCreated(), academico.getUpdated());
    }

    public void updateEntity(Academicos academico, AcademicoUpdateDto dto) {
        academico.setAcademico(dto.academico());
        academico.setEmail(dto.email());
        academico.setCpf(dto.cpf());
        academico.setRa(dto.ra());
        academico.setUpdatedBy(dto.updatedBy());
        academico.setUpdated(dto.updated());
    }
}
