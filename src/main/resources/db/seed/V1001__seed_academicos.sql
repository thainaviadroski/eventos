-- Seed de desenvolvimento: dados fake para a tabela `academicos`.
-- Sem dependências de FK, pode rodar logo após V1000.

INSERT INTO academicos
    (academico, email, cpf, ra, created_by, created, updated_by, updated)
VALUES
    ('Ana Beatriz Souza', 'ana.souza@aluno.facu.edu.br', '111.111.111-11', 'RA2023001', 'seed', NOW(), NULL, NULL),
    ('Bruno Carvalho Lima', 'bruno.lima@aluno.facu.edu.br', '222.222.222-22', 'RA2023002', 'seed', NOW(), NULL, NULL),
    ('Carla Mendes Rocha', 'carla.rocha@aluno.facu.edu.br', '333.333.333-33', 'RA2023003', 'seed', NOW(), NULL, NULL);
