package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.AcademicosCertificados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AcademicosCertificadosRepository extends JpaRepository<AcademicosCertificados, UUID> {

    Page<AcademicosCertificados> findByAcademicoId(Long academicoId, Pageable pageable);

    boolean existsByAcademicoIdAndCertificadoId(Long academicoId, Long certificadoId);

    long deleteByAcademicoIdAndCertificadoId(Long academicoId, Long certificadoId);

    // Cada linha aponta para um acadêmico diferente e o mapper lê o nome dele:
    // carrega junto para evitar N+1.
    @EntityGraph(attributePaths = "academico")
    Page<AcademicosCertificados> findByCertificadoId(Long certificadoId, Pageable pageable);
}
