package net.eventos_facu.eventos.services;

import lombok.RequiredArgsConstructor;
import net.eventos_facu.eventos.dto.certificados.CertificadoRequestDto;
import net.eventos_facu.eventos.dto.certificados.CertificadoResponseDto;
import net.eventos_facu.eventos.entities.Certificados;
import net.eventos_facu.eventos.entities.Eventos;
import net.eventos_facu.eventos.exceptions.ResourceNotFoundException;
import net.eventos_facu.eventos.mapper.CertificadosMapper;
import net.eventos_facu.eventos.repositories.CertificadosRepository;
import net.eventos_facu.eventos.repositories.EventosRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificadosService {
    private Logger logger = LoggerFactory.getLogger(EventosService.class);

    private final CertificadosRepository repository;
    private final EventosRepository eventosRepository;
    private final CertificadosMapper mapper;
    private final CertificadosImagesService imagesService;

    @Transactional
    public CertificadoResponseDto createNewCertificado(CertificadoRequestDto certificado, MultipartFile fundo) throws IOException {
        logger.info("Creating certificado");
        Eventos evento = eventosRepository.findById(certificado.eventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com id: " + certificado.eventoId()));
        Certificados entity = mapper.toEntity(certificado);
        entity.setEvento(evento);
        entity = save(entity);

        imagesService.createNewCertificadoImages(fundo, entity);

        return mapper.toDtoResponse(entity);
    }

    private Certificados save(Certificados entity) {
        return repository.save(entity);
    }


    public Page<CertificadoResponseDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDtoResponse);
    }

    public CertificadoResponseDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDtoResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado não encontrado, id: {}" + id));
    }

    public List<CertificadoResponseDto> findByEventoId(Long eventoId) {
        return repository.findByEventoId(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Não foram encontrados certificados para o evento, id: " + eventoId))
                .stream()
                .map(mapper::toDtoResponse)
                .toList();
    }
}
