package net.eventos_facu.eventos.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.certificados.CertificadoRequestDto;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.exceptions.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.CertificadosMapper;
import net.eventos_facu.eventos.repositories.CertificadosRepository;
import net.eventos_facu.eventos.repositories.EventosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificadosService {
    private Logger logger = LoggerFactory.getLogger(EventosService.class);

    private final CertificadosRepository reposioty;
    private final CertificadosMapper mapper;
    private final EventosRepository eventosRepository;

    public void createNewCertificado(CertificadoRequestDto certificado) {
        Eventos evento = eventosRepository.findById(certificado.eventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com id: " + certificado.eventoId()));
        Certificados entity = mapper.toEntity(certificado);
        entity.setEvento(evento);
        entity = save(entity);

    }

    private Certificados save(Certificados entity) {
        return reposioty.save(entity);
    }

    public void findById(Long id) {

    }

    public void findByEventoId(Long eventoId) {

    }


}
