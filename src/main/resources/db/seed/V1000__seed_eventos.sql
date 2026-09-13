-- Seed de desenvolvimento: dados fake para a tabela `eventos`.
-- Vive em db/seed (não em db/migration) e só é aplicado quando o profile
-- ativo inclui essa location (veja application-dev.properties).
--
-- Convenção de versionamento: seeds começam em V1000 para nunca colidir
-- com o número de versão das migrations estruturais em db/migration.

INSERT INTO eventos
    (evento, descricao, dt_inicio, dt_final, slug, created_by, created, updated_by, updated)
VALUES
    ('Semana Acadêmica de Engenharia de Software',
     'Palestras, oficinas e minicursos sobre práticas modernas de desenvolvimento de software.',
     '2026-10-05 08:00:00', '2026-10-09 18:00:00',
     'semana-academica-engenharia-software',
     'seed', NOW(), NULL, NULL),

    ('Congresso de Ciência da Computação',
     'Evento anual com apresentação de trabalhos científicos dos discentes e docentes.',
     '2026-11-10 09:00:00', '2026-11-12 17:00:00',
     'congresso-ciencia-computacao',
     'seed', NOW(), NULL, NULL),

    ('Hackathon Facu 2026',
     'Maratona de programação de 24 horas com premiação para as melhores soluções.',
     '2026-09-20 08:00:00', '2026-09-21 08:00:00',
     'hackathon-facu-2026',
     'seed', NOW(), NULL, NULL);
