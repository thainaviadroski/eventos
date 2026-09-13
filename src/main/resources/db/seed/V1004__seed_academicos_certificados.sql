-- Seed de desenvolvimento: dados fake para a tabela `academicos_certificados`.
-- Depende de `academicos` (V1001) e `certificados` (V1002).
--
-- O id é UUID gerado pela aplicação (Hibernate GenerationType.UUID),
-- armazenado como BINARY(16). UUID_TO_BIN(UUID()) sem swap-flag gera bytes
-- no mesmo layout que o Hibernate grava, então é seguro para seed de dev.

INSERT INTO academicos_certificados (id, academico_id, certificado_id, created, ch)
SELECT UUID_TO_BIN(UUID()), a.id, c.id, NOW(), 20
FROM academicos a, certificados c
WHERE a.email = 'ana.souza@aluno.facu.edu.br'
  AND c.cabecalho = 'Certificado de Participação - Semana Acadêmica de Engenharia de Software';

INSERT INTO academicos_certificados (id, academico_id, certificado_id, created, ch)
SELECT UUID_TO_BIN(UUID()), a.id, c.id, NOW(), 40
FROM academicos a, certificados c
WHERE a.email = 'bruno.lima@aluno.facu.edu.br'
  AND c.cabecalho = 'Certificado de Organização - Semana Acadêmica de Engenharia de Software';

INSERT INTO academicos_certificados (id, academico_id, certificado_id, created, ch)
SELECT UUID_TO_BIN(UUID()), a.id, c.id, NOW(), 8
FROM academicos a, certificados c
WHERE a.email = 'carla.rocha@aluno.facu.edu.br'
  AND c.cabecalho = 'Certificado de Palestrante - Congresso de Ciência da Computação';

INSERT INTO academicos_certificados (id, academico_id, certificado_id, created, ch)
SELECT UUID_TO_BIN(UUID()), a.id, c.id, NOW(), 24
FROM academicos a, certificados c
WHERE a.email = 'ana.souza@aluno.facu.edu.br'
  AND c.cabecalho = 'Certificado de Coordenação - Hackathon Facu 2026';
