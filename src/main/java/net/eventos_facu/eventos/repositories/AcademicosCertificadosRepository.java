package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.AcademicosCertificados;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AcademicosCertificadosRepository extends JpaRepository<AcademicosCertificados, UUID> {
}
