package net.eventos_facu.eventos.repositories;

import net.eventos_facu.eventos.entities.Academicos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AcademicosRepository extends JpaRepository<Academicos, Long> {
    Optional<Academicos> findByCpfOrRa(String cpf,  String ral);
    Optional<Academicos> findByCpf(String cpf);

    // Usa o índice FULLTEXT idx_search_academicos (V7): o MATCH precisa listar exatamente as colunas do índice.
    // "termo" deve vir limpo por FullTextUtils.toNaturalLanguageQuery.
    @Query(value = """
            SELECT * FROM academicos
            WHERE MATCH(academico, cpf, ra) AGAINST(:termo IN NATURAL LANGUAGE MODE)
            ORDER BY MATCH(academico, cpf, ra) AGAINST(:termo IN NATURAL LANGUAGE MODE) DESC, id DESC
            """,
            countQuery = """
            SELECT COUNT(*) FROM academicos
            WHERE MATCH(academico, cpf, ra) AGAINST(:termo IN NATURAL LANGUAGE MODE)
            """,
            nativeQuery = true)
    Page<Academicos> search(@Param("termo") String termo, Pageable pageable);
}
