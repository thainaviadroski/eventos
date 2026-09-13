CREATE TABLE eventos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    evento VARCHAR(255) NOT NULL,
    descricao TEXT,
    dt_inicio DATETIME(6),
    dt_final DATETIME(6),
    slug VARCHAR(255),
    created_by VARCHAR(255),
    created DATETIME(6),
    updated_by VARCHAR(255),
    updated DATETIME(6),
    PRIMARY KEY (id)
);
