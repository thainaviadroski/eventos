package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.Academicos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicosRepository extends JpaRepository<Academicos, Long> {
    Optional<Academicos> findByCpfOrRa(String cpf, String ra);
}
