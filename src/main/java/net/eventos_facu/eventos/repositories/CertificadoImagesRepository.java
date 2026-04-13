package net.eventos_facu.eventos.repositories;


import net.eventos_facu.eventos.entities.CertificadoImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificadoImagesRepository extends JpaRepository<CertificadoImages, Long> {
    List<CertificadoImages> findByCertificadoId(Long certificadoId);
}
