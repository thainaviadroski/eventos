-- Seed de desenvolvimento: dados fake para a tabela `certificados`.
-- Depende de `eventos` (V1000), por isso vem depois na linha do tempo do Flyway.
-- O evento_id é resolvido por subquery via `slug` em vez de um id fixo,
-- para não quebrar caso os ids de auto-incremento mudem.

INSERT INTO certificados
    (cabecalho, corpo, rodape, descricao, tipos_certificados, evento_id, created_by, created)
SELECT
    'Certificado de Participação - Semana Acadêmica de Engenharia de Software',
    'Certificamos que o(a) participante esteve presente na Semana Acadêmica de Engenharia de Software.',
    'Coordenação de Engenharia de Software',
    'Certificado de participação emitido para os presentes no evento.',
    'PARTICIPANTE',
    e.id, 'seed', NOW()
FROM eventos e WHERE e.slug = 'semana-academica-engenharia-software';

INSERT INTO certificados
    (cabecalho, corpo, rodape, descricao, tipos_certificados, evento_id, created_by, created)
SELECT
    'Certificado de Organização - Semana Acadêmica de Engenharia de Software',
    'Certificamos que o(a) discente atuou na organização da Semana Acadêmica de Engenharia de Software.',
    'Coordenação de Engenharia de Software',
    'Certificado emitido para a comissão organizadora do evento.',
    'ORGANIZADOR',
    e.id, 'seed', NOW()
FROM eventos e WHERE e.slug = 'semana-academica-engenharia-software';

INSERT INTO certificados
    (cabecalho, corpo, rodape, descricao, tipos_certificados, evento_id, created_by, created)
SELECT
    'Certificado de Palestrante - Congresso de Ciência da Computação',
    'Certificamos que o(a) palestrante ministrou palestra durante o Congresso de Ciência da Computação.',
    'Coordenação de Ciência da Computação',
    'Certificado emitido para os palestrantes convidados do evento.',
    'PALESTRANTE',
    e.id, 'seed', NOW()
FROM eventos e WHERE e.slug = 'congresso-ciencia-computacao';

INSERT INTO certificados
    (cabecalho, corpo, rodape, descricao, tipos_certificados, evento_id, created_by, created)
SELECT
    'Certificado de Coordenação - Hackathon Facu 2026',
    'Certificamos que o(a) docente coordenou o Hackathon Facu 2026.',
    'Coordenação de Curso',
    'Certificado emitido para o(a) coordenador(a) responsável pelo evento.',
    'COORDENADOR',
    e.id, 'seed', NOW()
FROM eventos e WHERE e.slug = 'hackathon-facu-2026';
