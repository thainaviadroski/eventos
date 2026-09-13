-- Seed de desenvolvimento: dados fake para a tabela `certificado_images`.
-- Depende de `certificados` (V1002). O certificado_id é resolvido por
-- subquery via `cabecalho` em vez de um id fixo.
-- `verso` é BIT(1): b'0' = frente, b'1' = verso.

INSERT INTO certificado_images
    (path, content_type, verso, certificado_id, created)
SELECT
    'certificados_images/semana-eng-software-participante-frente.png',
    'image/png', b'0', c.id, NOW()
FROM certificados c
WHERE c.cabecalho = 'Certificado de Participação - Semana Acadêmica de Engenharia de Software';

INSERT INTO certificado_images
    (path, content_type, verso, certificado_id, created)
SELECT
    'certificados_images/semana-eng-software-participante-verso.png',
    'image/png', b'1', c.id, NOW()
FROM certificados c
WHERE c.cabecalho = 'Certificado de Participação - Semana Acadêmica de Engenharia de Software';

INSERT INTO certificado_images
    (path, content_type, verso, certificado_id, created)
SELECT
    'certificados_images/congresso-cc-palestrante-frente.png',
    'image/png', b'0', c.id, NOW()
FROM certificados c
WHERE c.cabecalho = 'Certificado de Palestrante - Congresso de Ciência da Computação';
