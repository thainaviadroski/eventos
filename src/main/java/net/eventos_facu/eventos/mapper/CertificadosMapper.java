package net.eventos_facu.eventos.mapper;


import net.eventos_facu.eventos.dto.certificados.CertificadoRequestDto;
import net.eventos_facu.eventos.dto.certificados.CertificadoResponseDto;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.entities.TiposCertificados;
import org.springframework.stereotype.Component;

@Component
public class CertificadosMapper {
    public Certificados toEntity(CertificadoRequestDto certificado) {
        return Certificados.builder()
                .cabecalho(certificado.cabecalho())
                .corpo(certificado.corpo())
                .rodape(certificado.rodape())
                .descricao(certificado.descricao())
                .tiposCertificados(tipoCertificado(certificado.tipoCertifica()))
                .build();
    }

    public CertificadoResponseDto toDtoResponse(Certificados certificados) {
        return new CertificadoResponseDto(
                certificados.getId(),
                certificados.getEvento().getId(),
                certificados.getCabecalho(),
                certificados.getCorpo(),
                certificados.getRodape(),
                certificados.getTiposCertificados(),
                certificados.getCreatedBy(),
                certificados.getCreated()
        );
    }

    private TiposCertificados tipoCertificado(String tipoCertificado) {
        if (tipoCertificado == null) return TiposCertificados.PARTICIPANTE;
        try {
            return TiposCertificados.valueOf(tipoCertificado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo do certificado invalido" + e.getMessage());
        }
    }
}
